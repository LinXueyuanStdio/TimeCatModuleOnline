package com.timecat.module.user.base

import android.view.View
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.standard.navi.BottomBar
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage null
 */
abstract class BaseNavCollapseActivity : BaseBlockCollapseActivity(), BottomBar.OnTabSelectedListener {
    lateinit var mBottomBar: BottomBar
    override fun routerInject() = NAV.inject(this)
    override fun layout(): Int = R.layout.user_detail_collapse_viewpager_bottombar

    override fun bindView() {
        super.bindView()
        mBottomBar = findViewById(R.id.bottomBar)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        setupView()
        fetch()
    }

    protected fun setupView() {
        setupHeaderCard(providerHeaderCard())
        setupCollapse()
        setupViewPager()
        viewPager.currentItem = initViewPagerIndex()

        mBottomBar.setOnTabSelectedListener(this)
    }

    protected abstract fun providerHeaderCard(): View
    protected fun initViewPagerIndex(): Int = 0

    //region BottomBar.OnTabSelectedListener
    override fun onTabSelected(position: Int, prePosition: Int) {
        LogUtil.sd("select $position, $prePosition")
        //选中时触发
    }

    override fun onTabUnselected(position: Int) {}
    override fun onTabLongSelected(position: Int) {}
    override fun onTabReselected(position: Int) {}
    //endregion
}
