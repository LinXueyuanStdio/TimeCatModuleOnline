package com.timecat.module.user.ext

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.layout.ui.business.form.wrapContext
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.view.UserHeadView

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
): View = UserHeadView(style.wrapContext(context)).apply {
    icon = avatar
    this.title = title
    this.content = content
    this.levelView.beGone()
    this.followView.beGone()
    this.moreView.beGone()
    setShakelessClickListener(onClick = onClick)
}.also { if (autoAdd) addView(it) }

