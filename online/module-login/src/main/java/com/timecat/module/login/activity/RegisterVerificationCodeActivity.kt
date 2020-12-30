package com.timecat.module.login.activity

import android.text.TextUtils
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.page.base.view.MyClickListener
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.login.R
import com.timecat.module.login.data.SmsSend
import kotlinx.android.synthetic.main.login_activity_register_verification_code.*

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

    private val code: String
        get() = code_et.text?.toString() ?: ""

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
        BmobSMS.requestSMSCode(phone, "timecatSMS", object : QueryListener<Int>() {
            override fun done(smsId: Int, e: BmobException?) {
                mStatefulLayout?.showContent()
                val smsSend = SmsSend()
                if (e == null) {
                    smsSend.code = 200
                    smsSend.msg = "发送验证码成功!"
                } else {
                    smsSend.code = e.errorCode
                    smsSend.msg = "发送验证码失败：${e.errorCode}-${e.message}"
                }
                smsSendSuccess(smsSend)
            }
        })
    }

    private fun smsSendSuccess(smsSend: SmsSend) {
        ToastUtil.ok(smsSend.msg)
    }
}