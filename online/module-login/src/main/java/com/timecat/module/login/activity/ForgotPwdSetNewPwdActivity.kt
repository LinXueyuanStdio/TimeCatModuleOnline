package com.timecat.module.login.activity

import android.text.TextUtils
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.data._User
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.page.base.view.MyClickListener
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.login.R
import com.timecat.module.login.data.UserSetPassword
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import kotlinx.android.synthetic.main.login_activity_set_new_password.*

/**
 * 设置密码
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_ForgotPwdSetNewPwdActivity)
class ForgotPwdSetNewPwdActivity : BaseToolbarActivity() {
    @AttrValueAutowiredAnno("mPhone")
    @JvmField
    var mPhone: String? = null

    @AttrValueAutowiredAnno("mCode")
    @JvmField
    var mCode: String? = null

    override fun title(): String = getString(R.string.xiaoxing_login_set_pwd)
    override fun layout(): Int = R.layout.login_activity_set_new_password
    override fun routerInject() = NAV.inject(this)

    override fun initView() {
        btn_ok.setOnClickListener(MyClickListener {
            if (TextUtils.isEmpty(newPassword)) {
                ToastUtil.w("新密码不能为空")
                return@MyClickListener
            }
            if (TextUtils.isEmpty(newPasswordAgain)) {
                ToastUtil.w("确认密码不能为空")
                return@MyClickListener
            }
            if (newPassword != newPasswordAgain) {
                ToastUtil.w("两次密码输入的不一致，请重新输入")
                return@MyClickListener
            }
            userSetPasswordByPhone(mCode!!, newPassword)
        })

    }

    private fun userSetPasswordByPhone(smsCode: String, new_password: String) {
        mStatefulLayout?.showLoading()
        _User.resetPasswordBySMSCode(smsCode, new_password, object : UpdateListener() {
            override fun done(e: BmobException?) {
                mStatefulLayout?.showContent()
                val userSetpassword =
                    UserSetPassword()
                if (e == null) {
                    userSetpassword.msg = "重置成功"
                    userSetpassword.code = 200
                } else {
                    userSetpassword.code = e.errorCode
                    userSetpassword.msg = "重置失败：" + e.errorCode + "-" + e.message
                }
                userSetPasswordSuccess(userSetpassword)
            }
        })
    }

    private val newPasswordAgain: String
        get() = new_password_again_et.text?.toString() ?: ""

    private val newPassword: String
        get() = new_password_et.text?.toString() ?: ""

    private fun userSetPasswordSuccess(userSetPassword: UserSetPassword) {
        ToastUtil.w(userSetPassword.msg)
        if (userSetPassword.code == 200) {
            finish()
        }
    }
}