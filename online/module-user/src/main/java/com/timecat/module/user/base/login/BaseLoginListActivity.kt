package com.timecat.module.user.base.login

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseLoginListActivity : BaseRefreshListActivity() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")

    override fun initView() {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            mStatefulLayout?.showError("请登录") {
                NAV.go(RouterHub.LOGIN_LoginActivity)
            }
        } else {
            mStatefulLayout?.showContent()
            super.initView()
            initViewAfterLogin()
        }
    }

    protected open fun initViewAfterLogin() {}
}