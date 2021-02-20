package com.timecat.module.user.game.cube

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import cn.leancloud.AVQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.bmob.requestOwnCube
import com.timecat.data.bmob.ext.net.allOwnCube
import com.timecat.identity.data.block.IdentityBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.standard.navi.BottomBar
import com.timecat.layout.ui.standard.navi.BottomBarIvTextTab
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockCollapseActivity
import com.timecat.module.user.game.cube.fragment.*
import com.timecat.module.user.game.cube.vm.CubeViewModel
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.MomentListFragment
import com.timecat.module.user.view.TopicCard
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllOwnCubeActivity)
class AllOwnCubeActivity : BaseBlockCollapseActivity() {
    lateinit var cubeViewModel: CubeViewModel
    lateinit var card: TopicCard
    lateinit var mBottomBar: BottomBar
    override fun routerInject() = NAV.inject(this)
    override fun layout(): Int = R.layout.user_detail_collapse_viewpager_bottombar

    override fun bindView() {
        super.bindView()
        mBottomBar = findViewById(R.id.bottomBar)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        cubeViewModel = ViewModelProvider(this).get(CubeViewModel::class.java)
        cubeViewModel.ownCube.observe(this, {
            it?.let { loadDetail(it) }
        })
        cubeViewModel.cube.observe(this) {
            blockViewModel.block.postValue(it)
        }
        cubeViewModel.ownCubes.observe(this, {
            if (it.isEmpty()) {
                LogUtil.se("没有持有任何方块!")
                mStatefulLayout?.showEmpty("没有持有任何方块！")
                return@observe
            } else {
                mStatefulLayout?.showContent()
            }
            mBottomBar.removeAllViews()
            mBottomBar.addHorizonSV()
            it.forEach {
                val head = IdentityBlock.fromJson(it.cube.structure)
                val icon = head.header.avatar
                val title = it.cube.title
                val tab = BottomBarIvTextTab(this@AllOwnCubeActivity, icon, title)
                mBottomBar.addItem(tab)
            }
        })
        card = TopicCard(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        viewPager.currentItem = 0

        mBottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                LogUtil.sd("select $position, $prePosition")
                //选中时触发
                val cube = cubeViewModel.ownCubes.value!![position]
                cubeViewModel.ownCube.postValue(cube)
                cubeViewModel.cube.postValue(cube.cube)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabLongSelected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
        fetch()
    }

    private fun loadDetail(ownCube: OwnCube) {
        val cube = ownCube.cube
        val head = IdentityBlock.fromJson(cube.structure)
        // 1. 加载头部卡片
        titleString = cube.title
        card.apply {
            title = cube.title
            desc = cube.content
            icon = head.header.avatar
        }
    }

    override fun setupTabs(block: Block) {
        tabs.getTabAt(1)?.let {
            it.text = "评论${block.comments}"
        }
        tabs.getTabAt(2)?.let {
            it.text = "转发${block.relays}"
        }
        tabs.getTabAt(3)?.let {
            it.text = "赞${block.likes}"
        }
    }

    override fun fetch() {
        mStatefulLayout?.showLoading()
        requestOwnCube {
            query = I().allOwnCube().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onSuccess = {
                cubeViewModel.loadAllCube(it)
            }
            onEmpty = {
                cubeViewModel.loadAllCube(listOf())
            }
            onError = {
                mStatefulLayout?.showError("出错啦") {
                    fetch()
                }
            }
        }
    }

    override fun getAdapter(): FragmentStatePagerAdapter {
        return DetailAdapter(supportFragmentManager)
    }

    class DetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int = 4

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CubeDetailFragment()
                1 -> CommentListFragment()
                2 -> MomentListFragment()
                3 -> LikeListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "讨论"
                2 -> "动态"
                3 -> "赞"
                else -> super.getPageTitle(position)
            }
        }
    }
}