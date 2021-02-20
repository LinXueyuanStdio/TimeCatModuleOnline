package com.timecat.module.user.social.common


import androidx.fragment.app.FragmentActivity
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.asBlock
import com.timecat.data.bmob.ext.bmob.findAllComments
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.CommentItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题讨论
 * @usage null
 */
class CommentListFragment : BaseBlockListFragment() {
    override fun name(): String = "讨论"
    override fun query() = viewModel.block.value!!.findAllComment()
    override fun loadFirst() {
        findAllComments(offset, pageSize, viewModel.block.value!!.objectId) {
            onSuccess = {
                if (it.isEmpty()) {
                    emptyCallback()
                } else {
                    offset += it.size
                    mRefreshLayout.isRefreshing = false
                    val activity = requireActivity()
                    val items = it.map {
                        block2Item(activity, it.asBlock())
                    }
                    adapter.reload(items)
                    mStatefulLayout?.showContent()
                }
            }
            onError = errorCallback
        }
    }

    override fun loadMore() {
        findAllComments(offset, pageSize, viewModel.block.value!!.objectId) {
            onSuccess = {
                if (it.isEmpty()) {
                    emptyCallback()
                } else {
                    offset += it.size
                    mRefreshLayout.isRefreshing = false
                    val activity = requireActivity()
                    val items = it.map {
                        block2Item(activity, it.asBlock())
                    }
                    adapter.onLoadMoreComplete(items)
                    mStatefulLayout?.showContent()
                }
            }
            onError = errorCallback
        }
    }
    override fun addNew(block: Block) {
        GO.addCommentFor(block)
    }

    override fun block2Item(activity: FragmentActivity, block: Block): BaseItem<out BaseDetailVH> {
        return CommentItem(activity, block)
    }
}