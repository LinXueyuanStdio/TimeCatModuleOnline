package com.timecat.module.user.base.login

import androidx.fragment.app.Fragment
import com.timecat.page.base.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/24
 * @description null
 * @usage null
 */
abstract class BaseLoginToolbarFragmentActivity : BaseLoginToolbarActivity() {

    protected abstract fun createFragment(): Fragment

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        val fm = this.supportFragmentManager
        var fragment = fm.findFragmentById(R.id.container)
        if (fragment == null) {
            fragment = createFragment()
            fm.beginTransaction().add(R.id.container, fragment).commit()
        }
    }
}