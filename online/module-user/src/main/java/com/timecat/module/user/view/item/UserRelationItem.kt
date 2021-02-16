package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.setPadding
import com.afollestad.materialdialogs.utils.MDUtil.dimenPx
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class UserRelationItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    fun buildTextView(context: Context): TextView {
        return TextView {
            layout_width = wrap_content
            layout_height = match_parent
            layout_gravity = gravity_center
            weight = 1f
            gravity = Gravity.CENTER
            setPadding(dimenPx(R.dimen.margin_small))
            setTextColor(Attr.getPrimaryTextColor(context))
        }
    }

    var fansView: TextView
    var followsView: TextView
    var starView: TextView
    var creationView: TextView

    init {
        layout_width = match_parent
        layout_height = wrap_content
        orientation = HORIZONTAL
        fansView = buildTextView(context)
        followsView = buildTextView(context)
        starView = buildTextView(context)
        creationView = buildTextView(context)
    }

    var fans: Int = 0
        set(value) {
            fansView.setText("$value\n粉丝")
            field = value
        }
    var follows: Int = 0
        set(value) {
            followsView.setText("$value\n关注")
            field = value
        }
    var star: Int = 0
        set(value) {
            starView.setText("$value\n获赞")
            field = value
        }
    var creation: Int = 0
        set(value) {
            creationView.setText("$value\n造物")
            field = value
        }
}