package com.timecat.module.login.activity

import android.text.TextUtils
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import cn.bmob.v3.listener.UpdateListener
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.page.base.view.MyClickListener
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.login.R
import com.timecat.module.login.data.UserRegister
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import kotlinx.android.synthetic.main.login_activity_register_set_pwd.*

/**
 * 设置密码页面
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_RegisterSetPwdActivity)
class RegisterSetPwdActivity : BaseToolbarActivity() {
    @AttrValueAutowiredAnno("mPhone")
    @JvmField
    var mPhone: String? = null

    @AttrValueAutowiredAnno("mEmail")
    @JvmField
    var mEmail: String? = null

    @AttrValueAutowiredAnno("mUsername")
    @JvmField
    var mUsername: String? = null

    @AttrValueAutowiredAnno("type")
    @JvmField
    var type: String? = null

    override fun title(): String = getString(R.string.xiaoxing_login_set_pwd)
    override fun layout(): Int = R.layout.login_activity_register_set_pwd
    override fun routerInject() = NAV.inject(this)

    override fun initView() {
        btn_ok.setOnClickListener(MyClickListener {
            toRegister()
        })
    }

    private fun toRegister() {
        if (TextUtils.isEmpty(newPassword)) {
            ToastUtil.w("新密码不能为空")
            return
        }
        if (TextUtils.isEmpty(newPasswordAgain)) {
            ToastUtil.w("确认密码不能为空")
            return
        }
        if (newPassword != newPasswordAgain) {
            ToastUtil.w("两次密码输入的不一致，请重新输入")
            return
        }
        when (type) {
            "phone" -> {
                register(mPhone, newPassword)
            }
            "email" -> {
                register(mEmail, newPassword, true)
            }
            else -> {
                register(mUsername, newPassword)
            }
        }
    }

    private val newPasswordAgain: String
        get() = new_password_again_et.text?.toString() ?: ""

    private val newPassword: String
        get() = new_password_et.text?.toString() ?: ""

    private fun register(username: String?, password: String?, sendVerifyEmail: Boolean = false) {
        mStatefulLayout?.showLoading()
        UserDao.register(username, password, object : LogInListener<_User>() {
            override fun done(o: _User?, e: BmobException?) {
                mStatefulLayout?.showContent()
                val userRegister = UserRegister()
                if (e == null) {
                    userRegister.code = 200
                    userRegister.msg = "注册成功"
                    if (sendVerifyEmail) {
                        sendVerifyEmail(username)
                    }
                } else {
                    userRegister.code = e.errorCode
                    userRegister.msg = e.message
                }
                userRegisterSuccess(userRegister)
            }
        })
    }

    private fun sendVerifyEmail(email: String?) {
        mStatefulLayout?.showLoading()
        UserDao.requestEmailVerify(email, object : UpdateListener() {
            override fun done(e: BmobException?) {
                mStatefulLayout?.showContent()
                val userRegister = UserRegister()
                userRegister.data = email
                if (e == null) {
                    userRegister.code = 200
                    userRegister.msg = "请到邮箱激活账号"
                } else {
                    userRegister.code = e.errorCode
                    userRegister.msg = e.toString()
                }
                userRegisterSuccess(userRegister)
            }
        })
    }

    private fun userRegisterSuccess(userRegister: UserRegister) {
        ToastUtil.ok(userRegister.msg)
        if (userRegister.code == 200) {
            finish()
        }
    }

    private fun pleaseVerifyEmail(userRegister: UserRegister) {
        ToastUtil.w_long(userRegister.msg)
    }
}