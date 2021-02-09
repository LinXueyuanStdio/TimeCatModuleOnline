package com.timecat.module.user.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.component.identity.Attr
import com.timecat.identity.data.block.Reward
import com.timecat.layout.ui.layout.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 用户的卡片视图
 * @usage null
 */
class TaskCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var titleView: TextView
    lateinit var button: Button
    lateinit var placeholder: View
    lateinit var rewardView: RewardView

    var title: String = ""
        set(value) {
            titleView.text = value
            field = value
        }

    var rewards: List<Reward> = listOf()
        set(value) {
            rewardView.rewards = value
            field = value
        }
    var buttonText: String = ""
        set(value) {
            button.text = value
            field = value
        }
    var buttonColor: Int = Color.WHITE
        set(value) {
            button.backgroundTintList = ColorStateList.valueOf(value)
            field = value
        }
    var buttonClick: (View) -> Unit = {}
        set(value) {
            button.setShakelessClickListener(onClick = value)
        }

    init {
        layout_width = match_parent
        layout_height = wrap_content

        View {
            placeholder = this
            layout_id = "placeholder"
            layout_width = 0
            layout_height = wrap_content

            start_toStartOf = parent_id
            top_toTopOf = parent_id
            end_toEndOf = parent_id
        }
        TextView {
            titleView = this
            layout_id = "title"
            layout_width = 0
            layout_height = wrap_content

            margin_start = 10
            margin_end = 10

            start_toStartOf = parent_id
            top_toTopOf = parent_id
            end_toStartOf = "button"

            text_size = 20
            textStyle = bold
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(Attr.getPrimaryTextColor(context))
        }
        RewardView(context).apply {
            rewardView = this

            start_toStartOf = parent_id
            top_toBottomOf = "title"
            end_toStartOf = "button"
        }.also {
            addView(it)
        }
        Button {
            button = this
            layout_id = "button"
            layout_width = wrap_content
            layout_height = 40

            margin_start = 10
            margin_end = 10
            margin_bottom = 10
            padding = 2

            end_toEndOf = parent_id
            bottom_toBottomOf = parent_id

            setTextColor(Attr.getPrimaryTextColor(context))
            backgroundTintList = ColorStateList.valueOf(Attr.getPrimaryColor(context))
        }
    }
}