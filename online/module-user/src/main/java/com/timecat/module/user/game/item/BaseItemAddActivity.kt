package com.timecat.module.user.game.item

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Item
import com.timecat.data.bmob.ext.create
import com.timecat.identity.data.block.ItemBlock
import com.timecat.module.user.base.BaseBlockEditorActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
abstract class BaseItemAddActivity : BaseBlockEditorActivity() {
    abstract fun getItemBlock(): ItemBlock

    override fun savableBlock(): Block = I() create Item {
        title = formData.title
        content = formData.content
        subtype = subtype()
        headerBlock = getItemBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
        subtype = subtype()
        structure = getItemBlock().toJson()
    }
}