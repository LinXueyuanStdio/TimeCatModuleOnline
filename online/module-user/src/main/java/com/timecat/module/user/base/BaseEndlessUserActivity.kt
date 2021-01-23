package com.timecat.module.user.base


import cn.leancloud.AVQuery
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestUser
import com.timecat.module.user.adapter.user.UserItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseEndlessUserActivity : BaseEndlessListActivity() {
    abstract fun query(): AVQuery<_User>

    override fun loadFirst() {
        requestUser {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.CACHE_ELSE_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = this@BaseEndlessUserActivity
                val items = it.map {
                    UserItem(activity, it)
                }
                adapter.updateDataSet(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadMore() {
        requestUser {
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
                val activity = this@BaseEndlessUserActivity
                val items = it.map {
                    UserItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}