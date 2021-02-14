package com.timecat.module.user.social.moment

import android.view.View
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.isRelays
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.MomentBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.base.BaseArticleBlockEditorActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.android.synthetic.main.user_activity_moment_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddMomentActivity)
class MomentEditorActivity : BaseArticleBlockEditorActivity() {
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

    /**
     * 更新
     */
    @AttrValueAutowiredAnno("block")
    @JvmField
    var moment: Block? = null

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

    override fun currentBlock(): Block? = moment

    override fun subtype(): Int = 0

    override fun savableBlock(): Block = I() create Moment {

    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.replyBlockId
        content = formData.content
        //更新评论后应该保持parent不变
        structure = getHeadBlock().toJson()
    }

    fun getHeadBlock(): MomentBlock {
        return MomentBlock(
            mediaScope = attachments,
            atScope = AtScope(emojiEditText.realUserList.map {
                AtItem(it.user_name, it.user_id)
            }.toMutableList()),
            topicScope = TopicScope(emojiEditText.realTopicList.map {
                TopicItem(it.topicName, it.topicId)
            }.toMutableList()),
            relayScope = relay?.let { RelayScope(it.objectId) }
        )
    }

    override fun release() {
        formData.content = content
        formData.attachments = attachments
        ok()
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