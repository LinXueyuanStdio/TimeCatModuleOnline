package com.timecat.module.user.view.dsl

import android.view.ViewGroup
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.layout.ui.business.form.wrapContext
import com.timecat.module.user.view.item.ContentItem
import com.timecat.module.user.view.item.ContributionItem
import com.timecat.module.user.view.item.IconTextItem
import com.timecat.module.user.view.item.UserRelationItem
import com.timecat.module.user.view.widget.UserCircleImageView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description 对自定义 View 使用 DSL 来简化布局构建
 * @usage null
 */

inline fun ViewGroup.UserCircleImageView(
    style: Int? = null,
    autoAdd: Boolean = true,
    init: UserCircleImageView.() -> Unit = {}
): UserCircleImageView {
    val imageView = UserCircleImageView(style.wrapContext(context))
    return imageView.apply(init).also { if (autoAdd) addView(it) }
}


inline fun ViewGroup.Content(
    content: String,
    nameList: List<UserModel> = listOf(),
    topicList: List<TopicModel> = listOf(),
    style: Int? = null,
    autoAdd: Boolean = true,
    init: ContentItem.() -> Unit = {}
): ContentItem {
    val view = ContentItem(style.wrapContext(context))
    view.setRichText(content, nameList, topicList)
    return view.apply(init).also { if (autoAdd) addView(it) }
}
inline fun ViewGroup.IconText(
    icon: Int,
    text: String,
    style: Int? = null,
    autoAdd: Boolean = true,
    init: IconTextItem.() -> Unit = {}
): IconTextItem {
    val view = IconTextItem(style.wrapContext(context))
    view.setIconText(icon, text)
    return view.apply(init).also { if (autoAdd) addView(it) }
}


inline fun ViewGroup.UserRelation(
    style: Int? = null,
    autoAdd: Boolean = true,
    init: UserRelationItem.() -> Unit = {}
): UserRelationItem {
    val view = UserRelationItem(style.wrapContext(context))
    return view.apply(init).also { if (autoAdd) addView(it) }
}

inline fun ViewGroup.Contribution(
    style: Int? = null,
    autoAdd: Boolean = true,
    init: ContributionItem.() -> Unit = {}
): ContributionItem {
    val view = ContributionItem(style.wrapContext(context))
    return view.apply(init).also { if (autoAdd) addView(it) }
}

