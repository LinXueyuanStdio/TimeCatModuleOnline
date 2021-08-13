package com.timecat.module.user.adapter.block

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.CubeBlock
import com.timecat.identity.data.block.IdentityBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
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
 * @date 2021/2/13
 * @description null
 * @usage null
 */
class IdentityItem(
    val activity: Activity,
    val block: Block,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<IdentityItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
        val roles: View = root.findViewById(R.id.roles)
        val attr: View = root.findViewById(R.id.attr)
        val star: View = root.findViewById(R.id.star)
        val skill: View = root.findViewById(R.id.skill)
        val setting: View = root.findViewById(R.id.setting)
    }

    override fun getLayoutRes(): Int = R.layout.user_block_cube_item

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
        holder.tv_name.setText(block.title)
        val head = IdentityBlock.fromJson(block.structure)
        IconLoader.loadIcon(activity, holder.iv_avatar, head.header.avatar)
        val head2 = CubeBlock.fromJson(head.structure)
        holder.root.safeClick {
            GO.addIdentity(block)
        }
        holder.root.setOnLongClickListener {
            NAV.go(RouterHub.USER_SendCubeActivity, "block", block)
            true
        }
        holder.skill.setShakelessClickListener {
            NAV.go(RouterHub.USER_CubeSkillEditorActivity)
        }
        holder.star.setShakelessClickListener {
            NAV.go(RouterHub.USER_CubeStarEditorActivity)
        }
        holder.attr.setShakelessClickListener {
            NAV.go(RouterHub.USER_CubeAttrEditorActivity)
        }
        holder.roles.setShakelessClickListener {
            NAV.go(RouterHub.USER_CubeRolesEditorActivity)
        }
        holder.setting.setShakelessClickListener {
            NAV.go(RouterHub.USER_CubeSettingEditorActivity)
        }
    }


    protected fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}