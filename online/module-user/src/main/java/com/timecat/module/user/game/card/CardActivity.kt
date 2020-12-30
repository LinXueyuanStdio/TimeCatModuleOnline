package com.timecat.module.user.game.card

import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allTag
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.BaseBlockListActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 抽卡
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CardActivity)
class CardActivity : BaseBlockListActivity() {
    override fun title(): String = "抽卡"
    override fun routerInject() = NAV.inject(this)

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        requestBlock {
            query = allTag()
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = { data ->
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                mAdapter.replaceData(listOf(BlockItem(data)))
            }
            onListSuccess = { data ->
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                mAdapter.replaceData(data.map { BlockItem(it) })
            }
        }
    }
}