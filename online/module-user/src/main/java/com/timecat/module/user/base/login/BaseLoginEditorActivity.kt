package com.timecat.module.user.base.login

import androidx.lifecycle.ViewModelProvider
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.image.BaseImageSelectorActivity
import com.timecat.module.user.social.user.vm.UserViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseLoginEditorActivity : BaseImageSelectorActivity() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")

    lateinit var userViewModel: UserViewModel
    override fun initView() {
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
            mStatefulLayout?.showContent()
            super.initView()
            userViewModel.loadUser(I)
            initViewAfterLogin()
        }
    }

    protected open fun initViewAfterLogin() {}
    protected open fun loadDetail(user: User) {}
}