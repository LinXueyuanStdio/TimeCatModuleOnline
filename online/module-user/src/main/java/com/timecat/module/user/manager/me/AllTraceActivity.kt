package com.timecat.module.user.manager.me

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.identity.readonly.RouterHub
import com.timecat.page.base.friend.compact.BaseFragmentActivity
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
class AllTraceActivity : BaseFragmentActivity() {
    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: _User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createFragment(): Fragment {
        return Router.with(RouterHub.USER_TraceFragment)
            .putSerializable("user", user)
            .navigate() ?: FallBackFragment()
    }

}