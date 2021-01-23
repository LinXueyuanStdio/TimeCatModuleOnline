package com.timecat.module.user.social.leaderboard

import android.view.Menu
import android.view.MenuItem
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.follow
import com.timecat.data.bmob.ext.net.allFollowBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.LeaderBoardBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseDetailCollapseActivity
import com.timecat.module.user.base.GO
import com.timecat.module.user.social.forum.fragment.CommentListFragment
import com.timecat.module.user.social.leaderboard.fragment.LeaderBoardDetailFragment
import com.timecat.module.user.social.leaderboard.fragment.LeaderBoardListFragment
import com.timecat.module.user.social.leaderboard.fragment.RecommendListFragment
import com.timecat.module.user.social.leaderboard.vm.LeaderBoardViewModel
import com.timecat.module.user.view.ForumCard
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/9
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_LeaderBoardDetailActivity)
class LeaderBoardCollapseActivity : BaseDetailCollapseActivity() {
    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var viewModel: LeaderBoardViewModel
    lateinit var card: ForumCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(LeaderBoardViewModel::class.java)
        viewModel.board.observe(this, {
            it?.let { loadDetail(it) }
        })
        card = ForumCard(this)
        card.placeholder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = getStatusBarHeightPlusToolbarHeight()
        }
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    private fun loadDetail(block: Block) {
        // 1. 加载头部卡片
        val headerBlock = LeaderBoardBlock.fromJson(block.structure)
        titleString = block.title
        card.apply {
            title = block.title
            desc = "点赞 ${block.likes}  讨论 ${block.comments}  分享 ${block.relays}"
            icon = headerBlock.header?.icon ?: "R.drawable.ic_launcher"
            setupFollowBlockButton(context, button, block)
        }
    }

    override fun fetch() {
        requestOneBlock {
            query = oneBlockOf(blockId)
            onSuccess = {
                viewModel.board.postValue(it)
            }
            onError = {
                mStatefulLayout?.showError("出错啦") {
                    fetch()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.leaderboard_recommend, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == android.R.id.home) {
            finish()
            return true
        }
        if (i == R.id.add) {
            viewModel.board.value?.let {
                GO.addRecommendFor(it)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
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
                0 -> LeaderBoardDetailFragment()
                1 -> LeaderBoardListFragment()
                2 -> RecommendListFragment()
                3 -> CommentListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "排行榜"
                2 -> "举荐"
                3 -> "讨论"
                else -> super.getPageTitle(position)
            }
        }
    }

}