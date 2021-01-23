package com.timecat.module.login.activity

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.timecat.component.commonsdk.utils.string.Check
import com.timecat.component.commonsdk.utils.string.StringUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.EasyRequest
import com.timecat.data.bmob.ext.bmob.EasyRequestUserNull
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.login.R
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.xiaojinzi.component.anno.RouterAnno

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

    lateinit var btn_next: View
    lateinit var username_et: EditText
    override fun bindView() {
        super.bindView()
        btn_next = findViewById(R.id.btn_next)
        username_et = findViewById(R.id.username_et)
    }

    override fun initView() {
        btn_next.setShakelessClickListener {
            val phoneOrEmail = textFromEditText
            when {
                TextUtils.isEmpty(phoneOrEmail) -> ToastUtil.w("不能为空")
                StringUtil.isEmail(phoneOrEmail) -> userCheckEmail(phoneOrEmail)
                Check.isMobileNO(phoneOrEmail) -> userCheckPhone(phoneOrEmail)
                else -> ToastUtil.w("格式不正确:不是手机号也不是邮箱")
            }
        }
    }

    private val textFromEditText: String
        get() = username_et.text?.toString() ?: ""

    private fun resetPasswordByEmail(email: String) {
        mStatefulLayout?.showLoading()

        UserDao.requestPasswordResetByEmail(email, EasyRequest().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("失败:" + it.message)
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                ToastUtil.ok("重置密码请求成功，请到" + email + "邮箱进行密码重置操作")
            }
        })
    }

    private fun userCheckEmail(email: String) {
        mStatefulLayout?.showLoading()
        UserDao.queryUsersExits(email, EasyRequestUserNull().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.msg}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                if (it == null) {
                    ToastUtil.w_long("邮箱不存在！")
                } else {
                    resetPasswordByEmail(email)
                    ToastUtil.ok("邮箱存在，可以重置密码")
                }
            }
        })
    }

    private fun userCheckPhone(phone: String?) {
        mStatefulLayout?.showLoading()
        UserDao.queryUsersExits(phone, EasyRequestUserNull().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long("${it.msg}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                if (it == null) {
                    ToastUtil.w_long("此号码未绑定账号!")
                } else {
                    NAV.goAndFinish(this@ForgotPwdCheckExistActivity, RouterHub.LOGIN_ForgotPwdVerificationCodeActivity, "mPhone", phone)
                    ToastUtil.ok("号码存在，可以重置密码")
                }
            }
        })
    }
}