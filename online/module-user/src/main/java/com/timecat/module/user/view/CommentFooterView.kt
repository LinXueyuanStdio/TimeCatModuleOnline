package com.timecat.module.user.view

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentActivity
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.social.share.showShare
import com.timecat.module.user.view.dsl.setupLikeBlockButton

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class CommentFooterView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    init {
        init(context)
    }

    lateinit var root: View
    lateinit var response: TextView
    lateinit var comment: TextView
    lateinit var like: TextView
    lateinit var star: TextView
    lateinit var share: TextView

    private fun init(context: Context, layout: Int = R.layout.user_base_comment_footer) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        response = root.findViewById(R.id.write_response)
        comment = root.findViewById(R.id.comment)
        like = root.findViewById(R.id.like)
        star = root.findViewById(R.id.star)
        share = root.findViewById(R.id.share)
    }

    protected lateinit var activity: Activity

    /**
     * 必须调用，初始化
     */
    fun bindBlock(activity: FragmentActivity, block: Block) {
        this.activity = activity
        response.setShakelessClickListener {
            GO.replyComment(block)
        }
        comment.text = "${block.comments}"
        like.text = "${block.likes}"
        star.text = "${block.stars}"
        share.text = "${block.relays}"
        setupLikeBlockButton(activity, like, block)
        star.setShakelessClickListener {
            showShare(activity.supportFragmentManager, block)
        }
        share.setShakelessClickListener {
            showShare(activity.supportFragmentManager, block)
        }
    }

}