package com.timecat.module.user.adapter.detail

import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
abstract class BaseDetailItem<T : FlexibleViewHolder>(
    id: String = ""
) : BaseItem<T>(id) {
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: T,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        if (adapter is BaseAdapter) {
            adapter.bindViewHolderAnimation(holder)
        }
    }
}