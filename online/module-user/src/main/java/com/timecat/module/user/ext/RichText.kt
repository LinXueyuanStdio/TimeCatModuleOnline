package com.timecat.module.user.ext

import android.content.Context
import com.example.shuyu.span.CustomClickAtUserSpan
import com.example.shuyu.span.CustomClickTopicSpan
import com.example.shuyu.span.CustomLinkSpan
import com.shuyu.textutillib.listener.SpanAtUserCallBack
import com.shuyu.textutillib.listener.SpanCreateListener
import com.shuyu.textutillib.listener.SpanTopicCallBack
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.shuyu.textutillib.span.ClickAtUserSpan
import com.shuyu.textutillib.span.ClickTopicSpan
import com.shuyu.textutillib.span.LinkSpan

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
val mySpanCreateListener = object : SpanCreateListener {
    override fun getCustomClickAtUserSpan(
        context: Context,
        userModel: UserModel,
        color: Int,
        spanClickCallBack: SpanAtUserCallBack
    ): ClickAtUserSpan =
        CustomClickAtUserSpan(context, userModel, color, spanClickCallBack)

    override fun getCustomClickTopicSpan(
        context: Context,
        topicModel: TopicModel,
        color: Int,
        spanTopicCallBack: SpanTopicCallBack
    ): ClickTopicSpan =
        CustomClickTopicSpan(context, topicModel, color, spanTopicCallBack)

    override fun getCustomLinkSpan(
        context: Context,
        url: String,
        color: Int,
        spanUrlCallBack: SpanUrlCallBack
    ): LinkSpan =
        CustomLinkSpan(context, url, color, spanUrlCallBack)
}