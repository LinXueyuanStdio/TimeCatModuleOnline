package com.timecat.module.user.social.moment

import android.view.View
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.isRelays
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.MomentBlock
import com.timecat.module.user.base.BaseComplexEditorActivity
import kotlinx.android.synthetic.main.user_activity_moment_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddMomentActivity)
class MomentEditorActivity : BaseComplexEditorActivity() {
    /**
     * 暂时没用
     */
    @AttrValueAutowiredAnno("parent")
    @JvmField
    var parent: Block? = null

    /**
     * 被转发的块
     */
    @AttrValueAutowiredAnno("relay")
    @JvmField
    var relay: Block? = null

    override fun title(): String = "动态"
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        relay?.let {
            block_herf.visibility = View.VISIBLE
            block_herf.bindBlock(it)
            emojiEditText.hint = "转发 @${it.user.nick}"
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        val block = Block.forMoment(I(), content)
        block.parent = parent
        block.privacy = PrivacyScope(isPrivate = true)
        block.structure = MomentBlock(
            mediaScope = attachments,
            atScope = AtScope(emojiEditText.realUserList.map {
                AtItem(it.user_name, it.user_id)
            }.toMutableList()),
            topicScope = TopicScope(emojiEditText.realTopicList.map {
                TopicItem(it.topicName, it.topicId)
            }.toMutableList()),
            relayScope = relay?.let { RelayScope(it.objectId) }
        ).toJson()
        saveBlock {
            target = block
            onError = errorCallback
            onSuccess = {
                relay?.let {
                    it.isRelays { finish() }
                } ?: finish()
            }
            onListSuccess = {
                relay?.let {
                    it.isRelays { finish() }
                } ?: finish()
            }
        }
    }
}