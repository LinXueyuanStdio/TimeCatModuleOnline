package com.timecat.module.user.base


import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.InterAction
import com.timecat.data.bmob.ext.bmob.requestInterAction
import com.timecat.module.user.adapter.interaction.InterActionItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description null
 * @usage null
 */
abstract class BaseInterActionListActivity : BaseEndlessListActivity() {
    abstract fun query(): AVQuery<InterAction>

    override fun loadFirst() {
        requestInterAction {
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
                val activity = this@BaseInterActionListActivity
                val items = it.map {
                    InterActionItem(activity, it)
                }
                adapter.updateDataSet(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadMore() {
        requestInterAction {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = this@BaseInterActionListActivity
                val items = it.map {
                    InterActionItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}