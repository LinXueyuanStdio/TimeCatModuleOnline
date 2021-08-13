package com.timecat.module.user.adapter.game

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.game.item.showItemDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
class ItemItem(
    val activity: FragmentActivity,
    val item: Block,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<ItemItem.DetailVH>(item.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.user_item_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = item.friendlyCreateTimeText()
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        val title = item.title
        holder.tv_name.setText(title)

        val structure = item.structure
        val head = ItemBlock.fromJson(structure)
        IconLoader.loadIcon(activity, holder.iv_avatar, head.header.avatar)
        holder.root.safeClick {
            val path = when (item.subtype) {
                ITEM_Thing -> RouterHub.USER_ThingItemEditorActivity
                ITEM_Package -> RouterHub.USER_PackageItemEditorActivity
                ITEM_Data -> RouterHub.USER_DataItemEditorActivity
                ITEM_Equip -> RouterHub.USER_EquipItemEditorActivity
                ITEM_Buff -> RouterHub.USER_BuffItemEditorActivity
                ITEM_Cube -> RouterHub.USER_CubeItemEditorActivity
                else -> RouterHub.USER_ThingItemEditorActivity
            }
            NAV.go(path, "block", item)
        }
        holder.root.setOnLongClickListener {
            activity.showItemDialog(item)
            true
        }

    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}