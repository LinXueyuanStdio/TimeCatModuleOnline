package com.timecat.module.user.app.online

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.adapter.vh.BaseRecordCardVH
import com.timecat.module.user.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/8/17
 * @description 容器的头部
 * @usage null
 */
open class HomeHeaderCard(
    val tabs: List<MapSubItem>,
    val selectedIdx: Int = 0,
    var listener: Listener
) : BaseItem<HomeHeaderCard.RecordCardVH>("HEADER") {

    interface Listener {
        fun onSelect(item: MapSubItem)
    }

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): RecordCardVH {
        return RecordCardVH(view, adapter)
    }

    override fun getLayoutRes(): Int = R.layout.user_card_home_header

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: RecordCardVH,
        position: Int,
        payloads: List<Any>
    ) {
        holder.front_view.removeAllTabs()
        tabs.withIndex().forEach {
            val tab = holder.front_view.newTab()
            tab.text = it.value.title
            holder.front_view.addTab(tab, it.index == selectedIdx)
        }
        holder.front_view.clearOnTabSelectedListeners()
        holder.front_view.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val item = tabs.find { it.title == tab.text } ?: return
                listener.onSelect(item)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    inner class RecordCardVH(
        v: View,
        adapter: FlexibleAdapter<*>
    ) : BaseRecordCardVH(v, adapter) {
        val front_view: TabLayout by lazy { v.findViewById<TabLayout>(R.id.front_view) }
    }

    init {
        isDraggable = false
        isSelectable = false
        isSwipeable = false
    }
}