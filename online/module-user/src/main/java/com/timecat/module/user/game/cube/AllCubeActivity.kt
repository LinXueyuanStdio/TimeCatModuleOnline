package com.timecat.module.user.game.cube

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import cn.bmob.v3.BmobQuery
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.data.bmob.ext.bmob.requestOwnCube
import com.timecat.data.bmob.ext.net.allOwnCube
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseDetailCollapseActivity
import com.timecat.module.user.game.cube.fragment.*
import com.timecat.module.user.game.cube.vm.CubeViewModel
import com.timecat.module.user.view.TopicCard
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllCubeActivity)
class AllCubeActivity : BaseDetailCollapseActivity() {
    lateinit var viewModel: CubeViewModel
    lateinit var card: TopicCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(CubeViewModel::class.java)
        viewModel.cube.observe(this, {
            it?.let {
                loadDetail(it)
            }
        })
        card = TopicCard(this)
        card.placeholder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = getStatusBarHeightPlusToolbarHeight()
        }
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    private fun loadDetail(ownCube: OwnCube) {
        val cube = ownCube.cube
        // 1. 加载头部卡片
        titleString = cube.title
        card.apply {
            title = cube.title
            desc = cube.content
            icon = "R.drawable.ic_launcher"
        }
    }

    override fun fetch() {
        requestOwnCube {
            query = I().allOwnCube()
            onSuccess = {
                viewModel.cube.postValue(it)
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
        override fun getCount(): Int {
            return 7
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CubeDetailFragment()
                1 -> CubeAttrFragment()
                2 -> CubeSkillFragment()
                3 -> CubeEquipFragment()
                4 -> CommentListFragment()
                5 -> PostListFragment()
                6 -> MomentListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "属性"
                2 -> "技能"
                3 -> "装备"
                4 -> "讨论"
                5 -> "帖子"
                6 -> "动态"
                else -> super.getPageTitle(position)
            }
        }
    }
}