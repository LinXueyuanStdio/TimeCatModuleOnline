package com.timecat.module.user.game.item

import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.leancloud.AVQuery
import com.google.android.material.button.MaterialButton
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.Reward
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.view.item.RewardItem
import com.timecat.module.user.view.item.RewardListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
                    val fragment: Fragment = NAV.rawFragment(RouterHub.USER_ItemDetailFragment)
                        .putParcelable("item", block)
                        .navigate() ?: FallBackFragment()
                    if (fragment is DialogFragment) {
                        fragment.show(activity.supportFragmentManager, id)
                    }
                }
            }

            rewardListItem.rewards = rewards
        }
    }
    return rewardListItem
}

fun MaterialForm.RewardList(activity: FragmentActivity, rewardList: List<Reward>) {
    val rewardListItem = buildRewardListItem(activity, rewardList)
    container.addView(rewardListItem)
}

fun MaterialForm.BigTitle(title: String) {
    H1(title).apply {
        gravity = Gravity.CENTER
        setTextColor(Attr.getPrimaryTextColor(context))
        setTextSize(20f)
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.MIDDLE
    }
}

fun MaterialForm.Button(text: String, onClick: (View) -> Unit): Button {
    val button = MaterialButton(windowContext)
    button.setText(text)
    button.setShakelessClickListener(800, onClick)
    container.addView(button)
    return button
}