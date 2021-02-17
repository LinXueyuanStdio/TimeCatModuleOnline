package com.timecat.module.user.game.item

import android.view.Menu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.adapter.game.ItemItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.timecat.module.user.game.bag.vm.ItemViewModel
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/9
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllItemActivity)
class AllItemActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "物品"
    override fun query() = allItem()
    override fun block2Item(block: Block) = ItemItem(this, block)
    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 4)
    }

    override var pageSize: Int = 100

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        menu?.apply {
            add("物产").setOnMenuItemClickListener {
                NAV.go(RouterHub.USER_ThingItemEditorActivity)
                true
            }
            add("数据").setOnMenuItemClickListener {
                NAV.go(RouterHub.USER_DataItemEditorActivity)
                true
            }
            add("方块").setOnMenuItemClickListener {
                NAV.go(RouterHub.USER_CubeItemEditorActivity)
                true
            }
            add("装备").setOnMenuItemClickListener {
                NAV.go(RouterHub.USER_EquipItemEditorActivity)
                true
            }
            add("礼包").setOnMenuItemClickListener {
                NAV.go(RouterHub.USER_PackageItemEditorActivity)
                true
            }
            add("buff").setOnMenuItemClickListener {
                NAV.go(RouterHub.USER_BuffItemEditorActivity)
                true
            }
        }
        return super.onCreateOptionsMenu(menu)
    }
}