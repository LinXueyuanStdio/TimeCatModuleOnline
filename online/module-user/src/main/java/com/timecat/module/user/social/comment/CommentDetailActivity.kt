package com.timecat.module.user.social.comment

import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.AppBarLayout
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.bind
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.social.app.fragment.CommentListFragment
import com.timecat.module.user.social.common.BlockViewModel
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.RelayListFragment
import com.timecat.module.user.view.CommentFooterView
import com.timecat.module.user.view.CommentView
import com.timecat.module.user.view.UserHerfView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 应用详情，包括评论，预览
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CommentDetailActivity)
class CommentDetailActivity : BaseBlockDetailActivity() {
    override fun title(): String = "评论"

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var card: CommentView

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()

        card = CommentView(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        card.bindBlock(this, block)
    }

    override fun fetch() {
        viewModel attach requestOneBlock {
            query = oneBlockOf(blockId)
            onSuccess = {
                viewModel.block.postValue(it)
            }
            onError = {
                mStatefulLayout?.showError("出错啦") {
                    fetch()
                }
            }
        }
    }
}

