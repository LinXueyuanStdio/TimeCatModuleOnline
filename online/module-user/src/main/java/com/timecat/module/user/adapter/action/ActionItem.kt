package com.timecat.module.user.adapter.action

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.timecat.data.bmob.data.common.Action
import com.timecat.identity.data.action.*
import com.timecat.identity.data.block.type.*
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.friendlyCreateTimeText
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
class ActionItem(
    val activity: Activity,
    val action: Action,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<ActionItem.DetailVH>(action.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.user_block_small_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = action.friendlyCreateTimeText()
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        LOAD.image(action.user.avatar, holder.iv_avatar)
        holder.root.safeClick {
            val block = action.block
            GO.toAnyDetail(block)
        }
        val block = action.block
        val title = block.title
        val typeStr = when (block.type) {
            BLOCK_MOMENT -> "动态"
            BLOCK_FORUM -> "论坛"
            BLOCK_TOPIC -> "主题"
            BLOCK_TAG -> "标签"
            BLOCK_POST -> "帖子"
            BLOCK_COMMENT -> "评论"
            BLOCK_APP -> "App"
            BLOCK_LEADER_BOARD -> "排行榜"
            else -> ""
        }
        when (action.type) {
            ACTION_CLICK -> {
                holder.tv_name.setText("${action.user.nickName} 点击 $typeStr ${title}")
            }
            ACTION_LIKE -> {
                holder.tv_name.setText("${action.user.nickName} 点赞 $typeStr ${title}")
            }
            ACTION_DING -> {
                holder.tv_name.setText("${action.user.nickName} 拍了拍 $typeStr ${title}")
            }
            ACTION_SCORE -> {
                holder.tv_name.setText("${action.user.nickName} 评分 $typeStr ${title}")
            }
            ACTION_DOWNLOAD -> {
                holder.tv_name.setText("${action.user.nickName} 下载 $typeStr ${title}")
            }
            ACTION_FOCUS -> {
                holder.tv_name.setText("${action.user.nickName} 关注 $typeStr ${title}")
            }
            ACTION_VOTE -> {
                holder.tv_name.setText("${action.user.nickName} 投票 $typeStr ${title}")
            }
            ACTION_RECOMMEND -> {
                holder.tv_name.setText("${action.user.nickName} 推荐 $typeStr ${title}")
            }
        }
    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}