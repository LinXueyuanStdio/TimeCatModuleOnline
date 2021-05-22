package com.timecat.module.user.game.mail

import android.os.Bundle
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.net.allOwnMail
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessOwnMailFragment
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 邮件
 * @usage null
 */
@FragmentAnno(RouterHub.USER_OwnMailFragment)
class OwnMailFragment : BaseEndlessOwnMailFragment() {
    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun name() = "邮件"
    override fun query() = user?.allOwnMail() ?: I().allOwnMail()
}