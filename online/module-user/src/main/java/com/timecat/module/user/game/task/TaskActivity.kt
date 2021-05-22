package com.timecat.module.user.game.task

import androidx.fragment.app.Fragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.login.BaseLoginToolbarFragmentActivity
import com.timecat.module.user.game.cube.fragment.*
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description
 * @usage
 */
@RouterAnno(hostAndPath = RouterHub.USER_TaskActivity)
class TaskActivity : BaseLoginToolbarFragmentActivity() {

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String

    override fun routerInject() = NAV.inject(this)
    override fun title(): String {
        TODO("Not yet implemented")
    }

    override fun createFragment(): Fragment {
        TODO("Not yet implemented")
    }

}