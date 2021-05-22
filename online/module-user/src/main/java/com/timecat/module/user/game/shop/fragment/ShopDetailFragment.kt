package com.timecat.module.user.game.shop.fragment

import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.BasicShopBlock
import com.timecat.identity.data.block.ShopBlock
import com.timecat.identity.data.block.type.SHOP_Basic
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.adapter.game.GoodItem
import com.timecat.module.user.social.common.BaseBlockDetailFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 商店的详细信息
 * 1. 简要介绍商店（content、截止日期等等）
 * 2. 有什么商品
 * @usage null
 */
class ShopDetailFragment : BaseBlockDetailFragment() {

    override fun loadDetail(block: Block) {
        val list = mutableListOf<BaseItem<*>>()
        val header = ShopBlock.fromJson(block.structure)
        if (block.subtype == SHOP_Basic) {
            val h2 = BasicShopBlock.fromJson(header.structure)
            //从物品缓存中将id映射成block
//            h2.goods.map{
//                GoodItem()
//            }
        }
        adapter.reload(list)
    }
}