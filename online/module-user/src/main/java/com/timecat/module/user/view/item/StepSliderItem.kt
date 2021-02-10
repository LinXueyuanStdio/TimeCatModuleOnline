package com.timecat.module.user.view.item

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.slider.Slider
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description 图片的设置项
 * @usage 不要在xml里写文案，要在java里写
 */
class StepSliderItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var slider: Slider
    lateinit var plusView: TextView
    lateinit var minusView: TextView

    init {
        orientation = HORIZONTAL
        layout_width = match_parent
        layout_height = wrap_content
        padding = 4
        margin = 4
        minusView = TextView {
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
            text = "-"
            onClick = {
                value--
            }
        }

        slider = Slider(context).apply {
            layout_width = 0
            layout_height = wrap_content
            layout_gravity = gravity_center
            weight = 1f
        }.also {
            addView(it)
        }

        plusView = TextView {
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
                value++
            }
        }
    }

    var valueFrom
        get() = slider.valueFrom
        set(value) {
            slider.valueFrom = value
        }

    var valueTo
        get() = slider.valueTo
        set(value) {
            slider.valueTo = value
        }

    var stepSize
        get() = slider.stepSize
        set(value) {
            slider.stepSize = value
        }

    var value
        get() = slider.value
        set(value) {
            slider.value = when {
                value < valueFrom -> valueFrom
                value > valueTo -> valueTo
                else -> value
            }
        }

    fun onSlide(onChange: (value: Float) -> Unit) {
        slider.addOnChangeListener { _, value, _ -> onChange(value) }
    }
}