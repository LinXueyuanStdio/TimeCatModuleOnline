package com.timecat.module.user.social.cloud.slice

import com.timecat.data.bmob.ext.net.allForum
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * 论坛
 */
@FragmentAnno(RouterHub.USER_ForumFragment)
class ForumFragment : BaseEndlessBlockFragment() {
    override fun name(): String = "论坛"
    override fun query() = allForum()
    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        loadData()
    }
}