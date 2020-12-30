package com.timecat.module.user.social.app

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.view.AppView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.header_app_layout_detail.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 应用详情，包括评论，预览
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.APP_DETAIL_AppDetailActivity)
class AppDetailActivity : BaseBlockDetailActivity() {
    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var headerView: AppView

    override fun title(): String = "应用"
    override fun getDetailBlockId(): String = blockId

    override fun getHeaderView(): View {
        headerView = AppView(this)
        headerView.preface.layoutManager = LinearLayoutManager(this)
        return headerView
    }

    override fun initBlockHeader(block: Block) {
        headerView.bindBlock(block) {
            toolbar.title = it.title
        }
    }

}

