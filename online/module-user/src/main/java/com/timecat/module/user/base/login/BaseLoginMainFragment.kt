package com.timecat.module.user.base.login

import android.view.View
import com.gturedi.views.StatefulLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.page.base.friend.main.BaseMainFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseLoginMainFragment : BaseMainFragment() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")
    override fun layout(): Int = R.layout.base_blur_toolbar_stateful_container

    var mStatefulLayout: StatefulLayout? = null
    override fun bindView(view: View) {
        super.bindView(view)
        mStatefulLayout = view.findViewById(R.id.ll_stateful)
    }

    override fun lazyInit() {
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

    protected open fun initViewAfterLogin() {
    }
}