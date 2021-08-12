package com.timecat.module.user.base.login

import android.view.View
import android.widget.LinearLayout
import com.timecat.module.user.R
import com.timecat.page.base.base.simple.BaseSimpleFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseScrollContainerFragment : BaseSimpleFragment() {
    override fun layout(): Int = R.layout.base_user_scroll_container

    lateinit var container: LinearLayout
    override fun bindView(view: View) {
        super.bindView(view)
        container = view.findViewById(R.id.container)
    }

}