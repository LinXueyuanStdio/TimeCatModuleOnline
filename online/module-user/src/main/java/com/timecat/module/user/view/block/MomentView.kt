package com.timecat.module.user.view.block

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.MomentBlock

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 动态的详情展示，通常位于头部
 * @usage null
 */
class MomentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : BaseBlockView(context, attrs, defStyleAttr, defStyleRes) {
    override fun bindBlock(activity: Activity, block: Block) {
        this.activity = activity
        setHead(block)
        val head = MomentBlock.fromJson(block.structure)
        setRichTextView(root, block.content, head.atScope, head.topicScope)
        setMediaScope(root, head.mediaScope)
        setRelayScope(root, head.relayScope)
        setPosScope(root, head.posScope)
        setShare(block)
    }
}