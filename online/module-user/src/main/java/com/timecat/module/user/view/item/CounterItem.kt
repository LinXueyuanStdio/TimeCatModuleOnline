package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
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
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    lateinit var imageView: ImageView
    lateinit var closeView: ImageView
    lateinit var leftView: InputItem
    lateinit var rightView: InputItem
    lateinit var limit: NumberPicker
    var container: LinearLayout

    init {
        layout_width = match_parent
        layout_height = wrap_content

        container = LinearLayout {
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
            leftView = InputItem(context).apply {
                layout_width = wrap_content
                layout_height = wrap_content
                minimumWidth = 50.dp
            }.also {
                addView(it)
            }
            rightView = InputItem(context).apply {
                layout_width = wrap_content
                layout_height = wrap_content
                minimumWidth = 50.dp
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
                layout_gravity = gravity_bottom
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
            leftView.apply(value)
        }
    var right_field: InputItem.() -> Unit = {}
        set(value) {
            rightView.apply(value)
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