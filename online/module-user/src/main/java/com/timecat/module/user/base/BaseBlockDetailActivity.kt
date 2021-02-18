package com.timecat.module.user.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.R
import com.timecat.module.user.social.app.fragment.CommentListFragment
import com.timecat.module.user.social.common.BlockViewModel
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.RelayListFragment
import com.timecat.module.user.view.CommentFooterView
import com.timecat.module.user.view.UserHerfView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-24
 * @description 动态、评论
 * @usage null
 */
abstract class BaseBlockDetailActivity : BaseDetailCollapseActivity() {
    override fun routerInject() = NAV.inject(this)

    lateinit var viewModel: BlockViewModel
    lateinit var userHerf: UserHerfView
    lateinit var footer: CommentFooterView
    override fun layout(): Int = R.layout.user_detail_collapse_viewpager_footer
    override fun bindView() {
        super.bindView()
        userHerf = findViewById(R.id.user_herf)
        footer = findViewById(R.id.footer)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(BlockViewModel::class.java)
        viewModel.block.observe(this, {
            it?.let { loadDetail(it) }
        })
    }

    protected open fun loadDetail(block: Block) {
        titleString = block.title
        userHerf.bindBlock(block.user)
        footer.bindBlock(this, block)
    }

    override fun setupCollapse() {
        toolbar.setTitle("")
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            var alpha = Math.abs(i).toFloat() / appBarLayout.totalScrollRange
            alpha = Math.min(1f, alpha)
            userHerf.setAlpha(alpha)
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
                0 -> RelayListFragment()
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