package com.timecat.module.user.adapter.block

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.deleteBlock
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.element.alert.ToastUtil
import com.timecat.extend.image.IMG
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_FORUM
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.data.block.type.BLOCK_POST
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.nine.BGANinePhotoLayout
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.util.CopyToClipboard
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.*
import com.timecat.module.user.permission.PermissionValidator
import com.timecat.module.user.view.dsl.setupLikeBlockButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_footer.view.*
import kotlinx.android.synthetic.main.user_base_item_moment.view.*
import kotlinx.android.synthetic.main.user_base_item_user_head.view.*
import kotlinx.android.synthetic.main.user_moment_item_main.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class BlockItem(
    val activity: Activity,
    var block: Block
) : BaseDetailItem<BlockItem.DetailVH>(block.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

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
        when (block.type) {
            BLOCK_MOMENT -> {
                setHeader(holder, block)
                setMomentContent(holder, block)
                setFooter(adapter, holder, block)
            }
            BLOCK_FORUM -> {
                setForumHeader(holder, block)
                setForumContent(holder, block)
                setFooter(adapter, holder, block)
            }
            BLOCK_POST -> {
                setHeader(holder, block)
                setPostContent(holder, block)
                setFooter(adapter, holder, block)
            }
            BLOCK_COMMENT -> {
                setHeader(holder, block)
                setCommentContent(holder, block)
                setFooter(adapter, holder, block)
            }
        }
    }

    var timeString: String = block.friendlyCreateTimeText()
    var updateTimeString: String = block.friendlyUpdateTimeText()
    private fun setHeader(holder: DetailVH, block: Block) {
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
                    PermissionValidator.checkById("delete_block") {
                        onAllowed = {
                            menu.findItem(R.id.delete)?.setVisible(true)
                        }
                    }
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.copy -> {
                                LetMeKnow.report(LetMeKnow.CLICK_TIMECAT_COPY)
                                CopyToClipboard.copy(activity, block.content)
                                true
                            }
                            R.id.delete -> {
                                deleteBlock {
                                    target = block.copy().also { it.objectId = block.objectId }
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
    }

    private fun setForumHeader(holder: DetailVH, block: Block) {
        val b = ForumBlock.fromJson(block.structure)
        holder.apply {
            b.header?.let {
                IconLoader.loadIcon(activity, root.head_image, it.avatar)
            }
            root.head_image.setShakelessClickListener {
                GO.forumDetail(block.objectId)
            }
            root.head_title.setText(block.title)
            root.head_title.setShakelessClickListener {
                GO.forumDetail(block.objectId)
            }
            root.head_content.setText("$updateTimeString")
            root.head_content.beVisible()
            root.head_content.setShakelessClickListener {
                GO.forumDetail(block.objectId)
            }
            root.head_more.setShakelessClickListener {
                PopupMenu(activity, it).apply {
                    inflate(R.menu.social_head)
                    PermissionValidator.checkById("delete_block") {
                        onAllowed = {
                            menu.findItem(R.id.delete)?.setVisible(true)
                        }
                    }
                    setOnMenuItemClickListener {
                        when (it.itemId) {
                            R.id.copy -> {
                                LetMeKnow.report(LetMeKnow.CLICK_TIMECAT_COPY)
                                CopyToClipboard.copy(activity, block.title)
                                true
                            }
                            R.id.delete -> {
                                deleteBlock {
                                    target = block.copy().also { it.objectId = block.objectId }
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

    private fun setForumContent(holder: DetailVH, block: Block) {
        val b = ForumBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, null, null)
        setMediaScope(holder, block, b.mediaScope)
        setOnItemClick(holder) {
            GO.forumDetail(block.objectId)
        }
    }

    private fun setPostContent(holder: DetailVH, block: Block) {
        val b = PostBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, b.atScope, b.topicScope)
        setMediaScope(holder, block, b.mediaScope)
        setPosScope(holder, block, b.posScope)
        setOnItemClick(holder) {
            GO.postDetail(block.objectId)
        }
    }

    private fun setCommentContent(holder: DetailVH, block: Block) {
        val b = CommentBlock.fromJson(block.structure)
        when (b.type) {
            COMMENT_SIMPLE -> {
                val sc = SimpleComment.fromJson(b.structure)
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
                onSuccess = {
                    val data = listOf(it)
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
                onListSuccess = { data ->
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
        requestBlock {
            query = oneBlockOf(block.objectId)
            onSuccess = {
                this@BlockItem.block = it
                adapter.updateItem(this@BlockItem)
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
            activity,
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

}