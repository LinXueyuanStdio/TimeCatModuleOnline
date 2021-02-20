package com.timecat.module.user.game.shop.fragment

import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
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
//        list.add(SingleAuthorItem(forum.user))TODO 不要显示创建着
        list.add(SimpleContentItem(requireActivity(), block.content))
        list.add(ActionItem(block))
        adapter.reload(list)
    }
}