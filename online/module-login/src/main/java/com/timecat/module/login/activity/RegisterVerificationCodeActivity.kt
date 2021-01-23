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
import com.timecat.module.login.R
import com.timecat.module.login.view.CountDownButton
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.page.base.view.MyClickListener
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * 注册第二步：手机验证码
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_RegisterVerificationCodeActivity)
class RegisterVerificationCodeActivity : BaseToolbarActivity() {
    @AttrValueAutowiredAnno("mPhone")
    @JvmField
    var mPhone: String? = null

    override fun title(): String = getString(R.string.xiaoxing_login_verification)
    override fun layout(): Int = R.layout.login_activity_register_verification_code
    override fun routerInject() = NAV.inject(this)
    lateinit var code_et: EditText
    lateinit var btn_next: View
    lateinit var cd_btn: CountDownButton
    lateinit var tv_phone: TextView

    private val code: String
        get() = code_et.text?.toString() ?: ""

    override fun bindView() {
        super.bindView()
        code_et = findViewById(R.id.code_et)
        btn_next = findViewById(R.id.btn_next)
        tv_phone = findViewById(R.id.tv_phone)
        cd_btn = findViewById(R.id.cd_btn)
    }

    override fun initView() {
        tv_phone.text = mPhone
        btn_next.setOnClickListener(MyClickListener {
            if (TextUtils.isEmpty(code)) {
                ToastUtil.w("验证码不能为空")
                return@MyClickListener
            }
            NAV.raw(this, RouterHub.LOGIN_RegisterSetPwdActivity)
                .withString("mPhone", mPhone)
                .withString("type", "phone")
                .withString("mCode", code)
            finish()
        })
        cd_btn.setOnClickListener(MyClickListener {
            sendAndCountdown()
        })
        sendAndCountdown()
    }

    private fun sendAndCountdown() {
        smsSend(mPhone!!)
        cd_btn.startCountdown()
    }

    private fun smsSend(phone: String) {
        mStatefulLayout?.showLoading()
        UserDao.requestSmsCode(phone, EasyRequest().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.message}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                ToastUtil.ok("发送验证码成功")
            }
        })
    }
}