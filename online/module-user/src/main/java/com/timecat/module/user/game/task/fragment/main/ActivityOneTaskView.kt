package com.timecat.module.user.game.task.fragment.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.view.TaskCard

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 用户的卡片视图
 * @usage null
 */
class ActivityOneTaskView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    lateinit var coverView: ImageView
    lateinit var taskCard: TaskCard

    var cover: String = ""
        set(value) {
            IconLoader.loadIcon(context, coverView, value)
            field = value
        }
    var onClick: (View) -> Unit = {}
        set(value) {
            coverView.setShakelessClickListener(onClick = value)
        }

    init {
        layout_width = match_parent
        layout_height = wrap_content

        ImageView {
            coverView = this
            layout_id = "cover"
            layout_width = match_parent
            layout_height = match_parent
        }
        TaskCard(context).apply {
            taskCard = this
            layout_id = "task"
            layout_width = 250.dp
            layout_height = 100.dp
            margin_end = 50.dp
            margin_bottom = 150.dp

            end_toEndOf = parent_id
            bottom_toBottomOf = parent_id
        }.also {
            addView(it)
        }
    }
}