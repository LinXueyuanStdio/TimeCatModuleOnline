package com.timecat.module.user.social.forum.post

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.PostBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddPostActivity)
class PostEditorActivity : BaseBlockEditorActivity() {
    /**
     * 暂时没用
     */
    @AttrValueAutowiredAnno("parent")
    @JvmField
    var parent: Block? = null

    override fun title(): String = "帖子"
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        //TODO infix refactor
        val block = Block.forPost(I(), content)
        block.parent = parent
        block.structure = PostBlock(
            mediaScope = attachments,
            atScope = AtScope(emojiEditText.realUserList.map {
                AtItem(it.user_name, it.user_id)
            }.toMutableList()),
            topicScope = TopicScope(emojiEditText.realTopicList.map {
                TopicItem(it.topicName, it.topicId)
            }.toMutableList())
        ).toJson()
        saveBlock {
            target = block
            onSuccess = {
                finish()
            }
            onError = errorCallback
        }
    }
}