package com.timecat.module.user.social.comment

import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Comment
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.isCommented
import com.timecat.data.bmob.ext.isRelays
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseArticleBlockEditorActivity
import com.timecat.module.user.ext.div
import com.timecat.module.user.view.BlockHerfView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddCommentActivity)
class CommentEditorActivity : BaseArticleBlockEditorActivity() {
    /**
     * 评论某价值块
     * 当评论为第一次时，parent = 价值块，title=价值块.id
     * 当评论为评论某评论时，parent = 价值块，relay = 评论，title=reply.title/reply.id
     */
    @AttrValueAutowiredAnno("parent")
    @JvmField
    var parent: Block? = null

    /**
     * 回复某回复时用
     */
    @AttrValueAutowiredAnno("relay")
    @JvmField
    var relay: Block? = null

    /**
     * 更新
     */
    @AttrValueAutowiredAnno("block")
    @JvmField
    var comment: Block? = null

    override fun title(): String = "评论"
    override fun routerInject() = NAV.inject(this)

    override fun currentBlock(): Block? = comment

    override fun subtype(): Int {
        if (relay != null) {
            return COMMENT_REPLY
        } else if (parent?.isComment() == true) {
            return COMMENT_REPLY
        } else {
            return COMMENT_SIMPLE
        }
    }

    override fun savableBlock(): Block = I() create Comment {
        title = relay?.let { it.title / it.objectId } ?: parent!!.objectId
        content = formData.content
        parent = this@CommentEditorActivity.parent
        subtype = subtype()
        headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        //更新评论后应该保持parent,title不变
        content = formData.content
        subtype = subtype()
        structure = getHeadBlock().toJson()
    }

    fun getHeadBlock(): CommentBlock {
        return CommentBlock(
            content = NoteBody(),
            mediaScope = formData.attachments,
            atScope = formData.atScope,
            topicScope = formData.topicScope,
            structure = SimpleComment().toJsonObject()
        )
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        parent?.let {
            LogUtil.e(it)
            val block_herf = BlockHerfView(context)
            container.addView(block_herf, 0)
            block_herf.bindBlock(relay ?: it)
            emojiEditText.hint = relay?.let { "回复 #${it.title} @${it.user.nickName}" }
                ?: "评论 #${it.title}"
        }
        comment?.let {
            formData.content = it.content
            val head = CommentBlock.fromJson(it.structure)
            formData.attachments = head.mediaScope
            formData.setScope(head.atScope, head.topicScope)
        }
    }

    override fun onSaveSuccess(it: Block) {
        parent?.isCommented()
        relay?.isRelays()
        ToastUtil.ok("评论成功")
        finish()
    }
}