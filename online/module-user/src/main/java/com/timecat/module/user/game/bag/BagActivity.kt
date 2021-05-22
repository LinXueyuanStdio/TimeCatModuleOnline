package com.timecat.module.user.game.bag

import androidx.fragment.app.Fragment
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.login.BaseLoginToolbarFragmentActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.Router

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 背包的所有物品
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllOwnItemActivity)
class BagActivity : BaseLoginToolbarFragmentActivity() {

    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: User? = null

    override fun title(): String = "背包"
    override fun routerInject() = NAV.inject(this)
    override fun createFragment(): Fragment {
        return Router.with(RouterHub.USER_AllOwnItemFragment)
            .putParcelable("user", user)
            .navigate() ?: FallBackFragment()
    }
}