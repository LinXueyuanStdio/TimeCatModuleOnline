package com.timecat.module.user.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.extend.image.IMG
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.layout.ui.business.nine.BGANinePhotoLayout
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.mySpanCreateListener
import kotlinx.android.synthetic.main.header_moment_detail.view.*
import kotlinx.android.synthetic.main.user_base_item_comment_header.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class CommentView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    lateinit var root: View

    init {
        init(context)
    }

    private fun init(context: Context, layout: Int = R.layout.header_moment_detail) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
    }

    protected lateinit var activity: Activity

    /**
     * 必须调用，初始化
     */
    fun bindBlock(activity: Activity, block: Block) {
        this.activity = activity
        root.userSection.bindBlock(block.user)
        setCommentSum(block.comments)
        val cb = CommentBlock.fromJson(block.structure)
        when (cb.type) {
            COMMENT_SIMPLE -> {
                val sc = SimpleComment.fromJson(cb.structure)
                setRichTextView(root, block.content, sc.atScope, sc.topicScope)
                setMediaScope(root, sc.mediaScope)
                setRelayScope(root, sc.relayScope)
                setPosScope(root, sc.posScope)
            }
            COMMENT_LONG -> {

            }
            COMMENT_TEXT -> {

            }
            COMMENT_VIDEO -> {

            }
        }
    }

    fun setCommentSum(count: Int) {
        root.commentSumTitle.text = "评论 ($count)"
    }

    private fun setRichTextView(
        headerView: View,
        content: String,
        atScope: AtScope? = null,
        topicScope: TopicScope? = null
    ) {
        headerView.moment_content.apply {
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
        headerView: View,
        mediaScope: AttachmentTail? = null
    ) {
        mediaScope?.let {
            headerView.circle_image_container.apply {
                val datas = it.attachmentItems.map {
                    it.attachment
                }
                if (datas.isEmpty()) {
                    return@apply
                }
                visibility = View.VISIBLE
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
        root: View,
        relayScope: RelayScope? = null
    ) {
        relayScope?.let {
            requestOneBlock {
                query = oneBlockOf(it.objectId)
                onSuccess = { data ->
                    root.momentHerf.apply {
                        visibility = View.VISIBLE
                        if (data == null) {
                            isNotExist()
                        } else {
                            bindBlock(data)
                            setRelay(data)
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
        root: View,
        posScope: PosScope? = null
    ) {
        posScope?.let {
            root.position.visibility = View.VISIBLE
        }
    }
}