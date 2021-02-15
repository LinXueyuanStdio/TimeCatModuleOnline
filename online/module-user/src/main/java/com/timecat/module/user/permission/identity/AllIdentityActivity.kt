package com.timecat.module.user.permission.identity

import android.view.Menu
import android.view.MenuItem
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allIdentity
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.adapter.block.IdentityItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.timecat.module.user.base.GO
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 方块
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllIdentityActivity)
class AllIdentityActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "方块"
    override fun query() = allIdentity()
    override fun block2Item(block: Block) = IdentityItem(this, block)

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