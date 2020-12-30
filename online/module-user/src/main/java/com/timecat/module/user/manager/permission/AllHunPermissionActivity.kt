package com.timecat.module.user.manager.permission

import android.view.Menu
import android.view.MenuItem
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.ext.net.allHunPermission
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 混权限
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllHunPermissionActivity)
class AllHunPermissionActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "混权限"

    override fun query() = allHunPermission()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                GO.addHunPermission()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}