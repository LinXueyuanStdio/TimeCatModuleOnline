package com.timecat.module.user.social.topic

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.timecat.component.router.app.FallBackFragment
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.data.block.ForumBlock
import com.timecat.identity.data.block.TopicBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.MomentListFragment
import com.timecat.module.user.social.topic.fragment.TopicDetailFragment
import com.timecat.module.user.view.TopicCard
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description 话题详情
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_TopicDetailActivity)
class TopicDetailCollapseActivity : BaseBlockDetailActivity() {
    override fun title(): String = "话题"

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var card: TopicCard

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()

        card = TopicCard(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        val headerBlock = TopicBlock.fromJson(block.structure)
        titleString = block.title
        card.apply {
            title = block.title
            desc = "点赞 ${block.likes}  讨论 ${block.comments}  分享 ${block.relays}"
            icon = headerBlock.header?.icon ?: "R.drawable.ic_launcher"
            setupFollowBlockButton(context, button, block)
        }
    }

    override fun fetch() {
        fetch(blockId)
    }

    override fun setupTabs(block:Block) {
        tabs.getTabAt(1)?.let {
            it.text = "评论${block.comments}"
        }
        tabs.getTabAt(2)?.let {
            it.text = "赞${block.likes}"
        }
        tabs.getTabAt(3)?.let {
            it.text = "转发${block.relays}"
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
                0 -> TopicDetailFragment()
                1 -> CommentListFragment()
                2 -> LikeListFragment()
                3 -> MomentListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "讨论"
                2 -> "赞"
                3 -> "转发"
                else -> super.getPageTitle(position)
            }
        }
    }
}