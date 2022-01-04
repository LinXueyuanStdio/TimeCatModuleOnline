package com.timecat.module.user.game.illustration.item

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.readonly.item.ItemItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/9
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllItemIllustratedActivity)
class AllItemIllustratedActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "物品"
    override fun query() = allItem()
    override fun block2Item(block: Block) = ItemItem(this, block)
    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 4)
    }

    override var pageSize: Int = 100
}