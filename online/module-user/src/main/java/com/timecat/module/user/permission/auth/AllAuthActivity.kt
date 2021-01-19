package com.timecat.module.user.permission.auth

import android.view.Menu
import android.view.MenuItem
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allAuthToSomeone
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseInterActionListActivity
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 授权
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllAuthActivity)
class AllAuthActivity : BaseInterActionListActivity() {
    override fun title(): String = "授权"
    override fun query() = I().allAuthToSomeone()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                GO.addIdentity()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}