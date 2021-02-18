package com.timecat.module.user.view.dsl

import android.graphics.Color
import top.defaults.drawabletoolbox.DrawableBuilder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/18
 * @description null
 * @usage null
 */
fun roundGrayDrawable(color:Int=Color.GRAY) = DrawableBuilder()
    .rectangle()
    .rounded()
    .solidColor(color)
    .build()