package com.timecat.module.user.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-09
 * @description null
 * @usage null
 */
class ImageAdapter(data: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.app_item_image, data) {
    fun image(url: String, v: ImageView) {
        val u = if (url.startsWith("http://bmob")) {
            "R.drawable.ic_launcher"
        } else url
        IconLoader.loadIcon(context, v, u)
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        image(item, holder.getView(R.id.img))
    }
}