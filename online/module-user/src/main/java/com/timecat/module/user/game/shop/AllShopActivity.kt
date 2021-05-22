package com.timecat.module.user.game.shop

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allShop
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.game.ShopItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/23
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllShopActivity)
class AllShopActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "商店"
    override fun query() = allShop()
    override fun block2Item(block: Block) = ShopItem(this, block)
    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }
}