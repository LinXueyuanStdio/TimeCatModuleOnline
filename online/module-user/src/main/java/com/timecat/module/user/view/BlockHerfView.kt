package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.data.block.type.BLOCK_POST
import com.timecat.identity.data.block.type.BLOCK_RECOMMEND
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.ext.showDetail
import com.timecat.module.user.ext.simpleAvatar
import com.timecat.module.user.view.dsl.setupFollowBlockButton

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class BlockHerfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : UserHerfView(context, attrs, defStyleAttr, defStyleRes) {
    /**
     * 必须调用，初始化
     */
    fun bindBlock(block: Block) {
        title = if (!isSocial(block)) block.title else "@${block.user.nickName}\n${block.content}"
        icon = block.simpleAvatar()
        setupFollowBlockButton(context, focusUser, block)
        setShakelessClickListener {
            block.showDetail()
        }
    }

    fun isSocial(block: Block): Boolean {
        return block.type == BLOCK_MOMENT
            || block.type == BLOCK_POST
            || block.type == BLOCK_RECOMMEND
            || block.type == BLOCK_COMMENT
    }

}