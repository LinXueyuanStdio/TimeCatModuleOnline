package com.timecat.module.user.adapter.block

import android.view.View
import android.widget.TextView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description 点击加载更多
 * @usage null
 */
class ClickToLoadMore(
    val onClick: () -> Unit
) : BaseDetailItem<ClickToLoadMore.DetailVH>("点击加载更多") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.base_text_view

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)
        holder.apply {
            root.findViewById<TextView>(R.id.textview).setText("刚刚看到这里，点击刷新")
            root.setShakelessClickListener {
                onClick()
            }
        }
    }

}