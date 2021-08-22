package com.timecat.module.user.adapter.block

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.Payload
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description 无更多 已经到底了哦
 * @usage null
 */
class NotMoreItem : BaseDetailItem<NotMoreItem.DetailVH>("无更多") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val progress_bar: ProgressBar by lazy { root.findViewById<ProgressBar>(R.id.progress_bar) }
        val progress_message: TextView by lazy { root.findViewById<TextView>(R.id.progress_message) }
    }

    override fun getLayoutRes(): Int = R.layout.user_base_item_loadmore_footer

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    enum class Status {
        MORE_TO_LOAD,  //Default = should have an empty Payload
        DISABLE_ENDLESS,  //Endless is disabled because user has set limits
        NO_MORE_LOAD,  //Non-empty Payload = Payload.NO_MORE_LOAD
        ON_CANCEL, ON_ERROR
    }

    var status: Status = Status.NO_MORE_LOAD

    init {
        isHidden = true
        isEnabled = false
        isDraggable = false
        isSwipeable = false
        isSelectable = false
    }

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)

        val context = holder.itemView.context
        holder.progress_bar.setVisibility(View.GONE)
        holder.progress_message.setVisibility(View.VISIBLE)

        if (!adapter.isEndlessScrollEnabled) {
            status = Status.DISABLE_ENDLESS
        } else if (payloads!!.contains(Payload.NO_MORE_LOAD)) {
            status = Status.NO_MORE_LOAD
        }

        when (status) {
            Status.NO_MORE_LOAD -> {
                holder.progress_message.setText(
                    context.getString(R.string.no_more_load_retry)
                )
                // Reset to default status for next binding
                status = Status.MORE_TO_LOAD
            }
            Status.DISABLE_ENDLESS -> holder.progress_message.setText(
                context.getString(R.string.endless_disabled)
            )
            Status.ON_CANCEL -> {
                holder.progress_message.setText(context.getString(R.string.endless_cancel))
                // Reset to default status for next binding
                status = Status.MORE_TO_LOAD
            }
            Status.ON_ERROR -> {
                holder.progress_message.setText(context.getString(R.string.endless_error))
                // Reset to default status for next binding
                status = Status.MORE_TO_LOAD
            }
            else -> {
                holder.progress_bar.setVisibility(View.VISIBLE)
                holder.progress_message.setVisibility(View.GONE)
            }
        }
    }

}