package com.timecat.module.user.base

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.requestBlockRelation
import com.timecat.module.user.adapter.block.BlockItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessBlock2BlockFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): BmobQuery<Block2Block>

    override fun loadFirst() {
        requestBlockRelation {
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
                adapter.reload(listOf(BlockItem(activity, it.to)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    BlockItem(activity, it.to)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override  fun loadMore() {
        requestBlockRelation {
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
                adapter.onLoadMoreComplete(listOf(BlockItem(activity, it.to)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    BlockItem(activity, it.to)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}