package com.timecat.module.user.game.illustration.cube

import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.base.BaseBlockFragment
import com.timecat.module.user.view.dsl.Content
import com.timecat.module.user.view.item.ContentItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description
 * 简单的介绍
 * @usage null
 */
class IdentityAttrFragment : BaseBlockFragment() {
    lateinit var contentItem: ContentItem

    override fun loadDetail(block: Block) {
        contentItem.setRichText(block.content)
    }

    override fun initViewAfterLogin() {
        container.apply {
            contentItem = Content("")
        }
        super.initViewAfterLogin()
    }
}