package com.timecat.module.user.adapter.game

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
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
class Item(
    val activity: FragmentActivity,
    val item: Block,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<Item.DetailVH>(item.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.user_item_bag

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
        LOAD.image(head.header.avatar, holder.iv_avatar)
        holder.root.safeClick {
        }

    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}