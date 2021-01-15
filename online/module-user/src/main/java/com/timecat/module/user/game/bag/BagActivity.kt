package com.timecat.module.user.game.bag

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allTag
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.data.bmob.data.game.item.OwnItem
import com.timecat.data.bmob.ext.bmob.request
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.BaseBlockListActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 背包的所有物品
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_BagActivity)
class BagActivity : BaseBlockListActivity() {
    override fun title(): String = "物品"
    override fun routerInject() = NAV.inject(this)

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        requestOwnItem {
            query = BmobQuery<OwnItem>().apply {
                addWhereEqualTo("user", I())
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = { data ->
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                mAdapter.setList(listOf(BlockItem(data.item)))
            }
            onListSuccess = { data ->
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                mAdapter.setList(data.map { BlockItem(it.item) })
            }
        }
    }
}