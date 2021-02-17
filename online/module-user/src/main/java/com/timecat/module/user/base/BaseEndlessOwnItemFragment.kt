package com.timecat.module.user.base


import androidx.lifecycle.ViewModelProvider
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.module.user.adapter.game.OwnItemItem
import com.timecat.module.user.game.bag.vm.ItemViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessOwnItemFragment : BaseEndlessListFragment() {

    abstract fun name(): String
    abstract fun query(): AVQuery<OwnItem>

    lateinit var itemViewModel: ItemViewModel
    override fun initViewAfterLogin() {
        itemViewModel = ViewModelProvider(requireActivity()).get(ItemViewModel::class.java)
    }

    override fun loadFirst() {
        itemViewModel.attachLifecycle = requestOwnItem {
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
                    OwnItemItem(activity, it)
                }
                adapter.reload(items)
                itemViewModel.loadPagedOwnItems(it)
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun loadMore() {
        itemViewModel.attachLifecycle = requestOwnItem {
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
                    OwnItemItem(activity, it)
                }
                adapter.onLoadMoreComplete(items)
                itemViewModel.loadPagedOwnItems(it)
                mStatefulLayout?.showContent()
            }
        }
    }
}