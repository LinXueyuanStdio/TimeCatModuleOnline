package com.timecat.module.user.social.comment

import android.view.View
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.block.CommentDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.isRelays
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.COMMENT_SIMPLE
import com.timecat.identity.data.block.CommentBlock
import com.timecat.identity.data.block.SimpleComment
import kotlinx.android.synthetic.main.user_activity_moment_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddCommentActivity)
class CommentEditorActivity : BaseBlockEditorActivity() {
    @AttrValueAutowiredAnno("parent")
    @JvmField
    var parent: Block? = null

    /**
     * 被转发的块
     */
    @AttrValueAutowiredAnno("relay")
    @JvmField
    var relay: Block? = null

    override fun title(): String = "评论"
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        parent?.let {
            block_herf.visibility = View.VISIBLE
            block_herf.bindBlock(relay ?: it)
            emojiEditText.hint = relay?.let { "转发 @${it.user.nick}" } ?: "回复 @${it.user.nick}"
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        val block = Block.forComment(I(), content)
        block.privacy = PrivacyScope(isPrivate = true)
        block.structure = CommentBlock(
            COMMENT_SIMPLE,
            SimpleComment(
                content = NoteBody(),
                mediaScope = attachments,
                atScope = AtScope(emojiEditText.realUserList.map {
                    AtItem(it.user_name, it.user_id)
                }.toMutableList()),
                topicScope = TopicScope(emojiEditText.realTopicList.map {
                    TopicItem(it.topicName, it.topicId)
                }.toMutableList()),
                relayScope = relay?.let {
                    RelayScope(it.objectId)
                }
            ).toJson()
        ).toJson()

        CommentDao.addComment(I(), block, parent!!, {
            relay?.let {
                it.isRelays {
                    finish()
                }
            } ?: finish()
            ToastUtil.ok("评论成功")
        }, {
            ToastUtil.e_long("评论失败")
        })
    }
}