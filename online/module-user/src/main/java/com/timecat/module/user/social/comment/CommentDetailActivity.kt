package com.timecat.module.user.social.comment

import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.base.GO
import com.timecat.module.user.social.share.showShare
import com.timecat.module.user.view.block.CommentView
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
        footer.response.setShakelessClickListener {
            GO.replyComment(block)
        }
        card.bindBlock(this, block)
        card.share.onShare = {
            showShare(supportFragmentManager, block)
        }
    }

    override fun setupCollapse() {
        toolbar.setTitle("")
        userHerf.title.text = title()
        setupCollapse {  card.userHead.height }
    }

    override fun fetch() {
        fetch(blockId)
    }
}

