package com.timecat.login.activity

import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.login.R
import com.xiaojinzi.component.anno.RouterAnno

/**
 * 协议
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_ProtocolActivity)
class ProtocolActivity : BaseToolbarActivity() {

    override fun title(): String = getString(R.string.xiaoxing_login_protocol)
    override fun layout(): Int = R.layout.login_activity_protocol
    override fun routerInject() = NAV.inject(this)

    override fun initView() {

    }
}