package com.timecat.module.login.activity

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.EasyRequest
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.keyboardManager.InputMethodUtils
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.login.R
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

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

    lateinit var new_password_again_et: EditText
    lateinit var new_password_et: EditText
    lateinit var btn_ok: View

    override fun title(): String = getString(R.string.xiaoxing_login_set_pwd)
    override fun layout(): Int = R.layout.login_activity_set_new_password
    override fun routerInject() = NAV.inject(this)
    override fun bindView() {
        super.bindView()
        new_password_again_et = findViewById(R.id.new_password_again_et)
        new_password_et = findViewById(R.id.new_password_et)
        btn_ok = findViewById(R.id.btn_ok)
    }

    override fun initView() {
        btn_ok.setShakelessClickListener {
            if (TextUtils.isEmpty(newPassword)) {
                ToastUtil.w("新密码不能为空")
                return@setShakelessClickListener
            }
            if (TextUtils.isEmpty(newPasswordAgain)) {
                ToastUtil.w("确认密码不能为空")
                return@setShakelessClickListener
            }
            if (newPassword != newPasswordAgain) {
                ToastUtil.w("两次密码输入的不一致，请重新输入")
                return@setShakelessClickListener
            }
            userSetPasswordByPhone(mCode!!, newPassword)
        }
    }

    private fun userSetPasswordByPhone(smsCode: String, new_password: String) {
        InputMethodUtils.hideKeyboard(new_password_again_et)
        mStatefulLayout?.showLoading()
        UserDao.resetPasswordBySmsCode(smsCode, new_password, EasyRequest().apply {
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.w("重置失败：${it.msg}")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                finish()
            }
        })
    }

    private val newPasswordAgain: String
        get() = new_password_again_et.text?.toString() ?: ""

    private val newPassword: String
        get() = new_password_et.text?.toString() ?: ""
}