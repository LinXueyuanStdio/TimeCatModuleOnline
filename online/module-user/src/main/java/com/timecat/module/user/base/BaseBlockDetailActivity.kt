package com.timecat.module.user.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.block.BlockDao
import com.timecat.data.bmob.dao.block.CommentDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.module.user.R
import com.timecat.module.user.adapter.BlockAdapter
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.login.BaseLoginListActivity
import com.timecat.module.user.view.FooterView
import com.timecat.identity.data.block.BlockMini
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener
import kotlinx.android.synthetic.main.user_activity_app_detail.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-24
 * @description 动态、评论
 * @usage null
 */
abstract class BaseBlockDetailActivity : BaseLoginListActivity() {
    lateinit var blockAdapter: BlockAdapter

    override fun layout(): Int = R.layout.user_activity_app_detail

    override fun routerInject() = NAV.inject(this)

    override fun getAdapter(): RecyclerView.Adapter<*> {
        blockAdapter = BlockAdapter(this, ArrayList())
        blockAdapter.addHeaderView(getHeaderView())
        blockAdapter.addFooterView(getFooterView())
        return blockAdapter
    }

    abstract fun getHeaderView(): View
    abstract fun getDetailBlockId(): String
    abstract fun initBlockHeader(block: Block)

    open fun getFooterView(): View {
        val footerView = FooterView(this)
        return footerView
    }

    override fun onRefresh() {
        BlockDao.findByMini(BlockMini(getDetailBlockId()), object : OnFindListener<Block> {
            override fun success(data: List<Block>) {
                initByBlock(data[0])
                mRefreshLayout.isRefreshing = false
            }

            override fun error(e: DataError) {
                ToastUtil.e_long(e.message)
                mRefreshLayout.isRefreshing = false
            }
        })
    }

    private fun initByBlock(block: Block) {
        initBlockHeader(block)
        initComment(block)
        initFooterByBlock(block)
    }

    private fun initComment(block: Block) {
        CommentDao.findAll(block, object : OnFindListener<Block> {
            override fun success(data: List<Block>) {
                blockAdapter.replaceData(data.map { BlockItem(it) })
                LogUtil.i(data)
            }

            override fun error(e: DataError) {
                LogUtil.e(e)
            }
        })
    }

    private fun initFooterByBlock(block: Block) {
        comment.setOnClickListener {
            GO.addCommentFor(block)
        }
    }

}