package com.timecat.module.user.view

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.PostBlock
import kotlinx.android.synthetic.main.header_moment_detail.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 帖子的详情展示，通常位于头部
 * @usage null
 */
class PostView : BaseBlockView {
    constructor(context: Context) : super(context)

    constructor(context: Context, layout: Int) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes)

    /**
     * 必须调用，初始化
     * @param activity 图片预览需要用到，防止OOM
     */
    override fun bindBlock(activity: Activity, block: Block) {
        this.activity = activity
        root.userSection.bindBlock(block.user)
        val mb = PostBlock.fromJson(block.structure)
        setRichTextView(root, block.content, mb.atScope, mb.topicScope)
        setMediaScope(root, mb.mediaScope)
        setPosScope(root, mb.posScope)
        setCommentSum(block.comments)
    }
}