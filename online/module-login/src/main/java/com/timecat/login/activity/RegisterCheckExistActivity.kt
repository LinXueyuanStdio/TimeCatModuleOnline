package com.timecat.login.activity

import android.text.TextUtils
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.commonsdk.utils.string.Check
import com.timecat.component.commonsdk.utils.string.StringUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.login.R
import com.timecat.login.data.UserCheckExist
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.login_activity_register_send_phone.*

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

    private fun generate(
        list: List<_User>?, e: BmobException?, data: String,
        onAllow: String, onUsed: String
    ): UserCheckExist {
        val userCheckExistEmail = UserCheckExist()
        userCheckExistEmail.data = data
        if (e != null) {
            userCheckExistEmail.code = e.errorCode
            userCheckExistEmail.msg = e.toString()
        } else if (list == null || list.isEmpty()) {
            userCheckExistEmail.code = 200
            userCheckExistEmail.msg = onAllow
        } else {
            LogUtil.e(list.toString())
            userCheckExistEmail.code = 404
            userCheckExistEmail.msg = onUsed
        }
        return userCheckExistEmail
    }

    private fun userCheckEmail(email: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryEmail(email, 1, object : FindListener<_User>() {
            override fun done(list: List<_User>?, e: BmobException?) {
                mStatefulLayout?.showContent()
                val msg = generate(list, e, email, "允许使用", "邮箱已被使用")
                userCheckEmailSuccess(msg)
            }
        })
    }

    private fun userCheckphone(phone: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryPhone(phone, 1, object : FindListener<_User>() {
            override fun done(list: List<_User>?, e: BmobException?) {
                mStatefulLayout?.showContent()
                val msg = generate(list, e, phone, "允许使用", "号码已被使用")
                userCheckPhoneSuccess(msg)
            }
        })
    }

    private fun userCheckUsername(username: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryUsersExits(username, 1, object : FindListener<_User>() {
            override fun done(list: List<_User>?, e: BmobException?) {
                mStatefulLayout?.showContent()
                val msg = generate(list, e, username, "允许使用", "用户名已被使用")
                userCheckUsernameSuccess(msg)
            }
        })
    }

    private fun userCheckUsernameSuccess(userCheckPhoneExist: UserCheckExist) {
        LogUtil.e(userCheckPhoneExist.toString())
        if (userCheckPhoneExist.code == 200) {
            ToastUtil.ok(userCheckPhoneExist.msg)
            NAV.raw(this, RouterHub.LOGIN_RegisterSetPwdActivity)
                .withString("type", USERNAME)
                .withString("mUsername", textFromEditText)
                .navigation()
            finish()
        } else {
            ToastUtil.w(userCheckPhoneExist.msg)
        }
    }

    private fun userCheckPhoneSuccess(userCheckPhoneExist: UserCheckExist) {
        LogUtil.e(userCheckPhoneExist.toString())
        if (userCheckPhoneExist.code == 200) {
            ToastUtil.ok(userCheckPhoneExist.msg)
            NAV.goAndFinish(this, RouterHub.LOGIN_RegisterVerificationCodeActivity, "mPhone", userCheckPhoneExist.data)
        } else {
            ToastUtil.w(userCheckPhoneExist.msg)
        }
    }

    private fun userCheckEmailSuccess(userCheckExistEmail: UserCheckExist) {
        LogUtil.e(userCheckExistEmail.toString())
        //        if (userCheckExistEmail.getCode() == 200) {
//            mPresenter.sendVerifyEmail(userCheckExistEmail.getData());
//        }
        ToastUtil.w(userCheckExistEmail.msg)
        if (userCheckExistEmail.code == 200) {
            NAV.raw(this, RouterHub.LOGIN_RegisterSetPwdActivity)
                .withString("type", EMAIL)
                .withString("mEmail", userCheckExistEmail.data)
                .navigation()
            finish()
        }
    }

    private fun userSendVerifyEmailSuccess(userCheckExistEmail: UserCheckExist) {
        ToastUtil.w(userCheckExistEmail.msg)
        if (userCheckExistEmail.code == 200) {
            NAV.raw(this, RouterHub.LOGIN_RegisterSetPwdActivity)
                .withString("type", EMAIL)
                .withString("mEmail", userCheckExistEmail.data)
                .navigation()
            finish()
        }
    }


    companion object {
        const val PHONE = "phone"
        const val EMAIL = "email"
        const val USERNAME = "username"
    }
}