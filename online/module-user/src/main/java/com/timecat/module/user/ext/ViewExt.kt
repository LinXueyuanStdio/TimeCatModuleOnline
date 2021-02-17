package com.timecat.module.user.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.layout.ui.business.form.wrapContext
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import kotlinx.android.synthetic.main.user_base_item_user_head.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/17
 * @description null
 * @usage null
 */
fun ViewGroup.Layout(
    layoutId: Int,
    style: Int? = null,
    autoAdd: Boolean = true
): View = LayoutInflater.from(style.wrapContext(context)).inflate(layoutId, if (autoAdd) this else null)

fun ViewGroup.AvatarHead(
    avatar: String,
    title: String,
    content: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    onClick: (View) -> Unit = {}
): View = Layout(R.layout.user_base_item_user_head, style, autoAdd).apply {
    IconLoader.loadIcon(context, head_image, avatar)
    head_title.setText(title)
    head_content.setText(content)
    head_content.beVisible()
    head_more.beGone()
    setShakelessClickListener(onClick = onClick)
}
