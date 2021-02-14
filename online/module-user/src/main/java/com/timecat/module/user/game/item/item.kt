package com.timecat.module.user.game.item

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.leancloud.AVQuery
import com.google.android.material.button.MaterialButton
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.Reward
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.H1
import com.timecat.layout.ui.business.form.add
import com.timecat.layout.ui.business.setting.RewardItem
import com.timecat.layout.ui.business.setting.RewardListItem
import com.timecat.layout.ui.business.setting.StepSliderItem
import com.timecat.layout.ui.layout.setShakelessClickListener

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

fun ViewGroup.RewardList(activity: FragmentActivity, rewardList: List<Reward>) {
    val rewardListItem = buildRewardListItem(activity, rewardList)
    addView(rewardListItem)
}

fun ViewGroup.BigTitle(title: String) {
    add {
        H1(title).apply {
            gravity = Gravity.CENTER
            setTextColor(Attr.getPrimaryTextColor(context))
            setTextSize(20f)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.MIDDLE
        }
    }
}

fun ViewGroup.Button(text: String, onClick: (View) -> Unit): Button {
    val button = MaterialButton(context)
    button.setText(text)
    button.setShakelessClickListener(800, onClick)
    addView(button)
    return button
}

fun ViewGroup.StepSliderButton(
    maxCount: Int = 1,
    text: String,
    onClick: (View, count: Int) -> Unit
): Button {
    if (maxCount <= 1) {
        return Button(text) { onClick(it, 1) }
    }
    val stepSliderItem = StepSliderItem(context).apply {
        value = 1f
        valueFrom = 1f
        valueTo = maxCount.toFloat()
        stepSize = 1f
    }
    addView(stepSliderItem)
    val button = Button("${text} 1") {
        onClick(it, stepSliderItem.value.toInt())
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