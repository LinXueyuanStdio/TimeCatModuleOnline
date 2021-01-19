package com.timecat.module.user.social.trace

import androidx.fragment.app.Fragment
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.login.BaseLoginToolbarActivity
import com.timecat.page.base.R
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllTraceActivity)
class AllTraceActivity : BaseLoginToolbarActivity() {
    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: _User? = null

    override fun title(): String = "足迹"
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        val fm = this.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.container)
        if (fragment == null) {
            fragment = this.createFragment()
            fm.beginTransaction().add(R.id.container, fragment).commit()
        }
    }

    fun createFragment(): Fragment {
        return Router.with(RouterHub.USER_TraceFragment)
            .putSerializable("user", user)
            .navigate() ?: FallBackFragment()
    }

}