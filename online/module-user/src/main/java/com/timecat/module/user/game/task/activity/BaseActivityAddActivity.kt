package com.timecat.module.user.game.task.activity

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Activity
import com.timecat.data.bmob.ext.create
import com.timecat.identity.data.block.ActivityBlock
import com.timecat.module.user.base.BaseBlockEditorActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
abstract class BaseActivityAddActivity : BaseBlockEditorActivity() {
    abstract fun getItemBlock(): ActivityBlock

    override fun savableBlock(): Block = I() create Activity {
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