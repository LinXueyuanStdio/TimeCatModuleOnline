package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.timecat.layout.ui.drawabe.selectableItemBackground
import com.timecat.layout.ui.layout.drawable_padding
import com.timecat.layout.ui.layout.drawable_top
import com.timecat.layout.ui.layout.padding
import com.timecat.module.user.view.dsl.grayBackDrawable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/18
 * @description null
 * @usage null
 */
class IconTextItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    init {
        drawable_padding = 10
        padding = 15
        gravity = Gravity.CENTER
        background = selectableItemBackground(context)
    }

    fun setRoundIconText(icon: Int, text: String) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, grayBackDrawable(context, icon), null, null)
//        setCompoundDrawablesRelativeWithIntrinsicBounds(null, roundGrayDrawable(), null, null)
        this.text = text
    }

    fun setIconText(icon: Int, text: String) {
        drawable_top = icon
        this.text = text
    }
}