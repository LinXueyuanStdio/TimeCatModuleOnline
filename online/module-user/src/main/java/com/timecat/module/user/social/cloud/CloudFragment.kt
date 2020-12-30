package com.timecat.module.user.social.cloud

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.github.florent37.viewtooltip.ViewTooltip
import com.google.android.material.tabs.TabLayout
import com.timecat.extend.arms.BaseApplication
import com.timecat.page.base.base.OnFragmentOpenDrawerListener
import com.timecat.page.base.base.rxevent.BaseEventSupportFragment
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.utils.ScreenUtil
import com.timecat.layout.ui.utils.ToolbarUtils
import com.timecat.module.user.R
import com.timecat.module.user.adapter.ChannelPagerAdapter
import com.timecat.module.user.social.cloud.channel.ChannelListEvent
import com.timecat.module.user.social.cloud.channel.ChannelManager
import com.timecat.module.user.social.cloud.channel.ChannelPagerModel
import com.xiaojinzi.component.anno.FragmentAnno
import org.greenrobot.eventbus.Subscribe

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description 主fragment，一个fragment容器
 * @usage null
 */
@FragmentAnno(RouterHub.USER_CloudFragment)
class CloudFragment : BaseEventSupportFragment() {
    private var mOpenDrawerListener: OnFragmentOpenDrawerListener? = null
    private lateinit var tabs: TabLayout
    private lateinit var pager: ViewPager
    private lateinit var config: ImageView
    private lateinit var toolbar: Toolbar
    private lateinit var tint_statusbar: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = getThemedView(R.layout.user_fragment_cloud, inflater, container)
        tint_statusbar= view.findViewById(R.id.tint_statusbar)
        ToolbarUtils.initTintStatusBar(tint_statusbar, _mActivity)
        tabs = view.findViewById(R.id.tabs)
        pager = view.findViewById(R.id.viewPager)
        config = view.findViewById(R.id.config)
        toolbar = view.findViewById(R.id.toolbar)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectTab(position, true)
            }
        })
        tabs.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.tag == null) {
                    val position = tab.position
                    selectTab(position, false)
                }
                tab.tag = null
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {
                selectTab(tab.position, false)
            }
        })
        ToolbarUtils.initToolbarNav(toolbar, true) {
            mOpenDrawerListener?.onOpenDrawer(null)
        }
        config.setOnClickListener { NAV.go(context, RouterHub.USER_CustomChannelActivity) }
        val adapter = ChannelPagerAdapter(
            childFragmentManager,
            ChannelPagerModel.buildForAll(_mActivity, ChannelManager.myChannels)
        )
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
        tabs.tabGravity = TabLayout.GRAVITY_FILL
        dynamicSetTabLayoutMode(tabs)
        if (savedInstanceState == null) {
            tabs.getTabAt(0)?.select()
        }
    }

    private fun showToolTip(v: View, s: String): Boolean {
        ViewTooltip.on(v)
            .autoHide(true, 3000)
            .clickToHide(true)
            .position(ViewTooltip.Position.BOTTOM)
            .text(s)
            .corner(10)
            .arrowWidth(15)
            .arrowHeight(15)
            .show()
        return true
    }

    //region toolbar
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentOpenDrawerListener) {
            mOpenDrawerListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mOpenDrawerListener = null
    }

    //endregion
    //region EventBus
    override fun needEventBus(): Boolean {
        return true
    }

    @Subscribe
    fun onChannelChanged(e: ChannelListEvent) {
        val adapter = ChannelPagerAdapter(
            childFragmentManager,
            ChannelPagerModel.buildForAll(_mActivity, e.channelList)
        )
        pager.adapter = adapter
        tabs.setTabsFromPagerAdapter(adapter)
        dynamicSetTabLayoutMode(tabs)
    }

    //endregion
    private fun selectTab(position: Int, fromViewPager: Boolean) {
        if (!fromViewPager) {
            pager.currentItem = position
        } else {
            val tab = tabs.getTabAt(position)
            if (tab != null) {
                tab.tag = "hello"
                if (!tab.isSelected) {
                    tab.select()
                }
            }
        }
    }

    //endregion
    //region static
    private fun dynamicSetTabLayoutMode(tabLayout: TabLayout) {
        val tabWidth = calculateTabWidth(tabLayout)
        val screenWidth =
            ScreenUtil.getScreenWidth(BaseApplication.getContext())
        if (tabWidth <= screenWidth) {
            tabLayout.tabMode = TabLayout.MODE_FIXED
        } else {
            tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        }
    } //endregion

    companion object {
        private fun calculateTabWidth(tabLayout: TabLayout?): Int {
            var tabWidth = 0
            for (i in 0 until tabLayout!!.childCount) {
                val view = tabLayout.getChildAt(i)
                view.measure(0, 0) // 通知父view测量，以便于能够保证获取到宽高
                tabWidth += view.measuredWidth
            }
            return tabWidth
        }
    }

}