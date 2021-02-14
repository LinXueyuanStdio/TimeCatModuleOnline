package com.timecat.module.user.game.task.task

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Task
import com.timecat.data.bmob.ext.create
import com.timecat.identity.data.block.TaskBlock
import com.timecat.module.user.base.BaseBlockEditorActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
abstract class BaseTaskAddActivity : BaseBlockEditorActivity() {
    override fun savableBlock(): Block = I() create Task {
        title = formData.title
        content = formData.content
        subtype = subtype()
        headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
        subtype = subtype()
        structure = getHeadBlock().toJson()
    }

    abstract fun getHeadBlock(): TaskBlock
}