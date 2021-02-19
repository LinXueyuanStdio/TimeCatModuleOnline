package com.timecat.module.user.base


import androidx.fragment.app.FragmentActivity
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.ext.bmob.requestAction
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.action.ActionItem
import com.timecat.module.user.adapter.detail.BaseDetailVH

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessActionFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): AVQuery<Action>

    override fun loadFirst() {
        requestAction {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.CACHE_THEN_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    action2Item(activity, it)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    open fun action2Item(activity: FragmentActivity, action: Action): BaseItem<out BaseDetailVH> {
        return ActionItem(activity, action)
    }
    override fun loadMore() {
        requestAction {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.CACHE_THEN_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    action2Item(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}