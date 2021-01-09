package com.timecat.module.user.base

import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginEditorActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 重量级编辑器 基础版
 * @usage 简化添加 社区 block 流程
 * title + container + publish + rv(必须)
 */
abstract class BaseSimpleEditorActivity : BaseLoginEditorActivity() {
    override fun layout(): Int = R.layout.user_activity_simple_add

    lateinit var titleTv: TextView
    lateinit var publish: Button
    lateinit var container: LinearLayout

    override fun bindView() {
        super.bindView()
        titleTv = findViewById(R.id.title_tv)
        publish = findViewById(R.id.publish)
        container = findViewById(R.id.container)
    }

    override fun initViewAfterLogin() {
        titleTv.setText(title())

        publish.setShakelessClickListener {
            publish(it)
        }
    }

    abstract fun publish(view: View)

}