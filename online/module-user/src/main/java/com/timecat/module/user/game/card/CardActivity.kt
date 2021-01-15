package com.timecat.module.user.game.card

import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.login.BaseLoginToolbarActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 抽卡
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CardActivity)
class CardActivity : BaseLoginToolbarActivity() {
    override fun title(): String = "抽卡"
    override fun routerInject() = NAV.inject(this)

}