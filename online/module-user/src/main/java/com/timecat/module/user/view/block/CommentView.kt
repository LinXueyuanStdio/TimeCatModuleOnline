package com.timecat.module.user.view.block

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class CommentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : BaseBlockView(context, attrs, defStyleAttr, defStyleRes) {
    override fun bindBlock(activity: Activity, block: Block) {
        this.activity = activity
        setHead(block)
        val head = CommentBlock.fromJson(block.structure)
        setMediaScope(root, head.mediaScope)
        setPosScope(root, head.posScope)
        head.structure?.let {
            when (block.subtype) {
                COMMENT_SIMPLE -> {
                    setRichTextView(root, block.content, head.atScope, head.topicScope)
                }
                COMMENT_REPLY -> {
                    val reply = ReplyComment.fromJson(it)
                    LogUtil.e(reply)
                    setRichTextView(root, block.content, head.atScope, head.topicScope)
                }
                COMMENT_SCORE -> {

                }
                COMMENT_TEXT -> {

                }
                COMMENT_VIDEO -> {

                }
            }
        }
        setShare(block)
    }
}