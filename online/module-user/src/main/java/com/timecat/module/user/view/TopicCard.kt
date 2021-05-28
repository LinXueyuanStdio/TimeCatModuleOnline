package com.timecat.module.user.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.chip.Chip
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 话题的卡片视图
 * @usage null
 */
class TopicCard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var iconView: ImageView
    lateinit var titleView: TextView
    lateinit var descView: TextView
    lateinit var button: Button
    lateinit var placeholder: View

    var title: String = ""
        set(value) {
            titleView.text = value
            field = value
        }
    var desc: String = ""
        set(value) {
            descView.text = value
            field = value
        }
    var icon: String = ""
        set(value) {
            IconLoader.loadIcon(context, iconView, value)
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
        padding = 10

        View {
            placeholder = this
            layout_id = "placeholder"
            layout_width = 0
            layout_height = wrap_content

            start_toStartOf = parent_id
            top_toTopOf = parent_id
            end_toEndOf = parent_id
        }
        ImageView {
            iconView = this
            layout_id = "icon"
            layout_width = 100
            layout_height = 100

            start_toStartOf = parent_id
            top_toBottomOf = "placeholder"
            bottom_toBottomOf = parent_id

            src = R.drawable.ic_launcher
        }
        TextView {
            titleView = this
            layout_id = "title"
            layout_width = 0
            layout_height = wrap_content

            margin_start = 10

            start_toEndOf = "icon"
            top_toBottomOf = "placeholder"
            end_toEndOf = parent_id

            text_size = 20
            textStyle = bold
            setTextColor(Attr.getPrimaryTextColor(context))
        }
        TextView {
            descView = this
            layout_id = "desc"
            layout_width = 0
            layout_height = wrap_content

            margin_start = 10

            start_toEndOf = "icon"
            top_toBottomOf = "title"
            bottom_toTopOf = "button"
            end_toEndOf = parent_id

            text_size = 12
            setTextColor(Attr.getSecondaryTextColor(context))
        }
        Chip(context).apply {
            button = this
            layout_id = "button"
            layout_width = wrap_content
            layout_height = wrap_content

            margin_start = 10
            padding = 2

            start_toEndOf = "icon"
            bottom_toBottomOf = parent_id

            setTextColor(Attr.getPrimaryTextColor(context))
            setChipIconResource(R.drawable.ic_eye)
            chipIconTint = ColorStateList.valueOf(Attr.getIconColor(context))
        }.also {
            addView(it)
        }
    }

    fun setPlaceholderHeight(height: Int) {
        placeholder.updateLayoutParams<LayoutParams> {
            this.height = height
        }
    }
}