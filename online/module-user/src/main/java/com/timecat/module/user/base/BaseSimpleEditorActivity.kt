package com.timecat.module.user.base

import android.widget.LinearLayout
import android.widget.TextView
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginEditorActivity
import com.timecat.module.user.view.item.MaterialForm

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 重量级编辑器 基础版
 * @usage 简化添加 社区 block 流程
 * title + container + ok + rv(必须)
 */
abstract class BaseSimpleEditorActivity : BaseLoginEditorActivity() {
    override fun layout(): Int = R.layout.user_activity_simple_add

    lateinit var titleTv: TextView
    lateinit var container: LinearLayout
    lateinit var formData : MaterialForm
    override fun bindView() {
        super.bindView()
        titleTv = findViewById(R.id.title_tv)
        container = findViewById(R.id.container)
        formData = MaterialForm()
    }

    override fun initViewAfterLogin() {
        titleTv.setText(title())
    }
}