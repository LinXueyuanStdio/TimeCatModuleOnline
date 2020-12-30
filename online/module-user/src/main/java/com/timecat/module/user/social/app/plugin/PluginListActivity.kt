package com.timecat.module.user.social.app.plugin

import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.block.AppDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.BaseBlockListActivity
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description 插件中心 插件市场 插件商店
 * 仅供查看，不供调用
 * 调用请在我的插件里调用
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.PLUGIN_PluginListActivity)
class PluginListActivity : BaseBlockListActivity() {
    override fun title(): String = "插件中心"

    override fun onRefresh() {
        AppDao.getAllPluginApp(object : OnFindListener<Block> {
            override fun success(data: List<Block>) {
                mRefreshLayout.isRefreshing = false
                if (data.isEmpty()) {
                    ToastUtil.w("什么也没找到")
                } else {
                    mAdapter.replaceData(data.map { BlockItem(it) })
                }
            }

            override fun error(e: DataError) {
                mRefreshLayout.isRefreshing = false
                ToastUtil.w("查询失败")
            }
        })
    }
}