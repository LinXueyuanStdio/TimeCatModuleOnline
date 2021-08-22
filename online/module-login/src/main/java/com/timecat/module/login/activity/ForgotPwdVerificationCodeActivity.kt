package com.timecat.module.login.activity

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.EasyRequest
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.keyboardManager.InputMethodUtils
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.login.R
import com.timecat.module.login.view.CountDownButton
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * 忘记密码，通过手机验证码重置
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_ForgotPwdVerificationCodeActivity)
class ForgotPwdVerificationCodeActivity : BaseToolbarActivity() {

    @AttrValueAutowiredAnno("mPhone")
    var mPhone: String? = null

    private val code: String
        get() = code_et.text?.toString() ?: ""

    override fun title(): String = getString(R.string.xiaoxing_login_verification)
    override fun layout(): Int = R.layout.login_activity_forgot_pwd_verification_code
    override fun routerInject() = NAV.inject(this)

    lateinit var code_et: EditText
    lateinit var btn_next: View
    lateinit var cd_btn: CountDownButton
    lateinit var tv_phone: TextView
    override fun bindView() {
        super.bindView()
        code_et = findViewById(R.id.code_et)
        btn_next = findViewById(R.id.btn_next)
        tv_phone = findViewById(R.id.tv_phone)
        cd_btn = findViewById(R.id.cd_btn)
    }

    override fun initView() {
        tv_phone.text = mPhone
        btn_next.setShakelessClickListener {
            if (TextUtils.isEmpty(code)) {
                ToastUtil.w("验证码不能为空")
                return@setShakelessClickListener
            }
            NAV.raw(RouterHub.LOGIN_ForgotPwdSetNewPwdActivity)
                .putString("mPhone", mPhone)
                .putString("mCode", code)
                .navigation()
            finish()
        }
        cd_btn.setShakelessClickListener {
            sendAndCountdown()
        }
        sendAndCountdown()
    }

    private fun sendAndCountdown() {
        InputMethodUtils.showInputMethod(code_et, 50)
        smsSend(mPhone!!)
        cd_btn.startCountdown()
    }

    private fun smsSend(phone: String) {
        mStatefulLayout?.showLoading()
        UserDao.requestPasswordResetBySmsCode(phone, EasyRequest().apply {
            onSuccess = {
                mStatefulLayout?.showContent()
                ToastUtil.ok("发送验证码成功!")
            }
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("发送验证码失败：${it.message}")
            }
        })
    }
}