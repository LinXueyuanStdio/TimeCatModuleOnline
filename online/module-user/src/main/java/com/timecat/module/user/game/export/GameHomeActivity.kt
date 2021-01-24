package com.timecat.module.user.game.export

import androidx.fragment.app.Fragment
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.page.base.friend.compact.BaseFragmentActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/24
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_GameHomeActivity)
class GameHomeActivity : BaseFragmentActivity() {
    override fun createFragment(): Fragment = NAV.fragment(RouterHub.USER_GameHomeFragment)
}