package com.timecat.module.user.social.recommend

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.view.block.CommentView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 推荐详情，包括评论，预览
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_RecommendDetailActivity)
class RecommendDetailActivity : BaseBlockDetailActivity() {
    override fun title(): String = "评分"

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var card: CommentView

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()

        card = CommentView(this)
        card.placeholder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = getStatusBarHeightPlusToolbarHeight()
        }
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

