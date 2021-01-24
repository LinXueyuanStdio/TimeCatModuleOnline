package com.timecat.module.user.adapter.detail

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 一个作者
 * @usage 可关注作者，点击进入作者个人详情页
 */
class TraceItem(val activity: Activity, val user: User) : BaseDetailItem<TraceItem.DetailVH>("浏览记录") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val traceEntry: TextView = root.findViewById(R.id.trace_entry)
    }

    override fun getLayoutRes(): Int = R.layout.user_base_item_trace_entry

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
        holder.traceEntry.apply {
            text = "浏览记录"
            beVisible()
            setShakelessClickListener {
                NAV.raw(activity, RouterHub.USER_AllTraceActivity)
                    .putParcelable("user", user)
                    .forward()
            }
        }
    }

}