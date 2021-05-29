package com.timecat.module.user.game.task.fragment

import android.view.View
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
abstract class BaseActivityTabsFragment : BaseActivityFragment() {
    override fun layout(): Int = R.layout.user_fragment_game_activity_card

    lateinit var viewPager: ViewPager
    lateinit var tabs: TabLayout
    override fun bindView(view: View) {
        super.bindView(view)
        viewPager = view.findViewById(R.id.vp)
        tabs = view.findViewById(R.id.tabs)
    }

    protected fun setupViewPager(adapter: FragmentStatePagerAdapter) {
        viewPager.adapter = adapter
        viewPager.currentItem = 0
        tabs.setupWithViewPager(viewPager)
    }

    abstract fun getAdapter(owns: List<OwnActivity>): FragmentStatePagerAdapter

}