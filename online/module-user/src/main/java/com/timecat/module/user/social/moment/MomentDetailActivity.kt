package com.timecat.module.user.social.moment

import android.view.View
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.view.MomentView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 应用详情，包括评论，预览
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_MomentDetailActivity)
class MomentDetailActivity : BaseBlockDetailActivity() {
    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var headerView: MomentView

    override fun title(): String = "动态"
    override fun getDetailBlockId(): String = blockId

    override fun getHeaderView(): View {
        headerView = MomentView(this)
        return headerView
    }

    override fun initBlockHeader(block: Block) {
        headerView.bindBlock(this, block)
    }
}

