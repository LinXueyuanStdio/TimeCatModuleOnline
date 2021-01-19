package com.timecat.module.user.social.forum

import com.timecat.data.bmob.data.common.Block
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allForum
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.BaseEndlessBlockActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 所有论坛
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllForumActivity)
class AllForumActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "所有论坛"
    override fun query() = allForum()
    override fun block2Item(block: Block) = BlockSmallItem(this, block)
}