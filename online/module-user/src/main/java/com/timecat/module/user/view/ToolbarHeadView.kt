package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import android.widget.ViewFlipper
import com.timecat.layout.ui.business.form.H1

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/18
 * @description null
 * @usage null
 */
class ToolbarHeadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewFlipper(context, attrs) {
    var title: TextView = H1("动态")
    var head: BlockHerfView = BlockHerfView(context)

    init {
        addView(head)
    }
}