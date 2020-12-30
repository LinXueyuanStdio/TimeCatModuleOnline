package com.timecat.module.user.adapter.detail

import android.app.Activity
import android.view.View
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.identity.Attr
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.mySpanCreateListener
import com.timecat.identity.data.base.AtScope
import com.timecat.identity.data.base.TopicScope
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_richtext.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 一个作者
 * @usage 可关注作者，点击进入作者个人详情页
 */
class SimpleContentItem(
    val activity: Activity,
    val content: String,
    val atScope: AtScope? = null,
    val topicScope: TopicScope? = null
) : BaseDetailItem<SimpleContentItem.DetailVH>("内容描述") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.user_base_item_richtext

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
        holder.apply {
            setRichTextView(root, content)
        }
    }

    private fun setRichTextView(
        holder: View,
        content: String,
        atScope: AtScope? = null,
        topicScope: TopicScope? = null
    ) {
        holder.content.apply {
            val color = Attr.getAccentColor(context)
            atColor = color
            topicColor = color
            linkColor = color
            isNeedNumberShow = true
            isNeedUrlShow = true
            maxLines = Int.MAX_VALUE
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

}