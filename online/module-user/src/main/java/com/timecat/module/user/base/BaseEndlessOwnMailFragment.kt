package com.timecat.module.user.base


import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.data.bmob.ext.bmob.requestOwnMail
import com.timecat.module.user.adapter.game.OwnMailItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessOwnMailFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): AVQuery<OwnMail>

    override fun loadFirst() {
        requestOwnMail {
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
                    OwnMailItem(activity, it)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadMore() {
        requestOwnMail {
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
                    OwnMailItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}