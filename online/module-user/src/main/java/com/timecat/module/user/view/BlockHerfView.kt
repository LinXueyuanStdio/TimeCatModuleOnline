package com.timecat.module.user.view

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.showDetail
import com.timecat.module.user.ext.simpleAvatar
import com.timecat.module.user.view.dsl.setupFollowBlockButton

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class BlockHerfView : LinearLayout {
    lateinit var root: View

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, layout: Int) : super(context) {
        init(context, layout)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
        : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int)
        : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    lateinit var userSection: LinearLayout
    lateinit var focusUser: Button
    lateinit var userHead: ImageView
    lateinit var userName: TextView
    private fun init(context: Context, layout: Int = R.layout.user_base_item_user_herf) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        userSection = root.findViewById(R.id.userSection)
        focusUser = root.findViewById(R.id.focusUser)
        userHead = root.findViewById(R.id.userHead)
        userName = root.findViewById(R.id.userName)
        focusUser.tag = "关注"
    }

    /**
     * 必须调用，初始化
     */
    fun bindBlock(block: Block) {
        userName.text = block.title
        setupFollowBlockButton(context, focusUser, block)
        LOAD.image(block.simpleAvatar(), userHead)
        userSection.setShakelessClickListener {
            block.showDetail()
        }
    }

}