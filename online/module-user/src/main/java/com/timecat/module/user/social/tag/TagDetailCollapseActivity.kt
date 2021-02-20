package com.timecat.module.user.social.tag

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.TagBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.MomentListFragment
import com.timecat.module.user.social.common.PostListFragment
import com.timecat.module.user.social.tag.fragment.TagDetailFragment
import com.timecat.module.user.view.TagCard
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description 标签详情
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_TagDetailActivity)
class TagDetailCollapseActivity : BaseBlockDetailActivity() {
    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var card: TagCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        card = TagCard(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    override fun loadDetail(block: Block) {
        // 1. 加载头部卡片
        val headerBlock = TagBlock.fromJson(block.structure)
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

    override fun getAdapter(): FragmentStatePagerAdapter {
        return DetailAdapter(supportFragmentManager)
    }

    class DetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 4
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> TagDetailFragment()
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