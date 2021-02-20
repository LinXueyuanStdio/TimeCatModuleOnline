package com.timecat.module.user.base

import androidx.fragment.app.FragmentActivity
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.BlockItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessBlockFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): AVQuery<Block>

    var current: Disposable? = null
    override fun loadFirst() {
        current?.dispose()
        current = requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    block2Item(activity, it)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    open fun block2Item(activity: FragmentActivity, block: Block): BaseItem<out BaseDetailVH> {
        return BlockItem(activity, block)
    }

    override fun loadMore() {
        current = requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    block2Item(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}