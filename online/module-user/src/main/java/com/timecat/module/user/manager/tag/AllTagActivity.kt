package com.timecat.module.user.manager.tag

import com.timecat.data.bmob.data.common.Block
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allTag
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.base.BaseEndlessBlockActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 所有标签
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllTagActivity)
class AllTagActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "所有标签"
    override fun query() = allTag()
    override fun block2Item(block: Block) = BlockSmallItem(this, block)
}