package com.timecat.module.user.game.bag

import android.os.Bundle
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.net.allOwnItem
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessOwnItemFragment
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 足迹
 * Trace 是 log，指向 string 的 target id，持有一个 type 和 target type
 * @usage null
 */
@FragmentAnno(RouterHub.USER_BagFragment)
class BagFragment : BaseEndlessOwnItemFragment() {
    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun name() = "足迹"
    override fun query() = user?.allOwnItem() ?: I().allOwnItem()
    override fun initViewAfterLogin() {
    }
}