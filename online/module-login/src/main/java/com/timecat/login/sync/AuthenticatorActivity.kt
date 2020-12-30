package com.timecat.login.sync

import android.accounts.Account
import android.accounts.AccountAuthenticatorActivity
import android.accounts.AccountManager
import android.content.ContentResolver
import android.os.Bundle
import android.view.View
import android.widget.TextView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.LogInListener
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.login.R
import kotlinx.android.synthetic.main.login_activity_authenticator.*

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/5/8
 * @description 手机系统内置的同步系统
 * @usage null
 */
class AuthenticatorActivity : AccountAuthenticatorActivity() {
    private var mAccountManager: AccountManager? = null

    /**
     * Called when the activity is first created.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity_authenticator)
        mAccountManager = AccountManager.get(baseContext)
        val accountName = intent.getStringExtra(ARG_ACCOUNT_NAME)
        if (accountName != null) {
            (findViewById<View>(R.id.accountName) as TextView).text = accountName
        }
        findViewById<View>(R.id.submit).setOnClickListener { submit() }
    }

    fun submit() {
        val userName = accountName.text?.toString()?:""
        val userPass = accountPassword?.text?.toString()?:""
        UserDao.login(userName, userPass, object : LogInListener<_User>() {
                override fun done(s: _User?, e: BmobException?) {
                    if (e == null) {
                        ToastUtil.ok("登录成功！")
                        finishLogin(userName, userPass)
                    } else {
                        LogUtil.e(e.toString())
                        ToastUtil.e("登录失败！")
                    }
                }
            })
    }

    private fun finishLogin(accountName: String, accountPassword: String) {
        val account = Account(accountName, AccountConstants.ACCOUNT_TYPE)

        // Creating the account on the device and setting the auth token we got
        // (Not setting the auth token will cause another call to the server to authenticate the user)
        mAccountManager!!.addAccountExplicitly(account, accountPassword, null)
        mAccountManager!!.setPassword(account, accountPassword)
        //可以同步
        ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1)
        //可以自动同步
        ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true)

        // 自动同步间隔时间为30秒
        val bundle = Bundle()
        ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY, bundle, 30)

        // 手动同步
        ContentResolver.requestSync(account, AccountProvider.AUTHORITY, bundle)
        finish()
    }

    companion object {
        const val ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE"
        const val ARG_AUTH_TYPE = "AUTH_TYPE"
        const val ARG_ACCOUNT_NAME = "ACCOUNT_NAME"
        const val ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT"
        const val PARAM_USER_PASS = "USER_PASS"
    }
}