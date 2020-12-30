package com.timecat.module.user.base

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.BlockItem
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseEndlessBlockActivity : BaseEndlessListActivity() {
    abstract fun query(): BmobQuery<Block>

    override fun loadFirst() {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = BmobQuery.CachePolicy.CACHE_ELSE_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += 1
                mRefreshLayout.isRefreshing = false
                adapter.updateDataSet(listOf(block2Item(it)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val items = it.map {
                    block2Item(it)
                }
                adapter.updateDataSet(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    fun block2Item(block:Block): BaseItem<out BaseDetailVH> {
        return if (block.type in listOf(BLOCK_LEADER_BOARD, BLOCK_FORUM, BLOCK_APP, BLOCK_TOPIC, BLOCK_TAG, BLOCK_PERMISSION, BLOCK_ROLE, BLOCK_IDENTITY)){
            BlockSmallItem(this, block)
        } else {
            BlockItem(this, block)
        }
    }

    override fun loadMore() {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += 1
                mRefreshLayout.isRefreshing = false
                adapter.onLoadMoreComplete(listOf(block2Item(it)))
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                offset += it.size
                mRefreshLayout.isRefreshing = false
                val items = it.map {
                    block2Item(it)
                }
                adapter.onLoadMoreComplete(items)
                mStatefulLayout?.showContent()
            }
        }
    }
}