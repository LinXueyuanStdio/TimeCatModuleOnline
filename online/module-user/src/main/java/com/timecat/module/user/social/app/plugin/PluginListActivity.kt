package com.timecat.module.user.social.app.plugin

import cn.bmob.v3.BmobQuery
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.block.AppDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allPluginApp
import com.timecat.data.bmob.ext.net.allTopic
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.BaseBlockListActivity
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.base.BaseEndlessBlockActivity

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
class PluginListActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "插件中心"
    override fun query(): BmobQuery<Block> = allPluginApp()
    override fun block2Item(block: Block) = BlockSmallItem(this, block)
}