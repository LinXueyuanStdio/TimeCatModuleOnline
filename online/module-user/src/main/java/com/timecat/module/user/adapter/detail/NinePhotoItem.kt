package com.timecat.module.user.adapter.detail

import android.app.Activity
import android.view.View
import com.timecat.extend.image.IMG
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.layout.ui.business.ninegrid.NineGridView
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
class NinePhotoItem(
    val activity: Activity,
    val objectId: String,
    val mediaScope: AttachmentTail? = null
) : BaseDetailItem<NinePhotoItem.DetailVH>("九宫格图片") {

    inner class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        private val ninePhoto: NineGridView by lazy { root.findViewById(R.id.nine_photo) }

        fun setMediaScope(objectId: String, mediaScope: AttachmentTail? = null) {
            ninePhoto.visibility = View.GONE
            mediaScope?.let {
                ninePhoto.apply {
                    if (root.tag != objectId) return
                    visibility = View.VISIBLE
                    val datas = it.attachmentItems.map {
                        it.attachment
                    }
                    setUrls(datas)
                    setCallback(object : NineGridView.SimpleCallback() {
                        override fun onImageItemClicked(position: Int, urls: MutableList<String>) {
                            IMG.preview(activity)
                                .setIndex(position)
                                .setImageList(urls)
                                .start()
                        }
                    })
                }
            }
        }
    }

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
        holder.root.tag = objectId
        holder.setMediaScope(objectId, mediaScope)
    }


}