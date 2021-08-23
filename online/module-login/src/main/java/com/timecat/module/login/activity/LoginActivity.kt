package com.timecat.module.login.activity

import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.EasyRequestUser
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.login.R
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * 登录
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_LoginActivity)
class LoginActivity : BaseToolbarActivity() {
    @AttrValueAutowiredAnno("path")
    var path = ""

    override fun title(): String = getString(R.string.xiaoxing_login_login)
    override fun layout(): Int = R.layout.login_activity_login
    override fun routerInject() = NAV.inject(this)

    private lateinit var usernameLayout: TextInputLayout
    private lateinit var usernameEt: TextInputEditText
    private lateinit var passwordLayout: TextInputLayout
    private lateinit var passwordEt: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var tvReg: TextView
    private lateinit var tvForgotPwd: TextView

    override fun bindView() {
        super.bindView()
        usernameLayout = findViewById(R.id.username_layout)
        usernameEt = findViewById(R.id.username_et)
        passwordLayout = findViewById(R.id.password_layout)
        passwordEt = findViewById(R.id.password_et)
        btnLogin = findViewById(R.id.btn_login)
        tvReg = findViewById(R.id.tv_reg)
        tvForgotPwd = findViewById(R.id.tv_forgot_pwd)
    }

    override fun initView() {
        btnLogin.setShakelessClickListener {
            KeyboardUtils.hideSoftInput(btnLogin)
            login()
        }
        tvReg.setShakelessClickListener {
            NAV.go(this@LoginActivity, RouterHub.LOGIN_RegisterCheckExistActivity)
        }
        tvForgotPwd.setShakelessClickListener {
            NAV.go(this@LoginActivity, RouterHub.LOGIN_ForgotPwdCheckExistActivity)
        }
    }

    private fun login() {
        if (TextUtils.isEmpty(username)) {
            ToastUtil.w("帐户名/手机号/邮箱不能为空")
            return
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtil.w("密码不能为空")
            return
        }
        doLogin(username, password)
    }

    private val password: String
        get() = passwordEt.text?.toString() ?: ""
    private val username: String
        get() = usernameEt.text?.toString() ?: ""

    private fun doLogin(username: String, password: String) {
        UserDao.login(username, password, EasyRequestUser().apply {
            onSuccess = {
                //登录成功
                val user = UserDao.getCurrentUser()
                if (user != null) {
                    LogUtil.e("登陆成功")
                    onLoginSuccess()
                }
            }
            onError = {
                LogUtil.e(it.message)
                ToastUtil.e_long(it.message)
            }
        })
    }

    /**
     * 这里没有做初始化的工作
     */
    fun onLoginSuccess() {
        if (!TextUtils.isEmpty(path)) NAV.goAndFinish(this, path)
        else NAV.goAndFinish(this, RouterHub.MASTER_MainActivity)
    }
}