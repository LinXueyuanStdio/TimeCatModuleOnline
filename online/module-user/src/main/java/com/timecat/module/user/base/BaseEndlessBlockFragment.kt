package com.timecat.module.user.base

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.module.user.adapter.block.BlockItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessBlockFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): BmobQuery<Block>

    override fun loadFirst() {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = BmobQuery.CachePolicy.CACHE_ELSE_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += 1
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                adapter.reload(listOf(BlockItem(activity, it)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    BlockItem(activity, it)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override  fun loadMore() {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = BmobQuery.CachePolicy.CACHE_ELSE_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += 1
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                adapter.onLoadMoreComplete(listOf(BlockItem(activity, it)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    BlockItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}