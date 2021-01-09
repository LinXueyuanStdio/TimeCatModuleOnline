package com.timecat.module.user.adapter.block

import android.app.Activity
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ForumBlock
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.ext.simpleAvatar
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class BlockSmallItem(
        val activity: Activity,
        val block: Block,
        val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<BlockSmallItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.leaderboard_board_item

    override fun createViewHolder(
            view: View,
            adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = block.friendlyCreateTimeText()

    override fun bindViewHolder(
            adapter: FlexibleAdapter<IFlexible<*>>,
            holder: DetailVH,
            position: Int,
            payloads: MutableList<Any>?
    ) {
        when (block.type) {
            BLOCK_LEADER_BOARD -> {
                val leaderboard: Block = block
                holder.tv_name.setText(leaderboard.title)
                LOAD.image(leaderboard.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    GO.leaderBoardDetail(leaderboard.objectId)
                }
            }
            BLOCK_FORUM -> {
                val forum: Block = block
                holder.tv_name.setText(forum.title)
                val header = ForumBlock.fromJson(forum.structure)
                val url = header.header?.icon ?: "R.drawable.ic_launcher"
                LOAD.image(url, holder.iv_avatar)
                holder.root.safeClick {
                    GO.forumDetail(block.objectId)
                }
            }
            BLOCK_APP -> {
                val appBlock: Block = block
                holder.tv_name.setText(appBlock.title)
                LOAD.image(appBlock.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    GO.appDetail(appBlock.objectId)
                }
            }
            BLOCK_TOPIC -> {
                val block: Block = block
                holder.tv_name.setText(block.title)
                LOAD.image(block.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    GO.topicDetail(block.objectId)
                }
            }
            BLOCK_TAG -> {
                val block: Block = block
                holder.tv_name.setText(block.title)
                LOAD.image(block.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    GO.tagDetail(block.objectId)
                }
            }
            BLOCK_PERMISSION -> {
                val block: Block = block
                holder.tv_name.setText(block.title)
                LOAD.image(block.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    when (block.subtype) {
                        PERMISSION_Hun -> {
                            GO.addHunPermission(block)
                        }
                        PERMISSION_Meta -> {
                            GO.addMetaPermission(block)
                        }
                    }

                }
            }
            BLOCK_ROLE -> {
                val block: Block = block
                holder.tv_name.setText(block.title)
                LOAD.image(block.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    GO.addRole(block)
                }
            }
            BLOCK_IDENTITY -> {
                val block: Block = block
                holder.tv_name.setText(block.title)
                LOAD.image(block.simpleAvatar(), holder.iv_avatar)
                holder.root.safeClick {
                    GO.addIdentity(block)
                }
            }
        }
    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }

}