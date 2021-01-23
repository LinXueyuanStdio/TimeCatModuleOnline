package com.timecat.module.login.activity

import android.text.TextUtils
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import com.timecat.component.commonsdk.utils.string.Check
import com.timecat.component.commonsdk.utils.string.StringUtil
import com.timecat.component.router.app.ForwardCallback
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.EasyRequestUserNull
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.login.R
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.impl.RouterErrorResult
import com.xiaojinzi.component.impl.RouterRequest
import com.xiaojinzi.component.impl.RouterResult

/**
 * 注册
 * 在这里填用户名、邮箱、手机号
 * 填手机号则进入验证码页面，填邮箱会发激活邮件，然后直接到设置密码页面
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_RegisterCheckExistActivity)
class RegisterCheckExistActivity : BaseToolbarActivity() {
    override fun title(): String = getString(R.string.xiaoxing_login_register)
    override fun layout(): Int = R.layout.login_activity_register_send_phone
    override fun routerInject() = NAV.inject(this)

    lateinit var username_et: EditText
    lateinit var btn_next: View
    lateinit var tv_xie_yi: View
    lateinit var cb: CheckBox

    override fun bindView() {
        super.bindView()
        username_et = findViewById(R.id.username_et)
        btn_next = findViewById(R.id.btn_next)
        tv_xie_yi = findViewById(R.id.tv_xie_yi)
        cb = findViewById(R.id.cb)
    }

    override fun initView() {
        btn_next.setShakelessClickListener {
            next()
        }
        tv_xie_yi.setShakelessClickListener {
            NAV.go(this@RegisterCheckExistActivity, RouterHub.LOGIN_ProtocolActivity)
        }
    }

    operator fun next() {
        val phoneOrEmail = textFromEditText
        if (TextUtils.isEmpty(phoneOrEmail)) {
            ToastUtil.w("不能为空")
            return
        }
        if (!cb.isChecked) {
            ToastUtil.w("请先阅读协议")
            return
        }
        when {
            StringUtil.isEmail(phoneOrEmail) -> userCheckEmail(phoneOrEmail)
            Check.isMobileNO(phoneOrEmail) -> userCheckphone(phoneOrEmail)
            else -> userCheckUsername(phoneOrEmail)
        }
    }

    private val textFromEditText: String
        get() = username_et.text?.toString() ?: ""

    private fun userCheckEmail(email: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryUsersExits(email, EasyRequestUserNull().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.message}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                if (it == null) {
                    ToastUtil.ok("允许使用")
                    NAV.raw(this@RegisterCheckExistActivity, RouterHub.LOGIN_RegisterSetPwdActivity)
                        .withString("type", EMAIL)
                        .withString("mEmail", email)
                        .forward(object : ForwardCallback {
                            override fun onError(errorResult: RouterErrorResult) {
                            }

                            override fun onCancel(originalRequest: RouterRequest?) {
                            }

                            override fun onSuccess(result: RouterResult) {
                                finish()
                            }

                            override fun onEvent(successResult: RouterResult?, errorResult: RouterErrorResult?) {
                            }
                        })
                } else {
                    ToastUtil.w_long("邮箱已被使用")
                }
            }
        })
    }

    private fun userCheckphone(phone: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryUsersExits(phone, EasyRequestUserNull().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.message}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                if (it == null) {
                    ToastUtil.ok("允许使用")
                    NAV.goAndFinish(this@RegisterCheckExistActivity, RouterHub.LOGIN_RegisterVerificationCodeActivity, "mPhone", phone)
                } else {
                    ToastUtil.w_long("号码已被使用")
                }
            }
        })
    }

    private fun userCheckUsername(username: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryUsersExits(username, EasyRequestUserNull().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.message}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                if (it == null) {
                    ToastUtil.ok("允许使用")
                    NAV.raw(this@RegisterCheckExistActivity, RouterHub.LOGIN_RegisterSetPwdActivity)
                        .withString("type", USERNAME)
                        .withString("mUsername", textFromEditText)
                        .forward(object : ForwardCallback {
                            override fun onError(errorResult: RouterErrorResult) {
                            }

                            override fun onCancel(originalRequest: RouterRequest?) {
                            }

                            override fun onSuccess(result: RouterResult) {
                                finish()
                            }

                            override fun onEvent(successResult: RouterResult?, errorResult: RouterErrorResult?) {
                            }
                        })
                } else {
                    ToastUtil.w_long("用户名已被使用")
                }
            }
        })
    }

    companion object {
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val USERNAME = "username"
    }
}