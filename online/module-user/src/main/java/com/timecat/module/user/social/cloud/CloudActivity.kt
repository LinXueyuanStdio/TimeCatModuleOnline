package com.timecat.module.user.social.cloud

import androidx.fragment.app.Fragment
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.page.base.friend.compact.BaseFragmentActivity
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CloudActivity)
class CloudActivity : BaseFragmentActivity() {
    override fun createFragment(): Fragment = NAV.fragment(RouterHub.USER_CloudFragment)
}