package com.timecat.module.user.adapter.block

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import cn.leancloud.json.JSONObject
import com.shuyu.textutillib.RichTextView
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.commonsdk.extension.beVisibleIf
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.asUser
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.extend.image.IMG
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.H3
import com.timecat.layout.ui.business.ninegrid.NineGridView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.*
import com.timecat.module.user.social.comment.showSubComments
import com.timecat.module.user.social.share.showMore
import com.timecat.module.user.social.share.showShare
import com.timecat.module.user.view.UserHeadView
import com.timecat.module.user.view.dsl.setupLikeBlockButton2
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
open class CommentItem(
    open val activity: FragmentActivity,
    open var block: Block,
    val isMain: Boolean = false,
) : BaseDetailItem<CommentItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val main_flag: View = root.findViewById(R.id.main_flag)
        val userHead: UserHeadView = root.findViewById(R.id.userHead)
        val container: View = root.findViewById(R.id.container)
        val item: View = root.findViewById(R.id.item)
        val saying_content: RichTextView = root.findViewById(R.id.saying_content)
        val circle_image_container: NineGridView = root.findViewById(R.id.circle_image_container)
        val position: TextView = root.findViewById(R.id.position)
        val footer_like: TextView = root.findViewById(R.id.footer_like)
        val footer_comment: TextView = root.findViewById(R.id.footer_comment)
        val footer_share: TextView = root.findViewById(R.id.footer_share)
        val subs: ViewGroup = root.findViewById(R.id.subs)
    }

    override fun getLayoutRes(): Int = R.layout.user_base_item_comment

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
        holder.itemView.tag = block.objectId
        holder.main_flag.beVisibleIf(isMain)
        holder.userHead.bindBlock(block.get("user").asUser())
        val timeString: String = block.friendlyCreateTimeText()
        holder.userHead.content = timeString
        holder.userHead.moreView.setShakelessClickListener {
            showMore(activity.supportFragmentManager, block)
        }

        setCommentContent(holder, block)
        setFooter(adapter, holder, block)
        holder.item.setShakelessClickListener {
            GO.replyComment(block)
        }
        holder.item.setOnLongClickListener {
            showMore(activity.supportFragmentManager, block)
            true
        }
        block.getJSONArray("hot_children")?.let { subcomments ->
            holder.subs.apply {
                beVisible()
                for (comment in subcomments) {
                    LogUtil.e(comment)
                    val obj = comment as JSONObject?
                    obj?.let {
                        val author = it.getJSONObject("user").getString("nickName")
                        val content = it.getString("content")
                        val type = it.getIntValue("type")
                        if (type == COMMENT_REPLY) {
                            H3("$author$content")
                        } else {
                            H3("$author：$content")
                        }
                    }
                }
                if (subcomments.size >= 3) {
                    H3("查看全部 ${block.comments} 回复")
                }
            }
        }

        holder.subs.setShakelessClickListener {
            showSubComments(activity.supportFragmentManager, block)
        }
    }

    private fun setCommentContent(holder: DetailVH, block: Block) {
        val head = CommentBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, head.atScope, head.topicScope)
        setMediaScope(holder, block, head.mediaScope)
        setPosScope(holder, block, head.posScope)
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

    private fun setMediaScope(
        holder: DetailVH,
        block: Block,
        mediaScope: AttachmentTail? = null
    ) {
        holder.circle_image_container.visibility = View.GONE
        mediaScope?.let {
            holder.circle_image_container.apply {
                if (holder.itemView.tag != block.objectId) return
                visibility = View.VISIBLE
                val datas = it.attachmentItems.map {
                    it.attachment
                }
                setUrls(datas)
                setCallback(object : NineGridView.SimpleCallback() {
                    override fun onImageItemClicked(position: Int, urls: MutableList<String>) {
                        IMG.preview(activity)
                            .setIndex(position)
                            .setImageList(urls)
                            .start()
                    }
                })
            }
        }
    }

    private fun setPosScope(
        holder: DetailVH,
        block: Block,
        posScope: PosScope? = null
    ) {
        holder.position.visibility = View.GONE
        posScope?.let {
            holder.position.apply {
                if (holder.itemView.tag != block.objectId) return
                visibility = View.VISIBLE
            }
        }
    }

    fun rebind(adapter: FlexibleAdapter<IFlexible<*>>, block: Block) {
        requestOneBlock {
            query = oneBlockOf(block.objectId)
            onSuccess = {
                this@CommentItem.block = it
                adapter.updateItem(this@CommentItem)
            }
            onError = {
                it.printStackTrace()
            }
        }
    }

    private fun setFooter(adapter: FlexibleAdapter<IFlexible<*>>, holder: DetailVH, block: Block) {
        holder.footer_like.text = block.likeText("")
        holder.footer_comment.text = block.commentText("")
        holder.footer_share.text = block.shareText("")

        setupLikeBlockButton2(activity, holder.footer_like, block) {
//            rebind(adapter, block)
        }
        holder.footer_comment.setShakelessClickListener {
            if (UserDao.getCurrentUser() == null) {
                NAV.go(RouterHub.LOGIN_LoginActivity)
                return@setShakelessClickListener
            }
            GO.replyComment(block)
        }
        holder.footer_share.setShakelessClickListener {
            if (UserDao.getCurrentUser() == null) {
                NAV.go(RouterHub.LOGIN_LoginActivity)
                return@setShakelessClickListener
            }
            showShare(activity.supportFragmentManager, block)
        }
    }
}