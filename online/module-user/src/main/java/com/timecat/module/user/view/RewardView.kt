package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.*
import com.timecat.identity.data.block.Reward
import com.timecat.layout.ui.layout.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 用户的卡片视图
 * @usage null
 */
class RewardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr)  {

    lateinit var container: ViewGroup

    var rewards: List<Reward> = listOf()
        set(value) {
            container.removeAllViews()
            value.forEach {

            }
            field = value
        }

    init {
        layout_width = match_parent
        layout_height = wrap_content

        LinearLayout {
            container = this
            layout_id = "placeholder"
            layout_width = 0
            layout_height = wrap_content
            orientation =  LinearLayout.HORIZONTAL
        }
    }
}