package com.timecat.module.user.game.task

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.data.bmob.ext.bmob.requestOwnActivity
import com.timecat.data.bmob.ext.net.allOwnActivity
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.standard.navi.BottomBar
import com.timecat.layout.ui.standard.navi.BottomBarIvTextTab
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseDetailCollapseActivity
import com.timecat.module.user.game.task.vm.TaskViewModel
import com.timecat.module.user.game.task.fragment.*
import com.timecat.module.user.game.task.fragment.TaskDetailFragment
import com.timecat.module.user.view.TopicCard
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllTaskActivity)
class AllTaskActivity : BaseDetailCollapseActivity() {
    lateinit var viewModel: TaskViewModel
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
        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        viewModel.ownActivity.observe(this, {
            it?.let { loadDetail(it) }
        })
        viewModel.activities.observe(this, {
            mBottomBar.removeAllViews()
            mBottomBar.addHorizonSV()
            it.forEach {
                val icon = it.activity.structure
                val title = it.activity.title
                val tab = BottomBarIvTextTab(this@AllTaskActivity, icon, title)
                mBottomBar.addItem(tab)
            }
            viewModel.ownActivity.postValue(it[0])
        })
        card = TopicCard(this)
        card.placeholder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = getStatusBarHeightPlusToolbarHeight()
        }
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()

        mBottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                LogUtil.sd("select $position, $prePosition")
                //选中时触发
                val cube = viewModel.activities.value!![position]
                viewModel.ownActivity.postValue(cube)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabLongSelected(position: Int) {}
            override fun onTabReselected(position: Int) {}
        })
        fetch()
    }

    private fun loadDetail(ownActivity: OwnActivity) {
        val task = ownActivity.activity
        // 1. 加载头部卡片
        titleString = task.title
        card.apply {
            title = task.title
            desc = task.content
            icon = "R.drawable.ic_launcher"
        }
    }

    override fun fetch() {
        requestOwnActivity {
            query = I().allOwnActivity()
            onSuccess = {
                viewModel.activities.postValue(it)
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
            return 4
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TaskDetailFragment()
                1 -> CommentListFragment()
                2 -> PostListFragment()
                3 -> MomentListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "讨论"
                2 -> "帖子"
                3 -> "动态"
                else -> super.getPageTitle(position)
            }
        }
    }
}