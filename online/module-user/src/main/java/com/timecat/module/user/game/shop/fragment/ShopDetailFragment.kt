package com.timecat.module.user.game.shop.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.Pay
import com.timecat.data.bmob.ext.bmob.requestPay
import com.timecat.data.bmob.ext.game.pay
import com.timecat.data.bmob.ext.net.paysInShopOf
import com.timecat.identity.data.block.BasicShopBlock
import com.timecat.identity.data.block.ItemBlock
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
        shopViewModel.stopAllTask()
        loadPays(block) {
            loadWithShopAndPays(block, it)
        }
    }

    fun loadWithShopAndPays(shop: Block, pays:List<Pay>) {
        val list = mutableListOf<BaseItem<*>>()
        val header = ShopBlock.fromJson(shop.structure)
        LogUtil.se(shop)
        val g = pays.groupBy { it.good.objectId }.mapValues { it.value.sumBy { it.pay } }
        if (shop.subtype == SHOP_Basic) {
            val h2 = BasicShopBlock.fromJson(header.structure)
            LogUtil.se("h2 : $h2")
            gameService.itemContext(viewLifecycleOwner, I(), { onPrepareContent() }) { itemContext ->
                val money: Block = itemContext.itemsMap[h2.moneyId] ?: return@itemContext
                shopViewModel.money.postValue(money)
                val haveMoney: Int = itemContext.ownItemsMap[h2.moneyId] ?: 0
                shopViewModel.haveMoney.postValue(haveMoney)
                val moneyIcon = ItemBlock.fromJson(money.structure).header.icon
                //从物品缓存中将id映射成block
                val goods = mutableListOf<GoodItem>()
                for (good in h2.goods) {
                    val item = itemContext.itemsMap[good.itemId] ?: continue
                    val paid = g[good.itemId] ?: 0
                    val goodBlock = GoodBlock(moneyIcon, item, good.value.toInt(), good.max, paid)
                    val goodItem = GoodItem(requireActivity(), goodBlock) {
                        buy(goodBlock)
                    }
                    goods.add(goodItem)
                }
                list.addAll(goods)
                adapter.reload(list)
            }
        }
    }

    fun loadPays(shop: Block, load:(List<Pay>)->Unit) {
        shopViewModel attach requestPay {
            query = paysInShopOf(I(), shop)
            onSuccess = {
                load(it)
            }
            onError = {
                mStatefulLayout?.showError("出错啦") {
                    loadPays(shop, load)
                }
            }
        }
    }

    fun buy(goodBlock: GoodBlock) {
        requireActivity().showBuyItemDialog(goodBlock.item, goodBlock.value, goodBlock.max - goodBlock.paid)
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 4)
    }

}