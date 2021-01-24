package com.timecat.module.login.activity

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.EasyRequest
import com.timecat.data.bmob.ext.bmob.EasyRequestUser
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.keyboardManager.InputMethodUtils
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.login.R
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

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

    lateinit var btn_ok: View
    lateinit var new_password_again_et: EditText
    lateinit var new_password_et: EditText
    override fun bindView() {
        super.bindView()
        btn_ok = findViewById(R.id.btn_ok)
        new_password_again_et = findViewById(R.id.new_password_again_et)
        new_password_et = findViewById(R.id.new_password_et)
    }

    override fun initView() {
        btn_ok.setShakelessClickListener {
            toRegister()
        }
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
        InputMethodUtils.hideKeyboard(new_password_again_et)
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
        UserDao.register(username, password, EasyRequestUser().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.message}")
            }
            onSuccess = {
                ToastUtil.ok("注册成功")
                if (sendVerifyEmail) {
                    sendVerifyEmail(username)
                } else {
                    mStatefulLayout?.showContent()
                    finish()
                }
            }
        })
    }

    private fun sendVerifyEmail(email: String?) {
        mStatefulLayout?.showLoading()
        UserDao.requestEmailVerify(email, EasyRequest().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.message}")
                finish()
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                ToastUtil.ok("请到邮箱激活账号")
                finish()
            }
        })
    }

}