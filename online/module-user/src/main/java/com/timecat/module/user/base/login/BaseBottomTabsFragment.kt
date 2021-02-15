package com.timecat.module.user.base.login

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.gturedi.views.StatefulLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.page.base.base.simple.BaseSimpleFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseBottomTabsFragment : BaseSimpleFragment() {
    override fun layout(): Int = R.layout.base_user_viwpager_bottom_tabs

    lateinit var viewPager: ViewPager
    lateinit var tabs: TabLayout
    override fun bindView(view: View) {
        super.bindView(view)
        viewPager = view.findViewById(R.id.vp)
        tabs = view.findViewById(R.id.tabs)
    }

    protected fun setupViewPager() {
        viewPager.adapter = getAdapter()
        viewPager.currentItem = 0
        tabs.setupWithViewPager(viewPager)
    }
    abstract fun getAdapter(): FragmentStatePagerAdapter

}