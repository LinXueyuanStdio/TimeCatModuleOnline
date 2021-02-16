package com.timecat.module.user.base

import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.adapter.detail.BaseDetailVH

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description null
 * @usage null
 */
abstract class BaseBlockListActivity : BaseSimpleListActivity() {
    abstract fun query(): AVQuery<Block>
    override fun onRefresh() {
        requestBlock {
            query = query().apply {
                cachePolicy = AVQuery.CachePolicy.CACHE_THEN_NETWORK
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                mRefreshLayout.isRefreshing = false
                val items = it.map {
                    block2Item(it)
                }
                mAdapter.updateDataSet(items)
                mStatefulLayout?.showContent()
            }
        }
    }

    open fun block2Item(block: Block): BaseItem<out BaseDetailVH> {
        return BlockSmallItem(this, block)
    }
}