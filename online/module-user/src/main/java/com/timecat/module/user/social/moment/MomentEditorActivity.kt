package com.timecat.module.user.social.moment

import android.view.View
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.data.bmob.dao.block.BlockDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.isRelays
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.MomentBlock
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnSaveListener
import kotlinx.android.synthetic.main.user_activity_moment_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddMomentActivity)
class MomentEditorActivity : BaseBlockEditorActivity() {
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
        if (currentUser == null) {
            NAV.go(RouterHub.LOGIN_LoginActivity)
            return
        }
        val block = Block.forMoment(currentUser, content)
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
        BlockDao.save(block, object : OnSaveListener<Block> {
            override fun success(data: Block) {
                relay?.let {
                    it.isRelays {
                        finish()
                    }
                } ?: finish()
            }

            override fun error(e: DataError) {

            }
        })
    }
}