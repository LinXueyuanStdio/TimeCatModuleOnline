package com.timecat.module.user.view.item

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class CounterItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var slider: TextView
    var minusView: Chip
    var plusView: Chip

    init {
        orientation = HORIZONTAL
        layout_width = wrap_content
        layout_height = wrap_content
        padding = 4
        margin = 4
        minusView = Chip(context).apply {
            layout_width = wrap_content
            layout_height = wrap_content
            padding = 8
            margin = 4
            layout_gravity = gravity_center
            gravity = gravity_center
            setTextColor(Attr.getSecondaryTextColor(context))
            setTextSize(24f)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.MIDDLE
            text = "-"
            onClick = {
                value -= stepSize
                intValue -= stepSize.toInt()
            }
        }.also {
            addView(it)
        }

        slider = TextView {
            layout_width = wrap_content
            layout_height = wrap_content
            padding = 8
            margin = 4
            layout_gravity = gravity_center
            gravity = gravity_center
            setTextColor(Attr.getSecondaryTextColor(context))
            setTextSize(24f)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.MIDDLE
        }

        plusView = Chip(context).apply {
            layout_width = wrap_content
            layout_height = wrap_content
            padding = 8
            margin = 4
            layout_gravity = gravity_center
            gravity = gravity_center
            isClickable = false
            setTextColor(Attr.getSecondaryTextColor(context))
            setTextSize(24f)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.MIDDLE
            text = "+"
            onClick = {
                value += stepSize
                intValue += stepSize.toInt()
            }
        }.also {
            addView(it)
        }
    }

    var stepSize: Float = 1f

    var value: Float = 1f
        set(value) {
            slider.text = "${value.toInt()}"
            field = value
            onCount(value)
        }

    var intValue: Int = 1
        set(value) {
            slider.text = "${value}"
            field = value
            onIntCount(value)
        }

    var onCount: (Float)->Unit={}
    var onIntCount: (Int)->Unit={}
}