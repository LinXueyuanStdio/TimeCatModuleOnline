package com.timecat.module.user.game.task.fragment

import android.view.View
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginMainFragment
import com.timecat.page.base.view.BlurringToolbar

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
abstract class BaseActivityFragment : BaseLoginMainFragment() {

    override fun layout(): Int = R.layout.user_fragment_game_activity_main

    override fun bindView(view: View) {
        super.bindView(view)
        val toolbar: BlurringToolbar = view.findViewById(R.id.toolbar)
        val background: View = view.findViewById(R.id.background)
        toolbar.setBlurredView(background)
        toolbar.setPaddingStatusBar(_mActivity)
    }
}