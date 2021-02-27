package com.timecat.module.user.social.cloud.slice

import com.timecat.data.bmob.ext.net.allMoment
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * 世界动态
 */
@FragmentAnno(RouterHub.USER_WorldFragment)
class WorldFragment : BaseEndlessBlockFragment() {
    override fun name() = "动态"
    override fun query() = allMoment()

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        loadData()
    }
}