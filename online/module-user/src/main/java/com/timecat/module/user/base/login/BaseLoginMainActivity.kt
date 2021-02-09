package com.timecat.module.user.base.login

import com.gturedi.views.StatefulLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseMainActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/8
 * @description null
 * @usage null
 */
abstract class BaseLoginMainActivity : BaseMainActivity() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")
    var mStatefulLayout: StatefulLayout? = null

    override fun layoutId(): Int = R.layout.user_activity_base_login_main
    override fun init() {
        mStatefulLayout = findViewById(R.id.ll_stateful)
        mStatefulLayout?.showLoading()
        val I = UserDao.getCurrentUser()
        if (I == null) {
            mStatefulLayout?.showError("请登录") {
                NAV.go(RouterHub.LOGIN_LoginActivity)
            }
        } else {
            mStatefulLayout?.showContent()
            initViewAfterLogin()
        }
    }

    protected open fun initViewAfterLogin() {}
}