package com.timecat.module.user.manager.me

import android.os.Bundle
import android.util.Log
import cn.bmob.v3.BmobQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestAction
import com.timecat.data.bmob.ext.net.allAction
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessActionFragment
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
@FragmentAnno(RouterHub.USER_TraceFragment)
class TraceFragment : BaseEndlessActionFragment() {
    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: _User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }
    override fun name() = "足迹"
    override fun query() = user?.allAction() ?: I().allAction()
    override fun initViewAfterLogin() {
    }
}