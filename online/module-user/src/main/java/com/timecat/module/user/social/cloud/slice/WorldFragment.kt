package com.timecat.module.user.social.cloud.slice

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allMoment
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * 世界动态
 */
@FragmentAnno(RouterHub.USER_WorldFragment)
class WorldFragment : BaseEndlessBlockFragment() {
    override fun name(): String = "动态"
    override fun query(): BmobQuery<Block> = allMoment().apply {
        //世界动态是没有父节点的动态
        //有父节点的动态，可能是论坛里的动态
        addWhereDoesNotExists("parent")
    }
}