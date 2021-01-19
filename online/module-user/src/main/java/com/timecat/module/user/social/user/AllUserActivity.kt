package com.timecat.module.user.social.user

import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allUsers
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseEndlessUserActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 元权限
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllUserActivity)
class AllUserActivity : BaseEndlessUserActivity() {
    override fun title(): String = "用户"
    override fun query() = allUsers()
}