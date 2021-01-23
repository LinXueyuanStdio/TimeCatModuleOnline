package com.timecat.module.user.permission

import android.view.Menu
import android.view.MenuItem
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allHunPermission
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.timecat.module.user.base.GO
import com.xiaojinzi.component.anno.RouterAnno

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
    override fun block2Item(block: Block) = BlockSmallItem(this, block)

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