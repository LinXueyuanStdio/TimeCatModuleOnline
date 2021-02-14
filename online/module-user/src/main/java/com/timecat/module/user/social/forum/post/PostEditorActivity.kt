package com.timecat.module.user.social.forum.post

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Post
import com.timecat.data.bmob.ext.create
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

    /**
     * 更新
     */
    @AttrValueAutowiredAnno("block")
    @JvmField
    var post: Block? = null

    override fun title(): String = "帖子"
    override fun routerInject() = NAV.inject(this)

    override fun currentBlock(): Block? = post
    override fun subtype(): Int = 0
    override fun savableBlock(): Block = I() create Post {
        this.parent = this@PostEditorActivity.parent
        this.subtype = subtype()
        this.content = formData.content
        this.headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        this.title = ""
        this.content = formData.content
        this.subtype = subtype()
        //更新评论后应该保持parent不变
        this.structure = getHeadBlock().toJson()
    }


    fun getHeadBlock(): PostBlock {
        return PostBlock(
            mediaScope = formData.attachments,
            atScope = formData.atScope,
            topicScope = formData.topicScope,
        )
    }
}