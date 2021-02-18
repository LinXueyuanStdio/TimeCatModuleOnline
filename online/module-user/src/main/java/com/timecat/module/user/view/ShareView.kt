package com.timecat.module.user.view

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*
import com.timecat.module.user.R
import com.timecat.module.user.view.dsl.UserCircleImageView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/18
 * @description null
 * @usage null
 */
class ShareView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        layout_width = match_parent
        layout_height = wrap_content
        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        TextView {
            layout_width = wrap_content
            layout_height = wrap_content
            margin = 10
            padding = 10

            text = "分享"
            setTextColor(Attr.getSecondaryTextColor(context))
        }
        CircleImage(R.drawable.ic_qq) {
        }
        CircleImage(R.drawable.ic_wechat) {
        }
        CircleImage(R.drawable.ic_category_circle) {
        }
        CircleImage(R.mipmap.twitter) {
        }
        CircleImage(R.drawable.ic_more_horiz_24dp) {
        }
    }

    fun LinearLayout.CircleImage(src: Int, onClick: (View) -> Unit) = UserCircleImageView {
        layout_width = 40
        layout_height = 40
        margin = 5
        padding = 5
        val typedArray = context.obtainStyledAttributes(intArrayOf(R.attr.selectableItemBackground))
        background = typedArray.getDrawable(0)
        typedArray.recycle()
        imageTintList = ColorStateList.valueOf(Attr.getSecondaryTextColor(context))
        this.src = src
        setShakelessClickListener(onClick = onClick)
    }

    var blockId: String = ""
}