package com.timecat.module.user.base

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.service.DataError
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.user.adapter.block.NotMoreItem
import com.timecat.module.user.base.login.BaseLoginListActivity
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.Payload

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description 无尽的块，自动分页加载
 * @usage null
 */
abstract class BaseEndlessListActivity : BaseLoginListActivity() {
    private val notMoreItem: NotMoreItem = NotMoreItem()
    lateinit var adapter: BaseAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        adapter = BaseAdapter(null)
        adapter.addScrollableFooter(NotMoreItem())
        return adapter
    }

    override fun initViewAfterLogin() {
        adapter.setDisplayHeadersAtStartUp(false) //Show Headers at startUp!
            .setStickyHeaders(false) //Make headers sticky
            .setLoadingMoreAtStartUp(false)
            .setEndlessPageSize(pageSize)  //if newItems < 7
            .setEndlessScrollThreshold(4) //Default=1
            .setEndlessScrollListener(object : FlexibleAdapter.EndlessScrollListener {
                override fun noMoreLoad(newItemsSize: Int) {
                }

                override fun onLoadMore(lastPosition: Int, currentPage: Int) {
                    loadMore()
                }
            }, notMoreItem)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        adapter.onRestoreInstanceState(savedInstanceState)
    }

    override fun loadData() {
        load()
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = false
    }

    var errorCallback: (DataError) -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showError("查询失败") {
            mRefreshLayout.isRefreshing = true
            mStatefulLayout?.showLoading()
            loadData()
        }
        ToastUtil.e("查询失败")
        it.printStackTrace()
    }
    var emptyCallback: () -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showContent()
        adapter.updateItem(notMoreItem, Payload.NO_MORE_LOAD)
    }

    open var pageSize = 10
    var offset = 0

    fun load() {
        offset = 0
        loadFirst()
    }

    abstract fun loadFirst()

    abstract fun loadMore()
}