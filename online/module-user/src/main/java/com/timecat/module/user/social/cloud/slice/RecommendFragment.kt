package com.timecat.module.user.social.cloud.slice

import com.timecat.data.bmob.ext.net.globalRecommendBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessActionFragment
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * 推荐
 */
@FragmentAnno(RouterHub.USER_RecommendFragment)
class RecommendFragment : BaseEndlessActionFragment() {
    override fun name(): String = "推荐"
    override fun query() = globalRecommendBlock().apply {
        include("block")
    }

    override fun initViewAfterLogin() {
        loadData()
    }
}