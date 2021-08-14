package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.search.AVSearchQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.blockQuery
import com.timecat.data.bmob.ext.bmob.searchBlock
import com.timecat.data.bmob.ext.bmob.searchList
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.SearchBlockItem
import com.timecat.module.user.ext.toBlock

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
open class SearchBlockFragment : BaseSearchFragment() {

    lateinit var blockQuery: AVSearchQuery<AVObject>

    open fun query(q: String): AVSearchQuery<AVObject> = blockQuery(q)

    override fun onSearch(q: String) {
        LogUtil.se(q)
        blockQuery = query(q)
        mStatefulLayout.showLoading()
        disposable = searchBlock {
            query = blockQuery.apply {
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

    override fun loadFirst() {}

    override fun loadMore() {
        disposable = searchList {
            query = blockQuery.apply {
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

    open fun transform(activity: Activity, block: AVObject): BaseItem<*> {
        return SearchBlockItem(activity, transform(block))
    }

    fun transform(user: AVObject): Block {
        return user.toBlock()
    }
}