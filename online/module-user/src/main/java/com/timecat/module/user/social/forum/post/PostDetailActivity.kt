package com.timecat.module.user.social.forum.post

import com.google.android.material.appbar.AppBarLayout
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.social.share.showShare
import com.timecat.module.user.view.block.PostView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 帖子详情，包括评论，预览
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_PostDetailActivity)
class PostDetailActivity : BaseBlockDetailActivity() {
    override fun title(): String = "帖子"

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var card: PostView

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()

        card = PostView(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        card.bindBlock(this, block)
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

