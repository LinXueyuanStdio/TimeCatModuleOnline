package com.timecat.module.user.social.recommend

import android.view.View
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Comment
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.isRelays
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.COMMENT_REPLY
import com.timecat.identity.data.block.COMMENT_SIMPLE
import com.timecat.identity.data.block.CommentBlock
import com.timecat.identity.data.block.SimpleComment
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.user_activity_moment_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description 推荐
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddRecommendActivity)
class RecommendEditorActivity : BaseBlockEditorActivity() {
    /**
     * 评论某价值块
     * 当评论为第一次时，parent = 价值块
     * 当评论为回复某评论时，parent = 评论
     * 当评论为回复某回复时，parent = 评论，title = 回复
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

    var replyBlockId: String = ""

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
        this.parent = this@RecommendEditorActivity.parent
        this.subtype = subtype()
        this.headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = replyBlockId
        content = formData.content
        //更新评论后应该保持parent不变
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
            block_herf.visibility = View.VISIBLE
            block_herf.bindBlock(relay ?: it)
            emojiEditText.hint = relay?.let { "转发 @${it.user.nick}" } ?: "回复 @${it.user.nick}"
        }
        comment?.let {
            replyBlockId = it.title
            formData.content = it.content
            val head = CommentBlock.fromJson(it.structure)
            formData.attachments = head.mediaScope
        }
    }

    override fun onSaveSuccess(it: Block) {
        if (relay != null) {
            relay?.isRelays {
                ToastUtil.ok("评论成功")
                finish()
            }
        } else {
            ToastUtil.ok("评论成功")
            finish()
        }
    }
}