package com.timecat.module.user.adapter.detail

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.R
import com.timecat.identity.data.base.AttachmentTail
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 标签
 * @usage block 的标签
 */
class TagItem(
    val activity: Activity,
    val block: Block,
    val mediaScope: AttachmentTail? = null
) : BaseDetailItem<TagItem.DetailVH>("标签") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.user_base_item_media

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
        }
    }

}