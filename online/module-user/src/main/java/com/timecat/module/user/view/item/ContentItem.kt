package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import com.shuyu.textutillib.RichTextView
import com.timecat.layout.ui.layout.padding
import com.timecat.module.user.R
import com.timecat.module.user.game.cube.CubeLevel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class ContentItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.TimeCatWidget_EditText_MomentLike
) : RichTextView(context, attrs, defStyleAttr) {
    init {
        padding = 10
    }
}