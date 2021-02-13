package com.timecat.module.user.adapter.block

import android.app.Activity
import android.view.View
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.simpleAvatar
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/13
 * @description null
 * @usage null
 */
class RoleItem(
    activity: Activity,
    block: Block,
    onClick: ((View) -> Unit)? = null
) : BlockSmallItem(activity, block, onClick) {
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        holder.tv_name.setText(block.title)
        LOAD.image(block.simpleAvatar(), holder.iv_avatar)
        holder.root.safeClick {
            GO.leaderBoardDetail(block.objectId)
        }
    }
}