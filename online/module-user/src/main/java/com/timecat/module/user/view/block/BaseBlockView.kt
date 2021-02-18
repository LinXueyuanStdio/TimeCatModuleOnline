package com.timecat.module.user.view.block

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.view.updateLayoutParams
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
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.ext.mySpanCreateListener
import com.timecat.module.user.view.MomentHerfView
import com.timecat.module.user.view.ShareView
import com.timecat.module.user.view.UserHeadView
import com.timecat.module.user.view.item.BigContentItem
import kotlinx.android.synthetic.main.header_moment_detail.view.*
import kotlinx.android.synthetic.main.user_base_item_comment_header.view.*
import kotlinx.android.synthetic.main.user_base_item_moment.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
abstract class BaseBlockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init(context)
    }

    lateinit var root: View
    lateinit var placeholder: View
    lateinit var userHead: UserHeadView
    lateinit var contentItem: BigContentItem
    lateinit var circle_image_container: BGANinePhotoLayout
    lateinit var position: TextView
    lateinit var momentHerf: MomentHerfView
    lateinit var share: ShareView

    private fun init(context: Context, layout: Int = R.layout.header_moment_detail) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        placeholder = root.findViewById(R.id.placeholder)
        userHead = root.findViewById(R.id.userSection)
        contentItem = root.findViewById(R.id.moment_content)
        circle_image_container = root.findViewById(R.id.circle_image_container)
        momentHerf = root.findViewById(R.id.momentHerf)
        position = root.findViewById(R.id.position)
        share = root.findViewById(R.id.share)
    }

    protected lateinit var activity: Activity

    /**
     * 必须调用，初始化
     * @param activity 图片预览需要用到，防止OOM
     */
    abstract fun bindBlock(activity: Activity, block: Block)

    fun setPlaceholderHeight(height: Int) {
        placeholder.updateLayoutParams<LayoutParams> {
            this.height = height
        }
    }

    protected open fun setShare(block: Block) {
        share.block = block
    }

    protected open fun setHead(block: Block) {
        userHead.bindBlock(block.user)
        val timeString: String = block.friendlyCreateTimeText()
        if (!TextUtils.isEmpty(block.user.intro)) {
            userHead.content = "$timeString | ${block.user.intro}"
        } else {
            userHead.content = timeString
        }
    }

    protected open fun setRichTextView(
        headerView: View,
        content: String,
        atScope: AtScope? = null,
        topicScope: TopicScope? = null
    ) {
        contentItem.apply {
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

    protected open fun setMediaScope(
        headerView: View,
        mediaScope: AttachmentTail? = null
    ) {
        mediaScope?.let {
            circle_image_container.apply {
                val datas = it.attachmentItems.map { it.attachment }
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

    protected open fun setRelayScope(
        root: View,
        relayScope: RelayScope? = null
    ) {
        relayScope?.let {
            requestOneBlock {
                query = oneBlockOf(it.objectId)
                onSuccess = { data ->
                    momentHerf.apply {
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


    protected open fun setPosScope(
        root: View,
        posScope: PosScope? = null
    ) {
        posScope?.let {
            position.visibility = View.VISIBLE
        }
    }

}