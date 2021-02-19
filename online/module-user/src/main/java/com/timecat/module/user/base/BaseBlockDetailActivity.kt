package com.timecat.module.user.base

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.social.common.BlockViewModel
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.MomentListFragment
import com.timecat.module.user.social.share.showMore
import com.timecat.module.user.view.CommentFooterView
import com.timecat.module.user.view.ToolbarHeadView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-24
 * @description 动态、评论
 * @usage null
 */
abstract class BaseBlockDetailActivity : BaseDetailCollapseActivity() {
    override fun routerInject() = NAV.inject(this)

    lateinit var blockViewModel: BlockViewModel
    lateinit var userHerf: ToolbarHeadView
    lateinit var footer: CommentFooterView
    lateinit var more: View
    override fun layout(): Int = R.layout.user_detail_collapse_viewpager_footer
    override fun bindView() {
        super.bindView()
        userHerf = findViewById(R.id.user_herf)
        footer = findViewById(R.id.footer)
        more = findViewById(R.id.more_dialog)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        blockViewModel = ViewModelProvider(this).get(BlockViewModel::class.java)
        blockViewModel.block.observe(this, {
            it?.let { loadDetail(it) }
        })
    }

    protected open fun loadDetail(block: Block) {
        titleString = block.title
        userHerf.head.bindBlock(block.user)
        footer.bindBlock(this, block)
        footer.comment.setShakelessClickListener {
            collapseContainer.setScrimsShown(true, true)
        }
        more.setShakelessClickListener {
            showMore(supportFragmentManager, block)
        }
    }

    override fun setupCollapse() {
        setupCollapse { appBarLayout.totalScrollRange }
    }

    fun setupCollapse(visHeight: () -> Int) {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
//            LogUtil.se("${i} / ${visHeight()}")
            //滑动超过 head的可见高度，则显示 userHerf
            userHerf.displayedChild = if (Math.abs(i) >= visHeight()) 1 else 0
        })
    }

    override fun getAdapter(): FragmentStatePagerAdapter {
        return DetailAdapter(supportFragmentManager)
    }

    class DetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MomentListFragment()
                1 -> CommentListFragment()
                2 -> LikeListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "转发"
                1 -> "讨论"
                2 -> "点赞"
                else -> super.getPageTitle(position)
            }
        }
    }

}