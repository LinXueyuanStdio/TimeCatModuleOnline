package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.search.AVSearchQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.bmob.searchUser
import com.timecat.data.bmob.ext.bmob.userQuery
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.user.SearchUserItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
open class SearchBlockFragment : BaseSearchFragment() {

    lateinit var userQuery: AVSearchQuery<User>

    override fun onSearch(q: String) {
        LogUtil.se(q)
        userQuery = userQuery(q)
        mStatefulLayout.showLoading()
        searchUser {
            query = userQuery.apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    transform(activity, it)
                }
                adapter.reload(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadData() {}

    override fun loadFirst() {}

    override fun loadMore() {
        searchUser {
            query = userQuery.apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val activity = requireActivity()
                val items = it.map {
                    transform(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    open fun transform(activity: Activity, user: User): BaseItem<*> {
        return SearchUserItem(activity, user)
    }
}