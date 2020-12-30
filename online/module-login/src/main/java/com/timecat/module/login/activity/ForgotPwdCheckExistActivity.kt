package com.timecat.module.login.activity

import android.text.TextUtils
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import cn.bmob.v3.listener.UpdateListener

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.page.base.view.MyClickListener
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.commonsdk.utils.string.Check
import com.timecat.component.commonsdk.utils.string.StringUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.login.R
import com.timecat.module.login.data.UserCheckExist
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.login_activity_forgot_pwd_send_phone.*

/**
 * 忘记密码
 * 在这里填邮箱、手机号，不支持用户名
 * 填手机号则进入下一页面，填邮箱则发送重置密码邮件
 */
@RouterAnno(hostAndPath = RouterHub.LOGIN_ForgotPwdCheckExistActivity)
class ForgotPwdCheckExistActivity : BaseToolbarActivity() {

    override fun title(): String = getString(R.string.xiaoxing_login_verification)
    override fun layout(): Int = R.layout.login_activity_forgot_pwd_send_phone
    override fun routerInject() = NAV.inject(this)

    override fun initView() {
        btn_next.setOnClickListener(MyClickListener {
            val phoneOrEmail = textFromEditText
            when {
                TextUtils.isEmpty(phoneOrEmail) -> ToastUtil.w("不能为空")
                StringUtil.isEmail(phoneOrEmail) -> userCheckEmail(phoneOrEmail)
                Check.isMobileNO(phoneOrEmail) -> userCheckPhone(phoneOrEmail)
                else -> ToastUtil.w("格式不正确:不是手机号也不是邮箱")
            }
        })
    }

    private val textFromEditText: String
        get() = username_et.text?.toString() ?: ""

    private fun resetPasswordByEmail(email: String) {
        mStatefulLayout?.showLoading()
        UserDao.resetPasswordByEmail(email, object : UpdateListener() {
            override fun done(e: BmobException?) {
                mStatefulLayout?.showContent()
                if (e == null) {
                    ToastUtil.ok("重置密码请求成功，请到" + email + "邮箱进行密码重置操作")
                } else {
                    ToastUtil.e_long("失败:" + e.message)
                }
            }
        })
    }

    private fun userCheckEmail(email: String?) {
        mStatefulLayout?.showLoading()
        UserDao.queryEmail(email, 1, object : FindListener<_User>() {
            override fun done(list: List<_User>?, e: BmobException?) {
                mStatefulLayout?.showContent()

                val userCheckExistEmail =
                    UserCheckExist()
                userCheckExistEmail.data = email
                if (e != null) {
                    userCheckExistEmail.code = e.errorCode
                    userCheckExistEmail.msg = e.toString()
                } else if (list == null || list.isEmpty()) {
                    userCheckExistEmail.code = 404
                    userCheckExistEmail.msg = "邮箱不存在！"
                } else {
                    userCheckExistEmail.code = 200
                    userCheckExistEmail.msg = "邮箱存在，可以重置密码"
                }
                userCheckEmailSuccess(userCheckExistEmail)
            }
        })
    }

    private fun userCheckPhone(phone: String?) {
        mStatefulLayout?.showLoading()
        UserDao.queryPhone(phone, 1, object : FindListener<_User>() {
            override fun done(list: List<_User>?, e: BmobException?) {
                mStatefulLayout?.showContent()
                val userCheckExistPhone = UserCheckExist()
                userCheckExistPhone.data = phone
                if (e != null) {
                    userCheckExistPhone.code = e.errorCode
                    userCheckExistPhone.msg = e.toString()
                } else if (list == null || list.isEmpty()) {
                    userCheckExistPhone.code = 404
                    userCheckExistPhone.msg = "此号码未绑定账号！"
                } else {
                    userCheckExistPhone.code = 200
                    userCheckExistPhone.msg = "号码存在，可以重置密码"
                }
                userCheckPhoneSuccess(userCheckExistPhone)
            }
        })
    }

    private fun userCheckEmailSuccess(userCheckExistEmail: UserCheckExist) {
        LogUtil.e(userCheckExistEmail.toString())
        if (userCheckExistEmail.code == 200) {
            resetPasswordByEmail(userCheckExistEmail.data)
        }
        ToastUtil.w(userCheckExistEmail.msg)
    }

    private fun userCheckPhoneSuccess(userCheckPhoneExist: UserCheckExist) {
        LogUtil.e(userCheckPhoneExist.toString())
        if (userCheckPhoneExist.code == 200) {
            NAV.goAndFinish(this, RouterHub.LOGIN_ForgotPwdVerificationCodeActivity, "mPhone", userCheckPhoneExist.data)
        } else {
            ToastUtil.w(userCheckPhoneExist.msg)
        }
    }
}