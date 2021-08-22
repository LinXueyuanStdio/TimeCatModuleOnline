package com.timecat.module.user.adapter.block

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import com.shuyu.textutillib.RichTextView
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.deleteBlock
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.bmob.requestOneBlockOrNull
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.readonly.UiHub
import com.timecat.identity.service.PermissionService
import com.timecat.layout.ui.business.label_tag_view.TagCloudView
import com.timecat.layout.ui.business.ninegrid.NineGridView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.util.CopyToClipboard
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.*
import com.timecat.module.user.permission.PermissionValidator
import com.timecat.module.user.view.MomentHerfView
import com.timecat.module.user.view.UserHeadView
import com.timecat.module.user.view.dsl.setupLikeBlockButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class MomentItem(
    val context: Context,
    var block: Block,
    var preview: (position: Int, urls: MutableList<String>) -> Unit = { pos, urls -> }
) : BaseDetailItem<MomentItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val container: ConstraintLayout by lazy { root.findViewById<ConstraintLayout>(R.id.container) }
        val head: UserHeadView by lazy { root.findViewById<UserHeadView>(R.id.head) }
        val saying: LinearLayout by lazy { root.findViewById<LinearLayout>(R.id.saying) }
        val saying_content: RichTextView by lazy { root.findViewById<RichTextView>(R.id.saying_content) }
        val circle_image_container: NineGridView by lazy { root.findViewById<NineGridView>(R.id.circle_image_container) }
        val momentHerf: MomentHerfView by lazy { root.findViewById<MomentHerfView>(R.id.momentHerf) }
        val tag_cloud_view: TagCloudView by lazy { root.findViewById<TagCloudView>(R.id.tag_cloud_view) }
        val position: TextView by lazy { root.findViewById<TextView>(R.id.position) }
        val footer: LinearLayout by lazy { root.findViewById<LinearLayout>(R.id.footer) }
        val footer_like_ll: LinearLayout by lazy { root.findViewById<LinearLayout>(R.id.footer_like_ll) }
        val footer_like_icon: ImageView by lazy { root.findViewById<ImageView>(R.id.footer_like_icon) }
        val footer_like: TextView by lazy { root.findViewById<TextView>(R.id.footer_like) }
        val footer_comment_ll: LinearLayout by lazy { root.findViewById<LinearLayout>(R.id.footer_comment_ll) }
        val footer_comment: TextView by lazy { root.findViewById<TextView>(R.id.footer_comment) }
        val footer_share_ll: LinearLayout by lazy { root.findViewById<LinearLayout>(R.id.footer_share_ll) }
        val footer_share: TextView by lazy { root.findViewById<TextView>(R.id.footer_share) }
        val end: View by lazy { root.findViewById<View>(R.id.end) }
    }

    override fun getLayoutRes(): Int = R.layout.user_moment_item_main

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
        setHeader(holder, block)
        setMomentContent(holder, block)
        setFooter(adapter, holder, block)
    }

    var timeString: String = block.friendlyCreateTimeText()
    var updateTimeString: String = block.friendlyUpdateTimeText()
    private fun setHeader(holder: DetailVH, block: Block) {
        val user = block.user
        holder.head.bindBlock(user)
        if (!TextUtils.isEmpty(block.user.intro)) {
            holder.head.content = "$timeString | ${block.user.intro}"
        } else {
            holder.head.content = timeString
        }
        holder.head.moreView.setShakelessClickListener {
            PopupMenu(it.context, it).apply {
                inflate(R.menu.social_head)
                GlobalScope.launch(Dispatchers.IO) {
                    val s = NAV.service(PermissionService::class.java)
                    s?.validate(UiHub.USER_ITEM_delete_block, object : PermissionService.Callback{
                        override fun onPass() {
                            GlobalScope.launch(Dispatchers.Main) {
                                menu.findItem(R.id.delete)?.setVisible(true)
                            }
                        }

                        override fun onReject() {
                            GlobalScope.launch(Dispatchers.Main) {
                                menu.findItem(R.id.delete)?.setVisible(false)
                            }
                        }
                    })
                }
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy -> {
                            LetMeKnow.report(LetMeKnow.CLICK_TIMECAT_COPY)
                            CopyToClipboard.copy(holder.head.context, block.content)
                            true
                        }
                        R.id.delete -> {
                            deleteBlock {
                                target = block
                                onError = {
                                    LogUtil.e(it)
                                    ToastUtil.e("删除出错")
                                }
                                onSuccess = {
                                    LogUtil.e(it)
                                    ToastUtil.ok("删除成功")
                                }
                            }
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    private fun setMomentContent(holder: DetailVH, block: Block) {
        val b = MomentBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, b.atScope, b.topicScope)
        setRelayScope(holder, block, b.relayScope)
        setMediaScope(holder, block, b.mediaScope)
        setPosScope(holder, block, b.posScope)
        setOnItemClick(holder) {
            GO.momentDetail(block.objectId)
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
                    HERF.gotoUrl(context, url)
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
                        preview(position, urls)
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
        holder.momentHerf.visibility = View.GONE
        relayScope?.let {
            requestOneBlockOrNull {
                query = oneBlockOf(it.objectId)
                onEmpty = {
                    holder.momentHerf.apply {
                        visibility = View.VISIBLE
                        isNotExist()
                    }
                }
                onSuccess = { data ->
                    holder.momentHerf.apply {
                        visibility = View.VISIBLE
                        bindBlock(data)
                        setRelay(data)
                    }
                }
                onError = {
                    it.printStackTrace()
                }
            }
        }
        if (relayScope == null) {
            block.parent?.let { data ->
                holder.momentHerf.apply {
                    visibility = View.VISIBLE
                    bindBlock(data)
                    setRelay(data)
                }
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

    private fun setOnItemClick(
        holder: DetailVH,
        onClick: (View) -> Unit
    ) {
        holder.container.setShakelessClickListener {
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
                    this@MomentItem.block = it
                    adapter.updateItem(this@MomentItem)
                }
            }
            onError = {
                it.printStackTrace()
            }
        }
    }

    private fun setFooter(adapter: FlexibleAdapter<IFlexible<*>>, holder: DetailVH, block: Block) {
        holder.footer_like.text = block.likeText()
        holder.footer_comment.text = block.commentText()
        holder.footer_share.text = block.shareText()

        setupLikeBlockButton(
            holder.root.context,
            holder.footer_like_ll,
            holder.footer_like_icon,
            holder.footer_like,
            block
        ) {
//            rebind(adapter, block)
        }
        holder.footer_comment_ll.apply {
            setOnClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setOnClickListener
                }
                addCommentFor(block)
            }
        }
        holder.footer_share_ll.apply {
            setOnClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setOnClickListener
                }
                when (block.type) {
                    BLOCK_COMMENT -> {
                        //转发评论
                        GO.replyComment(block)
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

}