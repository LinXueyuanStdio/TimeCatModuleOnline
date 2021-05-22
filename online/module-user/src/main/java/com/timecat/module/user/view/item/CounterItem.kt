package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker.doOnProgressChanged

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class TriInputItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var imageView: ImageView
    var closeView: ImageView
    var left: InputItem
    var right: InputItem
    var limit: NumberPicker

    init {
        orientation = HORIZONTAL
        layout_width = match_parent
        layout_height = wrap_content
        imageView = ImageView {
            layout_width = 36
            layout_height = 36
            margin = 4
            padding = 4
            layout_gravity = gravity_center_vertical
        }
        left = InputItem(context).apply {
            layout_width = wrap_content
            layout_height = wrap_content
            weight = 4f
            padding = 4
        }.also {
            addView(it)
        }
        right = InputItem(context).apply {
            layout_width = wrap_content
            layout_height = wrap_content
            weight = 4f
            padding = 4
        }.also {
            addView(it)
        }
        limit = NumberPicker(context).apply {
            layout_width = wrap_content
            layout_height = wrap_content
            weight = 4f
            padding = 4
            minValue = 1
            maxValue = 999
            stepSize = 1
            progress = 1
            doOnProgressChanged { numberPicker, progress, formUser ->
                onCount(progress)
            }
        }.also {
            addView(it)
        }
        closeView = ImageView {
            layout_width = 36
            layout_height = 36
            margin = 4
            padding = 4
            layout_gravity = gravity_center_vertical
        }
    }

    var closeIcon: String = "R.drawable.ic_close"
        set(value) {
            IconLoader.loadIcon(context, closeView, value)
            field = value
        }
    var icon: String = "R.drawable.ic_launcher"
        set(value) {
            IconLoader.loadIcon(context, imageView, value)
            field = value
        }
    var left_field: InputItem.() -> Unit = {}
        set(value) {
            left.apply(value)
        }
    var right_field: InputItem.() -> Unit = {}
        set(value) {
            right.apply(value)
        }
    var onCount: (Int) -> Unit = {}

    fun onIconClick(onClick: (item: ImageView) -> Unit) {
        imageView.setShakelessClickListener {
            onClick(imageView)
        }
    }

    fun onCloseIconClick(onClick: (item: ImageView) -> Unit) {
        closeView.setShakelessClickListener {
            onClick(closeView)
        }
    }

}