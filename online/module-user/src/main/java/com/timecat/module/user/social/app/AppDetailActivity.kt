package com.timecat.module.user.social.app

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.timecat.component.router.app.FallBackFragment
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.social.app.fragment.AppDetailFragment
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.MomentListFragment
import com.timecat.module.user.social.share.showShare
import com.timecat.module.user.view.AppView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 应用详情，包括评论，预览
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.APP_DETAIL_AppDetailActivity)
class AppDetailActivity : BaseBlockDetailActivity() {
    override fun title(): String = "动态"

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var card: AppView

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()

        card = AppView(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        card.bindBlock(block) {
            userHerf.title.text = it.title
        }
        card.share.onShare = {
            showShare(supportFragmentManager, block)
        }
    }

    override fun setupCollapse() {
        toolbar.setTitle("")
        userHerf.title.text = title()
        setupCollapse { card.userHead.height }
    }

    override fun fetch() {
        blockViewModel attach requestOneBlock {
            query = oneBlockOf(blockId)
            onSuccess = {
                blockViewModel.block.postValue(it)
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
                0 -> AppDetailFragment()
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