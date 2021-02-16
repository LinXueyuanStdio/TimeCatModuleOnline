package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.layout_height
import com.timecat.layout.ui.layout.layout_width
import com.timecat.layout.ui.layout.match_parent
import com.timecat.module.user.R
import com.timecat.module.user.view.GitHubContributionsView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/16
 * @description null
 * @usage null
 */
class ContributionItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : GitHubContributionsView(context, attrs, defStyleAttr) {
    init {
        layout_width = match_parent
        layout_height = 120
        setBaseColor(Attr.getPrimaryColor(context))
        setBaseEmptyColor(Attr.getColor(context, R.color.material_grey_400))
        setTextColor(Attr.getPrimaryTextColor(context))
        setLastWeeks(53)
        displayMonth(true)
    }
}