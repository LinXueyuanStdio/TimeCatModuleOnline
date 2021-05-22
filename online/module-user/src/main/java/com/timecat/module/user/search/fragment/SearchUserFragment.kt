package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.search.AVSearchQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.bmob.searchList
import com.timecat.data.bmob.ext.bmob.userQuery
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.user.SearchUserItem
import com.timecat.module.user.adapter.user.SelectUserItem
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
@FragmentAnno(RouterHub.SEARCH_SearchUserFragment)
open class SearchUserFragment : BaseSearchFragment() {

    lateinit var userQuery: AVSearchQuery<AVObject>

    override fun onSearch(q: String) {
        LogUtil.se(q)
        userQuery = userQuery(q)
        mStatefulLayout.showLoading()
        disposable = searchList {
            query = userQuery.apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                LogUtil.sd(it)
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

    override fun loadFirst() {}

    override fun loadMore() {
        disposable = searchList {
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

    open fun transform(activity: Activity, user: AVObject): BaseItem<*> {
        return SearchUserItem(activity, transform(user))
    }

    fun transform(user: AVObject): User {
        val jsonString = user.toJSONString()
        val rawObject = AVObject.parseAVObject(jsonString)
        return Transformer.transform(rawObject, User::class.java)
    }
}