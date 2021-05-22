package com.timecat.module.user.base.login

import androidx.lifecycle.ViewModelProvider
import com.gturedi.views.StatefulLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseMainActivity
import com.timecat.module.user.social.user.vm.UserViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/8
 * @description null
 * @usage null
 */
abstract class BaseLoginMainActivity : BaseMainActivity() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")

    override fun layoutId(): Int = R.layout.user_activity_base_login_main

    var mStatefulLayout: StatefulLayout? = null

    lateinit var userViewModel: UserViewModel

    override fun bindView() {
        super.bindView()
        mStatefulLayout = findViewById(R.id.ll_stateful)
    }

    override fun initView() {
        super.initView()
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.user.observe(this, {
            it?.let { loadDetail(it) }
        })
        val I = UserDao.getCurrentUser()
        if (I == null) {
            mStatefulLayout?.showError("请登录") {
                NAV.go(RouterHub.LOGIN_LoginActivity)
            }
        } else {
            userViewModel.loadUser(I)
            initViewAfterLogin()
        }
    }

    open fun onPrepareContent() {
        mStatefulLayout?.showLoading()
    }

    open fun onContentLoaded() {
        mStatefulLayout?.showContent()
    }

    protected open fun initViewAfterLogin() {
        onPrepareContent()
    }

    protected open fun loadDetail(user: User) {
        onContentLoaded()
    }
}