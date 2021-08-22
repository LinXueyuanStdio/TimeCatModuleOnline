package com.timecat.module.user.adapter.interaction

import android.app.Activity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.shuyu.textutillib.RichTextView
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.common.InterAction
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.identity.data.action.INTERACTION_Recommend
import com.timecat.identity.data.base.AtScope
import com.timecat.identity.data.base.TopicScope
import com.timecat.layout.ui.business.label_tag_view.TagCloudView
import com.timecat.layout.ui.business.ninegrid.NineGridView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.ext.mySpanCreateListener
import com.timecat.module.user.view.MomentHerfView
import com.timecat.module.user.view.UserHeadView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

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

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val container: ConstraintLayout by lazy { root.findViewById<ConstraintLayout>(R.id.container) }
        val head: UserHeadView by lazy { root.findViewById<UserHeadView>(R.id.head) }
        val saying: LinearLayout by lazy { root.findViewById<LinearLayout>(R.id.saying) }
        val saying_content: RichTextView by lazy { root.findViewById<RichTextView>(R.id.saying_content) }
        val circle_image_container: NineGridView by lazy { root.findViewById<NineGridView>(R.id.circle_image_container) }
        val momentHerf: MomentHerfView by lazy { root.findViewById<MomentHerfView>(R.id.momentHerf) }
        val tag_cloud_view: TagCloudView by lazy { root.findViewById<TagCloudView>(R.id.tag_cloud_view) }
        val position: TextView by lazy { root.findViewById<TextView>(R.id.position) }
        val end: View by lazy { root.findViewById<View>(R.id.end) }
    }

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
        holder.head.bindBlock(user)
    }

    private fun setAuthContent(holder: DetailVH, block: InterAction) {
        val content =
            "${block.user.nickName}(${block.user.objectId}) 给 ${block.target.nickName}(${block.target.objectId}) 授予 ${block.block}"
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
        holder.saying_content.apply {
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
        holder.container.setShakelessClickListener {
            onClick(it)
        }
    }

}