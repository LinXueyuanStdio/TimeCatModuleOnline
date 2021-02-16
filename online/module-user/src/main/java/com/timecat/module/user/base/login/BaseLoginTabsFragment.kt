package com.timecat.module.user.base.login

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.gturedi.views.StatefulLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.social.user.vm.UserViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/27
 * @description null
 * @usage null
 */
abstract class BaseLoginTabsFragment : BaseTabsFragment() {
    fun I(): User = UserDao.getCurrentUser() ?: throw Exception("未登录")
    override fun layout(): Int = R.layout.base_user_stateful_viewpager_tabs

    lateinit var userViewModel: UserViewModel

    var mStatefulLayout: StatefulLayout? = null
    override fun bindView(view: View) {
        super.bindView(view)
        mStatefulLayout = view.findViewById(R.id.ll_stateful)
    }

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
            mStatefulLayout?.showContent()
            userViewModel.loadUser(I)
            initViewAfterLogin()
        }
    }

    protected open fun initViewAfterLogin() {}
    protected open fun loadDetail(user: User) {}
}