package com.timecat.module.user.base.login

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.social.user.vm.UserViewModel
import com.timecat.page.base.friend.list.BaseStatefulRefreshListFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseLoginListFragment : BaseStatefulRefreshListFragment() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")

    open fun routerInject() {}
    override fun onCreate(savedInstanceState: Bundle?) {
        routerInject()
        super.onCreate(savedInstanceState)
    }

    lateinit var userViewModel: UserViewModel
    override fun lazyInit() {
        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
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
        mRefreshLayout.isRefreshing = false
    }

    protected open fun initViewAfterLogin() {
        onContentLoaded()
    }

    protected open fun loadDetail(user: User) {}
}