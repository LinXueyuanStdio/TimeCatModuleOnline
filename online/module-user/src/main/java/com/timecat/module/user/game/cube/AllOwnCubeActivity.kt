package com.timecat.module.user.game.cube

import android.view.View
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
import com.timecat.layout.ui.standard.navi.BottomBarIvTextTab
import com.timecat.module.user.base.BaseNavCollapseActivity
import com.timecat.module.user.game.cube.fragment.*
import com.timecat.module.user.game.cube.fragment.detail.*
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
class AllOwnCubeActivity : BaseNavCollapseActivity() {
    lateinit var cubeViewModel: CubeViewModel
    lateinit var card: TopicCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewModel() {
        super.initViewModel()
        cubeViewModel = ViewModelProvider(this).get(CubeViewModel::class.java)
        cubeViewModel.ownCube.observe(this, {
            it?.let {
                loadDetail(it)
                onContentLoaded()
            }
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
    }

    override fun providerHeaderCard(): View {
        card = TopicCard(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        return card
    }

    override fun onTabSelected(position: Int, prePosition: Int) {
        super.onTabSelected(position, prePosition)
        val cube = cubeViewModel.ownCubes.value!![position]
        cubeViewModel.loadCube(cube)
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
        cubeViewModel attach requestOwnCube {
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
        override fun getCount(): Int = 8

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CubeAttrFragment()
                1 -> CubeRolesFragment()
                2 -> CubeSettingFragment()
                3 -> CubeSkillFragment()
                4 -> CubeEquipFragment()
                5 -> CommentListFragment()
                6 -> MomentListFragment()
                7 -> LikeListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "属性"
                1 -> "权柄"
                2 -> "设置"
                3 -> "技能"
                4 -> "装备"
                5 -> "讨论"
                6 -> "动态"
                7 -> "赞"
                else -> super.getPageTitle(position)
            }
        }
    }
}