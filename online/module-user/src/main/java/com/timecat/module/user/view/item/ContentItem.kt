package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.shuyu.textutillib.RichTextView
import com.shuyu.textutillib.listener.SpanUrlCallBack
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.identity.Attr
import com.timecat.extend.arms.BaseApplication
import com.timecat.layout.ui.layout.padding
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.mySpanCreateListener
import com.timecat.module.user.game.cube.CubeLevel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class ContentItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.style.TimeCatWidget_EditText_MomentLike
) : RichTextView(context, attrs, defStyleAttr) {
    init {
        padding = 10
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
                HERF.gotoUrl(BaseApplication.getContext(), url)
            }
        })
        setSpanCreateListener(mySpanCreateListener)
        setSpanTopicCallBackListener { _, topicModel ->
            GO.topicDetail(topicModel.topicId)
        }
        setSpanAtUserCallBackListener { _, userModel ->
            GO.userDetail(userModel.user_id)
        }
    }
}