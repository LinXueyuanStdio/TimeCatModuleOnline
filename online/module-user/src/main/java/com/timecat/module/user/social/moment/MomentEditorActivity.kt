package com.timecat.module.user.social.moment

import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Moment
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.isRelays
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.MomentBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseArticleBlockEditorActivity
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
@RouterAnno(hostAndPath = RouterHub.USER_AddMomentActivity)
class MomentEditorActivity : BaseArticleBlockEditorActivity() {
    /**
     * 被转发的价值块
     */
    @AttrValueAutowiredAnno("parent")
    @JvmField
    var parent: Block? = null

    /**
     * 评论
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
        parent?.let {parent->
            val block_herf = BlockHerfView(context)
            container.addView(block_herf, 0)
            block_herf.bindBlock(relay ?: parent)
            if (relay == null) {
                emojiEditText.hint = "评论 @${parent.user.nickName} "
            } else {
                relay?.let {
                    emojiEditText.hint = "转发 @${it.user.nickName} "
                    val content = "//@${it.user.nickName} ：${it.content}"
                    formData.setContent(context, content,
                        listOf(UserModel("@${it.user.nickName}", it.user.objectId)),
                        listOf(TopicModel("#${parent.title}#", parent.objectId)))
                    emojiEditText.setSelection(0)
                }
            }
        }
    }

    override fun currentBlock(): Block? = moment

    override fun subtype(): Int = 0

    override fun savableBlock(): Block = I() create Moment {
        title = ""
        content = formData.content
        parent = this@MomentEditorActivity.parent
        subtype = subtype()
        headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = ""
        content = formData.content
        //更新评论后应该保持parent不变
        subtype = subtype()
        structure = getHeadBlock().toJson()
    }

    fun getHeadBlock(): MomentBlock {
        return MomentBlock(
            mediaScope = formData.attachments,
            atScope = formData.atScope,
            topicScope = formData.topicScope,
            relayScope = relay?.let { RelayScope(it.objectId) }
        )
    }

    override fun onSaveSuccess(it: Block) {
        relay?.isRelays()
        ToastUtil.ok("发布成功")
        finish()
    }
}