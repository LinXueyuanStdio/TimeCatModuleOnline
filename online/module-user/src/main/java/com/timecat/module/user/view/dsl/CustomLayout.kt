package com.timecat.module.user.view.dsl

import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import com.timecat.module.user.view.widget.UserCircleImageView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description 对自定义 View 使用 DSL 来简化布局构建
 * @usage null
 */

inline fun ViewGroup.UserCircleImageView(
    style: Int? = null,
    autoAdd: Boolean = true,
    init: UserCircleImageView.() -> Unit
) {
    val imageView =
        if (style != null) UserCircleImageView(
            ContextThemeWrapper(context, style)
        ) else UserCircleImageView(context)
    imageView.apply(init).also { if (autoAdd) addView(it) }
}



