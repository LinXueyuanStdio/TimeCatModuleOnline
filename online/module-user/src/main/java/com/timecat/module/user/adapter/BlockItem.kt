package com.timecat.module.user.adapter

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.timecat.data.bmob.data.common.Block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-14
 * @description null
 * @usage null
 */
data class BlockItem(
    val block: Block,
    override val itemType: Int = block.type
) : MultiItemEntity
