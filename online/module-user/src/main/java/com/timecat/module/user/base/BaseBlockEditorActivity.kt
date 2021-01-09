package com.timecat.module.user.base

import android.view.View
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 重量级编辑器
 * @usage 简化添加 社区 block 流程
 */
abstract class BaseBlockEditorActivity : BaseComplexEditorActivity() {

    override fun layout(): Int = R.layout.user_activity_moment_add

    lateinit var add_pos: View
    lateinit var privacy: View
    lateinit var photo: View

    override fun bindView() {
        super.bindView()
        add_pos = findViewById(R.id.add_pos)
        privacy = findViewById(R.id.privacy)
        photo = findViewById(R.id.photo)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        add_pos.setOnClickListener {

        }

        privacy.setOnClickListener {

        }

        photo.setOnClickListener {
            onAddImage()
        }
    }

}