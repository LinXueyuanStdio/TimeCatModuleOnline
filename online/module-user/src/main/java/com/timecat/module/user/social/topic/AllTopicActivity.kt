package com.timecat.module.user.social.topic

import com.timecat.data.bmob.data.common.Block
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allTopic
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.base.BaseEndlessBlockActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 所有话题
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllTopicActivity)
class AllTopicActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "所有话题"
    override fun query() = allTopic()
    override fun block2Item(block: Block) = BlockSmallItem(this, block)
}