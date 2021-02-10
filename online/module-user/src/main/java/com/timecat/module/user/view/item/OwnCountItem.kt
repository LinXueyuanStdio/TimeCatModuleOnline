package com.timecat.module.user.view.item

import android.content.Context
import android.content.res.ColorStateList
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description 图片的设置项
 * @usage 不要在xml里写文案，要在java里写
 */
class OwnCountItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    lateinit var imageView: ImageView
    lateinit var closeView: ImageView
    lateinit var left: InputItem
    lateinit var right: InputItem

    init {
        orientation = HORIZONTAL
        layout_width = match_parent
        layout_height = wrap_content
        padding = 4
        margin = 4
        imageView = ImageView {
            layout_width = 24
            layout_height = 24
            margin = 4
            layout_gravity = gravity_center
            isClickable = false
            weight = 1f
        }
        left = InputItem(context).apply{
            weight = 4f
        }.also{
            addView(it)
        }
        right = InputItem(context).apply{
            weight = 4f
        }.also{
            addView(it)
        }
        closeView = ImageView {
            layout_width = 24
            layout_height = 24
            margin = 4
            layout_gravity = gravity_center
            isClickable = false
            weight = 1f
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
    var left_field: InputItem.()->Unit = {}
        set(value) {
            left.apply(value)
        }
    var right_field: InputItem.()->Unit = {}
        set(value) {
            right.apply(value)
        }

    fun onClick(onClick: (item: OwnCountItem) -> Unit) {
        setShakelessClickListener {
            onClick(this@OwnCountItem)
        }
    }

}