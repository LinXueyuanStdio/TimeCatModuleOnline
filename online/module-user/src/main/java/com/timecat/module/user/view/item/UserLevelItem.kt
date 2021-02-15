package com.timecat.module.user.view.item

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.ColorUtils
import com.timecat.module.user.game.core.Level

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
open class UserLevelItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var levelTv: TextView
    var maxLevelTv: TextView
    var expTv: TextView
    var expBar: RoundCornerProgressBar

    init {
        padding = 10
        val accentColor = Attr.getAccentColor(context)
        val alphaAccentColor = ColorUtils.setAlpha(accentColor, 0.5f)
        levelTv = TextView {
            layout_id = "level"
            layout_width = wrap_content
            layout_height = wrap_content

            padding = 3
            start_toStartOf = parent_id
            bottom_toTopOf = "exp_bar"

            text_size = 24
            textStyle = bold
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(Attr.getPrimaryTextColor(context))
        }
        maxLevelTv = TextView {
            layout_id = "maxLevel"
            layout_width = wrap_content
            layout_height = wrap_content

            padding = 3
            margin_start = 3
            start_toEndOf = "level"
            bottom_toTopOf = "exp_bar"

            text_size = 24
            textStyle = bold
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(Attr.getPrimaryTextColor(context))
            alpha = 0.5f
        }
        expTv = TextView {
            layout_id = "exp"
            layout_width = wrap_content
            layout_height = wrap_content

            padding = 3
            end_toEndOf = parent_id
            bottom_toTopOf = "exp_bar"

            text_size = 16
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            setTextColor(accentColor)
        }
        expBar = RoundCornerProgressBar(context, null).apply {
            layout_id = "exp_bar"
            layout_width = match_parent
            layout_height = 10
            bottom_toBottomOf = parent_id
            start_toStartOf = parent_id
            end_toEndOf = parent_id

            progressColors = intArrayOf(ColorUtils.randomColor(), accentColor)
            secondaryProgressColors = intArrayOf(ColorUtils.randomColor(), alphaAccentColor)
            enableAnimation()
        }.also {
            addView(it)
        }
    }

    var maxLevel: Int = 20
        set(value) {
            maxLevelTv.text = "/ $value"
            field = value
        }
    var exp: Long = 1
        set(value) {
            val (level, exp) = getLevel(value)
            val expLimit = expLimit(level)
            levelTv.text = "等级 $level"
            expTv.text = "$exp / $expLimit"
            expBar.progress = exp.toFloat()
            expBar.max = expLimit.toFloat()
            field = value
        }
    var fakeExp: Long = 1
        set(value) {
            val progress = value.coerceIn(exp, expBar.max.toLong())
            expBar.secondaryProgress = progress.toFloat()
            field = progress
        }

    open var getLevel: (Long) -> Pair<Int, Long> = { Level.getLevel(it) }
    open var expLimit: (Int) -> Long = { Level.expLimit(it) }
}