package com.timecat.module.user.game.cube

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.data.block.ForumBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseDetailCollapseActivity
import com.timecat.module.user.game.cube.fragment.*
import com.timecat.module.user.game.cube.vm.CubeViewModel
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.MomentListFragment
import com.timecat.module.user.view.TopicCard
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage 方块相关的评论、帖子、动态等等，不包括方块的养成方面
 * 如果要访问方块的养成，可以访问 AllCubeActivity
 * 这个页面一般用来展示用户未拥有的那些方块的详情
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeActivity)
class CubeActivity : BaseDetailCollapseActivity() {

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String

    lateinit var viewModel: CubeViewModel
    lateinit var card: TopicCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(CubeViewModel::class.java)
        viewModel.cube.observe(this, {
            it?.let { loadDetail(it) }
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

    private fun loadDetail(block: Block) {
        // 1. 加载头部卡片
        val headerBlock = ForumBlock.fromJson(block.structure)
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
            return 4
        }

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