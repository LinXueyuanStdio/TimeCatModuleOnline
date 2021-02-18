package com.timecat.module.user.view.dsl

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.dp
import top.defaults.drawabletoolbox.DrawableBuilder
import top.defaults.drawabletoolbox.LayerDrawableBuilder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/18
 * @description null
 * @usage null
 */
fun roundGrayDrawable(color: Int = Color.GRAY) = DrawableBuilder()
    .rectangle()
    .rounded()
    .solidColor(color)
    .build()

fun grayBackDrawable(context: Context, icon: Int): Drawable {
    val drawable = Attr.getDrawable(context, icon)
    val grayRoundDrawable = DrawableBuilder()
        .rounded()
        .size(50.dp)
        .solidColor(Attr.getBackgroundDarkColor(context))
        .build()
    return LayerDrawableBuilder()
        .add(grayRoundDrawable)
        .add(drawable).inset(10.dp)
        .build()
}
