package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.search.AVSearchQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.blockQuery
import com.timecat.data.bmob.ext.bmob.searchBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.SearchBlockItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
open class SearchBlockFragment : BaseSearchFragment() {

    lateinit var blockQuery: AVSearchQuery<Block>

    open fun query(q: String): AVSearchQuery<Block> = blockQuery(q)

    override fun onSearch(q: String) {
        LogUtil.se(q)
        blockQuery = query(q)
        mStatefulLayout.showLoading()
        searchBlock {
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

    override fun loadData() {}

    override fun loadFirst() {}

    override fun loadMore() {
        searchBlock {
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

    open fun transform(activity: Activity, block: Block): BaseItem<*> {
        return SearchBlockItem(activity, block)
    }
}