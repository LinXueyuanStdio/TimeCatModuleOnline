package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.data.bmob.data.common.Block
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.type.BLOCK_COMMENT
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import kotlinx.android.synthetic.main.user_base_item_card_herf.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class MomentHerfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    lateinit var root: View

    init {
        init(context)
    }

    private fun init(context: Context, layout: Int = R.layout.user_base_item_card_herf) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        root.herf_go.tag = "关注"
    }

    fun isNotExist() {
        root.herf_intro.text = "原块不可见"
        root.herf_title.visibility = View.GONE
        root.herf_avatar.visibility = View.GONE
        root.herf_go.visibility = View.GONE
    }

    fun setRelay(block: Block) {
        root.herf_title.text = when (block.type) {
            BLOCK_MOMENT -> "转发动态"
            BLOCK_COMMENT -> "转发评论"
            else -> "转发动态"
        }
    }

    /**
     * 必须调用，初始化
     */
    fun bindBlock(block: Block) {
        root.herf_title.text = block.title
        root.herf_intro.text = block.content
        setupFollowBlockButton(context, root.herf_go, block)
        loadAvatar(block)
        root.setOnClickListener {
            when (block.type) {
                BLOCK_COMMENT -> GO.commentDetail(block.objectId)
                BLOCK_MOMENT -> GO.momentDetail(block.objectId)
                else -> {
                    ToastUtil.e_long("不支持的格式，请升级")
                }
            }
        }
    }

    private fun loadAvatar(block: Block) {
        LOAD.image(
            when (block.type) {
                BLOCK_COMMENT -> {
                    val appBlock = AppBlock.fromJson(block.structure)
                    appBlock.header?.avatar ?: "R.drawable.ic_comment"
                }
                BLOCK_MOMENT -> "R.drawable.ic_cloud_white_24dp"
                else -> "R.drawable.ic_block_type_accent_24dp"
            }, root.herf_avatar
        )

    }

}