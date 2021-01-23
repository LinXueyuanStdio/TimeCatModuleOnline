package com.timecat.module.user.base


import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.User2User
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.module.user.adapter.user.UserItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseEndlessUserRelationActivity : BaseEndlessListActivity() {
    abstract fun query(): AVQuery<User2User>

    override fun loadFirst() {
        requestUserRelation {
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
                val activity = this@BaseEndlessUserRelationActivity
                val items = it.map { it.target }.filterNotNull().map {
                    UserItem(activity, it)
                }
                adapter.updateDataSet(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadMore() {
        requestUserRelation {
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
                val activity = this@BaseEndlessUserRelationActivity
                val items = it.map { it.target }.filterNotNull().map {
                    UserItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}