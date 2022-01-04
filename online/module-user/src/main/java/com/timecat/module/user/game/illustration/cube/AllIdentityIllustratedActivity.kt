package com.timecat.module.user.game.illustration.cube

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
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allIdentity
import com.timecat.identity.data.block.IdentityBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.standard.navi.BottomBarIvTextTab
import com.timecat.module.user.base.BaseNavCollapseActivity
import com.timecat.module.user.game.cube.fragment.*
import com.timecat.module.user.game.cube.vm.IdentityViewModel
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
@RouterAnno(hostAndPath = RouterHub.USER_AllIdentityIllustratedActivity)
class AllIdentityIllustratedActivity : BaseNavCollapseActivity() {
    lateinit var cubeViewModel: IdentityViewModel
    lateinit var card: TopicCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewModel() {
        super.initViewModel()
        cubeViewModel = ViewModelProvider(this).get(IdentityViewModel::class.java)
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
                val head = IdentityBlock.fromJson(it.structure)
                val icon = head.header.avatar
                val title = it.title
                val tab = BottomBarIvTextTab(this@AllIdentityIllustratedActivity, icon, title)
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

    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        val cube = block
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
        tabs.getTabAt(4)?.let {
            it.text = "评论${block.comments}"
        }
        tabs.getTabAt(5)?.let {
            it.text = "转发${block.relays}"
        }
        tabs.getTabAt(6)?.let {
            it.text = "赞${block.likes}"
        }
    }

    override fun fetch() {
        cubeViewModel attach requestBlock {
            query = I().allIdentity().apply {
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
        override fun getCount(): Int = 7

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> IdentityAttrFragment()
                1 -> CubeRolesFragment()
                2 -> CubeSkillFragment()
                3 -> CubeDataFragment()
                4 -> CommentListFragment()
                5 -> MomentListFragment()
                6 -> LikeListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "属性"
                1 -> "权柄"
                2 -> "技能"
                3 -> "资料"
                4 -> "讨论"
                5 -> "动态"
                6 -> "赞"
                else -> super.getPageTitle(position)
            }
        }
    }
}