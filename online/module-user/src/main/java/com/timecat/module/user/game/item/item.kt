package com.timecat.module.user.game.item

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.leancloud.AVQuery
import com.google.android.material.button.MaterialButton
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.Reward
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.H1
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.business.form.StepSlide
import com.timecat.layout.ui.business.setting.RewardItem
import com.timecat.layout.ui.business.setting.RewardListItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/10
 * @description null
 * @usage null
 */
fun buildRewardListItem(activity: FragmentActivity, items: List<Reward>): RewardListItem {
    val rewardListItem = RewardListItem(activity)
    val itemId2Count = mutableMapOf<String, Long>()
    for (i in items) {
        itemId2Count[i.uuid] = i.count
    }
    requestBlock {
        query = allItem().apply {
            whereContainedIn("objectId", items.map { it.uuid })
            cachePolicy = AVQuery.CachePolicy.CACHE_ELSE_NETWORK
        }
        onSuccess = { blocks ->
            val blockMap = mutableMapOf<String, Block>()
            val rewards = blocks.map {
                blockMap[it.objectId] = it
                val header = ItemBlock.fromJson(it.structure)
                val avatar = header.header.avatar
                val count = itemId2Count[it.objectId] ?: 0
                val name = it.title
                RewardItem.Reward(it.objectId, avatar, count, name)
            }
            rewardListItem.onClick = {
                val reward = it.reward
                val id = reward.uuid
                val block = blockMap[id]
                if (block != null) {
                    activity.showItemDialog(block)
                }
            }

            rewardListItem.rewards = rewards
        }
    }
    return rewardListItem
}

fun buildOwnRewardListItem(activity: Context, items: List<OwnItem>, onSelect:(RewardItem, Block, Int)->Unit): RewardListItem {
    val rewardListItem = RewardListItem(activity)
    val id2Item = mutableMapOf<String, OwnItem>()
    for (i in items) {
        id2Item[i.objectId] = i
    }
    val rewards = items.map {
        val block = it.item
        val header = ItemBlock.fromJson(block.structure)
        val avatar = header.header.avatar
        val name = block.title
        RewardItem.Reward(it.objectId, avatar, it.count.toLong(), name)
    }
    rewardListItem.onClick = {
        val reward = it.reward
        val id = reward.uuid
        val ownItem = id2Item[id]
        if (ownItem != null) {
            onSelect(it, ownItem.item, ownItem.count)
        }
    }
    rewardListItem.rewards = rewards
    return rewardListItem
}

fun ViewGroup.RewardList(activity: FragmentActivity, rewardList: List<Reward>) {
    val rewardListItem = buildRewardListItem(activity, rewardList)
    addView(rewardListItem)
}

fun ViewGroup.BigTitle(title: String) {
    H1(title).apply {
        gravity = Gravity.CENTER
        setTextColor(Attr.getPrimaryTextColor(context))
        setTextSize(20f)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.MIDDLE
    }
}

fun ViewGroup.StepSliderButton(
    text: String,
    maxCount: Int = 2,
    defaultCount: Int = 1,
    onClick: (TextView, count: Int) -> Unit
): Button {
    if (maxCount <= defaultCount) {
        return MaterialButton(text) { onClick(it as TextView, defaultCount) }
    }
    val stepSliderItem = StepSlide(
        from = 1f, to = maxCount.toFloat(), step = 1f,
        defaultValue = defaultCount.toFloat()
    )
    val button = MaterialButton("$text $defaultCount") {
        onClick(it as TextView, stepSliderItem.value.toInt())
    }
    stepSliderItem.onSlide {
        button.text = "${text} ${it.toInt()}"
    }
    return button
}

fun FragmentActivity.showItemDialog(item: Block) {
    val fragment: Fragment = NAV.rawFragment(RouterHub.USER_ItemDetailFragment)
        .putParcelable("item", item)
        .navigate() ?: FallBackFragment()
    if (fragment is DialogFragment) {
        fragment.show(supportFragmentManager, item.objectId)
    }
}
fun FragmentActivity.showOwnItemDialog(ownItem: OwnItem) {
    val fragment: Fragment = NAV.rawFragment(RouterHub.USER_OwnItemDetailFragment)
        .putParcelable("ownItem", ownItem)
        .navigate() ?: FallBackFragment()
    if (fragment is DialogFragment) {
        fragment.show(supportFragmentManager, ownItem.objectId)
    }
}
fun FragmentActivity.showOwnMailDialog(ownMail: OwnMail) {
    val fragment: Fragment = NAV.rawFragment(RouterHub.USER_MailDetailFragment)
        .putParcelable("ownMail", ownMail)
        .navigate() ?: FallBackFragment()
    if (fragment is DialogFragment) {
        fragment.show(supportFragmentManager, ownMail.uuid)
    }
}