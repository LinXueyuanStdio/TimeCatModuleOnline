package com.timecat.module.user.game.bag

import androidx.fragment.app.Fragment
import cn.bmob.v3.BmobQuery
import com.timecat.component.router.app.FallBackFragment
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allTag
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.data.bmob.data.game.item.OwnItem
import com.timecat.data.bmob.ext.bmob.request
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.BaseBlockListActivity
import com.timecat.page.base.R
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
@RouterAnno(hostAndPath = RouterHub.USER_BagActivity)
class BagActivity : BaseBlockListActivity() {
    override fun title(): String = "物品"
    override fun routerInject() = NAV.inject(this)
    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: _User? = null

    override fun initViewAfterLogin() {
        val fm = this.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.container)
        if (fragment == null) {
            fragment = this.createFragment()
            fm.beginTransaction().add(R.id.container, fragment).commit()
        }
    }

    fun createFragment(): Fragment {
        return Router.with(RouterHub.USER_BagFragment)
            .putSerializable("user", user)
            .navigate() ?: FallBackFragment()
    }
}