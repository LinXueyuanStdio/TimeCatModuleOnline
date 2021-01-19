package com.timecat.module.user.base

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.ext.bmob.requestAction
import com.timecat.module.user.adapter.action.ActionItem
import com.timecat.module.user.adapter.block.BlockItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessActionFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): BmobQuery<Action>

    override fun loadFirst() {
        requestAction {
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
                adapter.reload(listOf(ActionItem(activity, it)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    ActionItem(activity, it)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadMore() {
        requestAction {
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
                adapter.onLoadMoreComplete(listOf(ActionItem(activity, it)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    ActionItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}