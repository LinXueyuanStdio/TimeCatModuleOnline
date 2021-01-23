package com.timecat.module.user.adapter.interaction

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.common.InterAction
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.identity.data.action.INTERACTION_Recommend
import com.timecat.identity.data.base.AtScope
import com.timecat.identity.data.base.TopicScope
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.ext.mySpanCreateListener
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_auth_item_main.view.*
import kotlinx.android.synthetic.main.user_base_item_moment.view.*
import kotlinx.android.synthetic.main.user_base_item_user_head.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class InterActionItem(
    val activity: Activity,
    var action: InterAction
) : BaseDetailItem<InterActionItem.DetailVH>(action.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.user_auth_item_main

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)
        holder.itemView.tag = action.objectId
        when (action.type) {
            INTERACTION_Recommend -> {
                setHeader(holder, action)
                setAuthContent(holder, action)
            }
            INTERACTION_Auth_Identity, INTERACTION_Auth_Permission, INTERACTION_Auth_Role -> {
                setHeader(holder, action)
                setAuthContent(holder, action)
            }
        }
    }

    var timeString: String = action.friendlyCreateTimeText()
    private fun setHeader(holder: DetailVH, block: InterAction) {
        val user = block.user
        holder.apply {
            IconLoader.loadIcon(activity, root.head_image, user.avatar)
            root.head_image.setShakelessClickListener {
                GO.userDetail(user.objectId)
            }
            root.head_title.setText(user.nickName)
            root.head_title.setShakelessClickListener {
                GO.userDetail(user.objectId)
            }
            if (!TextUtils.isEmpty(user.brief_intro)) {
                root.head_content.setText("$timeString | ${user.brief_intro}")
            } else {
                root.head_content.setText("$timeString")
            }
            root.head_content.beVisible()
            root.head_content.setShakelessClickListener {
                GO.userDetail(user.objectId)
            }
            root.head_more.setShakelessClickListener {
                PopupMenu(activity, it).apply {
                    inflate(R.menu.social_head)
                    show()
                }
            }
        }
    }

    private fun setAuthContent(holder: DetailVH, block: InterAction) {
        val content = "${block.user.nick}(${block.user.objectId}) 给 ${block.target.nick}(${block.target.objectId}) 授予 ${block.block}"
        setRichTextView(holder, content)
        setOnItemClick(holder) {
            //TODO auth detail，not editable
        }
    }

    private fun setRichTextView(
        holder: DetailVH,
        content: String,
        atScope: AtScope? = null,
        topicScope: TopicScope? = null
    ) {
        holder.root.saying_content.apply {
            val color = Attr.getAccentColor(context)
            atColor = color
            topicColor = color
            linkColor = color
            isNeedNumberShow = true
            isNeedUrlShow = true
            setSpanUrlCallBackListener(object : SpanUrlCallBack {
                override fun phone(p0: View?, p1: String?) {
//                    val callIntent = Intent(Intent.ACTION_CALL)
//                    callIntent.setData(Uri.parse("tel:123456789"))
//                    context.startActivity(callIntent)
                }

                override fun url(p0: View?, url: String?) {
                    HERF.gotoUrl(activity, url)
                }
            })
            setSpanCreateListener(mySpanCreateListener)
            setSpanTopicCallBackListener { _, topicModel ->
                GO.topicDetail(topicModel.topicId)
            }
            setSpanAtUserCallBackListener { _, userModel ->
                GO.userDetail(userModel.user_id)
            }
            val users = atScope?.ats?.map {
                UserModel(it.name, it.objectId)
            }?.toMutableList() ?: mutableListOf()
            val topics = topicScope?.topics?.map {
                TopicModel(it.name, it.objectId)
            }?.toMutableList() ?: mutableListOf()
            setRichText(content, users, topics)
        }
    }

    private fun setOnItemClick(
        holder: DetailVH,
        onClick: (View) -> Unit
    ) {
        holder.root.container.setShakelessClickListener {
            onClick(it)
        }
    }

}