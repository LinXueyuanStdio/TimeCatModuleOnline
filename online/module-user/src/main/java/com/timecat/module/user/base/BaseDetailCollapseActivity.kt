package com.timecat.module.user.base

import android.util.TypedValue
import android.view.View
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.dp
import com.timecat.layout.ui.utils.ScreenUtil
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginDetailActivity
import kotlin.math.abs

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description AppBarLayout + CollapsingToolbarLayout + Toolbar
 * @usage 复杂的 detail 页面
 */
abstract class BaseDetailCollapseActivity : BaseLoginDetailActivity() {

    var titleString = ""
    var primaryColor: Int = 0
    var backgroundDarkestColor: Int = 0
    override fun title(): String = ""

    override fun bindView() {
        super.bindView()
        primaryColor = Attr.getPrimaryColor(this)
        backgroundDarkestColor = Attr.getBackgroundDarkestColor(this)
    }

    override fun setUpToolbar() {
        setSupportToolbar()
        setPaddingStatusBar()
        setBaseToolbar()
    }

    override fun initViewAfterLogin() {
    }

    protected fun setupCollapse() {
        appBarLayout.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED
                        //修改状态标记为展开
                        toolbar.setTitle("")
                    }
                } else if (appBarLayout != null && abs(verticalOffset) >= appBarLayout.totalScrollRange) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        toolbar.setTitle(titleString)
                    }
                } else {
                    toolbar.setTitle("")
                }
            }
        })
    }

    protected fun setupViewPager() {
        viewPager.adapter = getAdapter()
        viewPager.currentItem = 1
        tabs.setupWithViewPager(viewPager)
    }

    protected fun getStatusBarHeightPlusToolbarHeight(): Int {
        val tv = TypedValue()
        val statusbarHeight = ScreenUtil.getStatusBarHeight(this)
        if (theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
            val actionBarHeight: Int = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            return statusbarHeight + actionBarHeight
        } else {
            return statusbarHeight + 56.dp
        }
    }

    protected fun setupHeaderCard(card: View) {
        collapseContainer.addView(card, 0)
        card.updateLayoutParams<CollapsingToolbarLayout.LayoutParams> {
            collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PARALLAX
        }
    }

    abstract fun fetch()
    abstract fun getAdapter(): FragmentStatePagerAdapter
}