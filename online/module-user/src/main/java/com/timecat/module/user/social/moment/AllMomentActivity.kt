package com.timecat.module.user.social.moment

import com.timecat.data.bmob.data.common.Block
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allMoment
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.block.BlockItem
import com.timecat.module.user.base.BaseEndlessBlockActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 所有论坛
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllMomentActivity)
class AllMomentActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "所有动态"
    override fun query() = allMoment()
    override fun block2Item(block: Block) = BlockItem(this, block)
}