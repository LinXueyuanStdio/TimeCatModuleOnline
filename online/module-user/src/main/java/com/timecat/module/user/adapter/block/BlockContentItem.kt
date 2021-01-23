package com.timecat.module.user.adapter.block

import android.app.Activity
import android.view.View
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.element.alert.ToastUtil
import com.timecat.extend.arms.BaseApplication
import com.timecat.extend.image.IMG
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.data.block.type.BLOCK_POST
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.nine.BGANinePhotoLayout
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.commentText
import com.timecat.module.user.ext.likeText
import com.timecat.module.user.ext.mySpanCreateListener
import com.timecat.module.user.ext.shareText
import com.timecat.module.user.view.dsl.setupLikeBlockButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_footer.view.*
import kotlinx.android.synthetic.main.user_base_item_moment.view.*
import kotlinx.android.synthetic.main.user_base_item_moment_content.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
open class BlockContentItem(
    open val activity: Activity,
    open var block: Block,
) : BaseDetailItem<BlockContentItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.user_base_item_moment_content

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
        when (block.type) {
            BLOCK_MOMENT -> {
                setMomentContent(holder, block)
                setFooter(adapter, holder, block)
            }
            BLOCK_POST -> {
                setPostContent(holder, block)
                setFooter(adapter, holder, block)
            }
            BLOCK_COMMENT -> {
                setCommentContent(holder, block)
                setFooter(adapter, holder, block)
            }
        }
    }

    private fun setMomentContent(holder: DetailVH, block: Block) {
        val mb = MomentBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, mb.atScope, mb.topicScope)
        setRelayScope(holder, block, mb.relayScope)
        setMediaScope(holder, block, mb.mediaScope)
        setPosScope(holder, block, mb.posScope)
        setOnItemClick(holder) {
            GO.momentDetail(block.objectId)
        }
    }

    private fun setPostContent(holder: DetailVH, block: Block) {
        val mb = PostBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, mb.atScope, mb.topicScope)
        setMediaScope(holder, block, mb.mediaScope)
        setPosScope(holder, block, mb.posScope)
        setOnItemClick(holder) {
            GO.postDetail(block.objectId)
        }
    }

    private fun setCommentContent(holder: DetailVH, block: Block) {
        val cb = CommentBlock.fromJson(block.structure)
        when (cb.type) {
            COMMENT_SIMPLE -> {
                val sc = SimpleComment.fromJson(cb.structure)
                setRichTextView(holder, block.content, sc.atScope, sc.topicScope)
                setMediaScope(holder, block, sc.mediaScope)
                setRelayScope(holder, block, sc.relayScope)
                setPosScope(holder, block, sc.posScope)
                setOnItemClick(holder) {
                    GO.commentDetail(block.objectId)
                }
            }
            COMMENT_LONG -> {

            }
            COMMENT_TEXT -> {

            }
            COMMENT_VIDEO -> {

            }
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

    private fun setMediaScope(
        holder: DetailVH,
        block: Block,
        mediaScope: AttachmentTail? = null
    ) {
        holder.root.circle_image_container.visibility = View.GONE
        mediaScope?.let {
            holder.root.circle_image_container.apply {
                if (holder.itemView.tag != block.objectId) return
                visibility = View.VISIBLE
                val datas = it.attachmentItems.map {
                    it.attachment
                }
                data = ArrayList(datas)
                setDelegate(object : BGANinePhotoLayout.Delegate {
                    override fun onClickNinePhotoItem(
                        ninePhotoLayout: BGANinePhotoLayout,
                        view: View,
                        position: Int,
                        model: String,
                        models: MutableList<String>
                    ) {
                        IMG.preview(activity)
                            .setIndex(position)
                            .setImageList(models)
                            .start()
                    }

                    override fun onClickExpand(
                        ninePhotoLayout: BGANinePhotoLayout,
                        view: View,
                        position: Int,
                        model: String,
                        models: MutableList<String>
                    ) {
                        IMG.preview(activity)
                            .setIndex(position)
                            .setImageList(models)
                            .start()
                    }
                })
            }
        }
    }

    private fun setRelayScope(
        holder: DetailVH,
        block: Block,
        relayScope: RelayScope? = null
    ) {
        holder.root.momentHerf.visibility = View.GONE
        relayScope?.let {
            requestBlock {
                query = oneBlockOf(it.objectId)
                onSuccess = { data ->
                    holder.root.momentHerf.apply {
                        visibility = View.VISIBLE
                        if (data.isEmpty()) {
                            isNotExist()
                        } else {
                            bindBlock(data[0])
                            setRelay(data[0])
                        }
                    }
                }
                onError = {
                    it.printStackTrace()
                }
            }
        }
    }

    private fun setPosScope(
        holder: DetailVH,
        block: Block,
        posScope: PosScope? = null
    ) {
        holder.root.position.visibility = View.GONE
        posScope?.let {
            holder.root.position.apply {
                if (holder.itemView.tag != block.objectId) return
                visibility = View.VISIBLE
            }
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

    fun rebind(adapter: FlexibleAdapter<IFlexible<*>>, block: Block) {
        requestOneBlock {
            query = oneBlockOf(block.objectId)
            onSuccess = {
                if (it == null) {
                    ToastUtil.e("发生错误")
                } else {
                    this@BlockContentItem.block = it
                    adapter.updateItem(this@BlockContentItem)
                }
            }
            onError = {
                it.printStackTrace()
            }
        }
    }

    private fun setFooter(adapter: FlexibleAdapter<IFlexible<*>>, holder: DetailVH, block: Block) {
        holder.root.footer_like.text = block.likeText()
        holder.root.footer_comment.text = block.commentText()
        holder.root.footer_share.text = block.shareText()

        setupLikeBlockButton(
            BaseApplication.getContext(),
            holder.root.footer_like_ll.footer_like_icon,
            block
        ) {
            rebind(adapter, block)
        }
        holder.root.footer_comment_ll.apply {
            setOnClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setOnClickListener
                }
                addCommentFor(block)
            }
        }
        holder.root.footer_share_ll.apply {
            setOnClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setOnClickListener
                }
                when (block.type) {
                    BLOCK_COMMENT -> {
                        //转发评论
                        GO.relayComment(block.parent, block)
                    }
                    BLOCK_MOMENT -> {
                        //转发动态
                        GO.relayMoment(block.parent, block)
                    }
                }
            }
        }
    }

    private fun addCommentFor(block: Block) {
        GO.addCommentFor(block)
    }

    private fun getText(count: Int): String {
        if (count == 0) return ""
        return "$count"
    }
}