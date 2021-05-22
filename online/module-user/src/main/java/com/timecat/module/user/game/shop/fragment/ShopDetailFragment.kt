package com.timecat.module.user.game.shop.fragment

import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.BasicShopBlock
import com.timecat.identity.data.block.ShopBlock
import com.timecat.identity.data.block.type.SHOP_Basic
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.game.GoodBlock
import com.timecat.module.user.adapter.game.GoodItem
import com.timecat.module.user.game.item.showBuyItemDialog
import com.timecat.module.user.game.task.rule.GameService
import com.timecat.module.user.social.common.BaseBlockDetailFragment
import com.xiaojinzi.component.anno.ServiceAutowiredAnno

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

    @ServiceAutowiredAnno
    lateinit var gameService: GameService
    lateinit var shopViewModel: ShopViewModel

    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        shopViewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
    }

    override fun loadDetail(block: Block) {
        val list = mutableListOf<BaseItem<*>>()
        val header = ShopBlock.fromJson(block.structure)
        LogUtil.se(block)
        if (block.subtype == SHOP_Basic) {
            val h2 = BasicShopBlock.fromJson(header.structure)
            gameService.itemContext(viewLifecycleOwner, I(), { onPrepareContent() }) { itemContext ->
                LogUtil.se("load item context success")
                val money: Block = itemContext.itemsMap[h2.moneyId] ?: return@itemContext
                shopViewModel.money.postValue(money)
                val haveMoney: Int = itemContext.ownItemsMap[h2.moneyId] ?: 0
                shopViewModel.haveMoney.postValue(haveMoney)
                //从物品缓存中将id映射成block
                val goods = mutableListOf<GoodItem>()
                for (good in h2.goods) {
                    val item = itemContext.itemsMap[good.itemId] ?: continue
                    val goodBlock = GoodBlock(item, good.value.toInt(), good.max)
                    val goodItem = GoodItem(requireActivity(), goodBlock) {
                        buy(goodBlock)
                    }
                    goods.add(goodItem)
                }
                list.addAll(goods)
            }
        }
        adapter.reload(list)
    }

    fun buy(goodBlock: GoodBlock) {
        requireActivity().showBuyItemDialog(goodBlock.item, goodBlock.value, goodBlock.max)
    }

}