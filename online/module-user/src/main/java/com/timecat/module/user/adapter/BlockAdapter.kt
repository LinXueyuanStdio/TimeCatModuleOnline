package com.timecat.module.user.adapter

import android.app.Activity
import android.os.Parcelable
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.PopupMenu
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
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
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.extend.image.IMG
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.nine.BGANinePhotoLayout
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.util.CopyToClipboard
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.*
import com.timecat.module.user.view.MomentHerfView
import com.timecat.module.user.view.dsl.setupLikeBlockButton
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.header_moment_detail.view.*
import kotlinx.android.synthetic.main.user_base_item_footer.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-15
 * @description 一般块
 * @usage null
 */
class BlockAdapter(
    private val activity: Activity,
    data: MutableList<BlockItem>
) : BaseMultiItemQuickAdapter<BlockItem, BaseViewHolder>(data) {
    init {
        addItemType(BLOCK_MOMENT, R.layout.user_moment_item_main)
        addItemType(BLOCK_COMMENT, R.layout.user_moment_item_main)
        addItemType(BLOCK_POST, R.layout.user_moment_item_main)

        addItemType(BLOCK_LEADER_BOARD, R.layout.user_block_small_item)
        addItemType(BLOCK_FORUM, R.layout.user_block_small_item)
        addItemType(BLOCK_APP, R.layout.user_block_small_item)
        addItemType(BLOCK_TOPIC, R.layout.user_block_small_item)
        addItemType(BLOCK_TAG, R.layout.user_block_small_item)
        addItemType(BLOCK_PERMISSION, R.layout.user_block_small_item)
        addItemType(BLOCK_ROLE, R.layout.user_block_small_item)
        addItemType(BLOCK_IDENTITY, R.layout.user_block_small_item)
    }

    override fun convert(holder: BaseViewHolder, item: BlockItem) {
        holder.itemView.tag = item.block.objectId
        when (item.itemType) {
            BLOCK_MOMENT -> {
                val moment: Block = item.block
                setHead(holder, moment)
                setMomentContent(holder, moment)
                setFooter(holder, moment, item)
            }
            BLOCK_POST -> {
                val post: Block = item.block
                setHead(holder, post)
                setPostContent(holder, post)
                setFooter(holder, post, item)
            }
            BLOCK_COMMENT -> {
                val comment: Block = item.block
                setHead(holder, comment)
                setCommentContent(holder, comment)
                setFooter(holder, comment, item)
            }
            BLOCK_LEADER_BOARD -> {
                val leaderboard: Block = item.block
                holder.setText(R.id.tv_name, leaderboard.title)
                LOAD.image(leaderboard.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    LogUtil.e(leaderboard.toString())
                    NAV.go(context, RouterHub.LEADERBOARD_LeaderBoardDetailActivity, "leaderBoard", item.block as Parcelable)
                }
            }
            BLOCK_FORUM -> {
                val forum: Block = item.block
                holder.setText(R.id.tv_name, forum.title)
                val header = ForumBlock.fromJson(forum.structure)
                val url = header.header?.icon ?: "R.drawable.ic_launcher"
                LOAD.image(url, holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    GO.forumDetail(item.block.objectId)
                }
            }
            BLOCK_APP -> {
                val appBlock: Block = item.block
                holder.setText(R.id.tv_name, appBlock.title)
                LOAD.image(appBlock.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    LogUtil.e(appBlock.toString())
                    NAV.go(context, RouterHub.APP_DETAIL_AppDetailActivity, "blockId", appBlock.objectId)
                }
            }
            BLOCK_TOPIC -> {
                val block: Block = item.block
                holder.setText(R.id.tv_name, block.title)
                LOAD.image(block.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    GO.topicDetail(item.block.objectId)
                }
            }
            BLOCK_TAG -> {
                val block: Block = item.block
                holder.setText(R.id.tv_name, block.title)
                LOAD.image(block.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    GO.tagDetail(item.block.objectId)
                }
            }
            BLOCK_PERMISSION -> {
                val block: Block = item.block
                holder.setText(R.id.tv_name, block.title)
                LOAD.image(block.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    when (block.subtype) {
                        PERMISSION_Hun -> {
                            NAV.go(context, RouterHub.USER_AddHunPermissionActivity, "block", block as Parcelable)
                        }
                        PERMISSION_Meta -> {
                            NAV.go(context, RouterHub.USER_AddHunPermissionActivity, "block", block as Parcelable)
                        }
                    }

                }
            }
            BLOCK_ROLE -> {
                val block: Block = item.block
                holder.setText(R.id.tv_name, block.title)
                LOAD.image(block.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    NAV.go(context, RouterHub.USER_AddRoleActivity, "block", block as Parcelable)
                }
            }
            BLOCK_IDENTITY -> {
                val block: Block = item.block
                holder.setText(R.id.tv_name, block.title)
                LOAD.image(block.simpleAvatar(), holder.getView(R.id.iv_avatar))
                holder.getView<View>(R.id.container).setOnClickListener {
                    NAV.go(context, RouterHub.USER_AddIdentityActivity, "block", block as Parcelable)
                }
            }
        }
    }

    var onClick: ((Block) -> Unit)? = null

    private fun setMomentContent(holder: BaseViewHolder, block: Block) {
        val mb = MomentBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, mb.atScope, mb.topicScope)
        setRelayScope(holder, block, mb.relayScope)
        setMediaScope(holder, block, mb.mediaScope)
        setPosScope(holder, block, mb.posScope)
        setOnItemClick(holder) {
            GO.momentDetail(block.objectId)
        }
    }

    private fun setPostContent(holder: BaseViewHolder, block: Block) {
        val mb = PostBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, mb.atScope, mb.topicScope)
        setMediaScope(holder, block, mb.mediaScope)
        setPosScope(holder, block, mb.posScope)
        setOnItemClick(holder) {
            GO.postDetail(block.objectId)
        }
    }

    private fun setCommentContent(holder: BaseViewHolder, block: Block) {
        val head = CommentBlock.fromJson(block.structure)
        setRichTextView(holder, block.content, head.atScope, head.topicScope)
        setMediaScope(holder, block, head.mediaScope)
        setPosScope(holder, block, head.posScope)
        when (block.subtype) {
            COMMENT_SIMPLE -> {
                setOnItemClick(holder) {
                    GO.commentDetail(block.objectId)
                }
            }
            COMMENT_REPLY -> {
                setOnItemClick(holder) {
                    GO.commentDetail(block.objectId)
                }
            }
            COMMENT_SCORE -> {
                setOnItemClick(holder) {
                    GO.commentDetail(block.objectId)
                }
            }
            COMMENT_TEXT -> {
                setOnItemClick(holder) {
                    GO.commentDetail(block.objectId)
                }
            }
            COMMENT_VIDEO -> {
                setOnItemClick(holder) {
                    GO.commentDetail(block.objectId)
                }
            }
        }
    }

    private fun setRichTextView(
        holder: BaseViewHolder,
        content: String,
        atScope: AtScope? = null,
        topicScope: TopicScope? = null
    ) {
        holder.getView<RichTextView>(R.id.saying_content).apply {
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
        holder: BaseViewHolder,
        block: Block,
        mediaScope: AttachmentTail? = null
    ) {
        holder.getView<BGANinePhotoLayout>(R.id.circle_image_container).visibility = View.GONE
        mediaScope?.let {
            holder.getView<BGANinePhotoLayout>(R.id.circle_image_container).apply {
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
        holder: BaseViewHolder,
        block: Block,
        relayScope: RelayScope? = null
    ) {
        holder.getView<MomentHerfView>(R.id.momentHerf).visibility = View.GONE
        relayScope?.let {
            requestBlock {
                query = oneBlockOf(it.objectId)
                onSuccess = { data ->
                    holder.getView<MomentHerfView>(R.id.momentHerf).apply {
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
        holder: BaseViewHolder,
        block: Block,
        posScope: PosScope? = null
    ) {
        holder.getView<TextView>(R.id.position).visibility = View.GONE
        posScope?.let {
            holder.getView<TextView>(R.id.position).apply {
                if (holder.itemView.tag != block.objectId) return
                visibility = View.VISIBLE
            }
        }
    }

    private fun setOnItemClick(
        holder: BaseViewHolder,
        onClick: (View) -> Unit
    ) {
        holder.getView<View>(R.id.container).setShakelessClickListener {
            onClick(it)
        }
    }

    private fun setFooter(holder: BaseViewHolder, block: Block, item: BlockItem) {
        setHeadText(holder, R.id.footer_like, block.likeText(), null)
        setHeadText(holder, R.id.footer_comment, block.commentText(), null)
        setHeadText(holder, R.id.footer_share, block.shareText(), null)
        val like_ll = holder.getView<View>(R.id.footer_like_ll)
        setupLikeBlockButton(context, like_ll.footer_like_icon, block)
        holder.getView<View>(R.id.footer_comment_ll).apply {
            setShakelessClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setShakelessClickListener
                }
                addCommentFor(block, item)
            }
        }
        holder.getView<View>(R.id.footer_share_ll).apply {
            setShakelessClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setShakelessClickListener
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

    private fun addCommentFor(block: Block, item: BlockItem) {
        GO.addCommentFor(block)
    }

    private fun getText(count: Int): String {
        if (count == 0) return ""
        return "$count"
    }

    private fun setHead(helper: BaseViewHolder, block: Block) {
        val who = block.user
        val onClickListener = View.OnClickListener { GO.userDetail(who.objectId) }
        setHeadImage(helper, R.id.head_image, who.avatar, who.objectId)
        setHeadText(helper, R.id.head_title, who.nickName, onClickListener)
        val timeString = block.friendlyUpdateTimeText()
        helper.setVisible(R.id.head_content, true)
        if (!TextUtils.isEmpty(who.intro)) {
            setHeadText(helper, R.id.head_content, "$timeString ${who.intro}", onClickListener)
        } else {
            setHeadText(helper, R.id.head_content, "$timeString ", onClickListener)
        }
        helper.getView<View>(R.id.head_more).setShakelessClickListener {
            PopupMenu(activity, it).apply {
                inflate(R.menu.social_head)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy -> {
                            LetMeKnow.report(LetMeKnow.CLICK_TIMECAT_COPY)
                            CopyToClipboard.copy(activity, block.content)
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

    private fun setHeadText(
        helper: BaseViewHolder, @IdRes id: Int,
        item: String,
        listener: View.OnClickListener?
    ) {
        val mTextView = helper.getView<TextView>(id)
        mTextView.text = item
        listener?.let { mTextView.setOnClickListener(it) }
    }


    private fun setHeadImage(
        helper: BaseViewHolder, @IdRes id: Int,
        avatar: String,
        userId: String
    ) {
        val iv = helper.getView<CircleImageView>(id)
        LOAD.image(avatar, iv)
        iv.setOnClickListener {
            GO.userDetail(userId)
        }
    }
}
