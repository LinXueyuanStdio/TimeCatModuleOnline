package com.timecat.module.user.social.app.add

import android.widget.EditText
import android.widget.ImageView
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseComplexEditorActivity
import com.timecat.module.user.ext.chooseImage

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 开发者上传 app
 * @usage null
 */
abstract class BaseAddAppActivity : BaseComplexEditorActivity() {

    override fun title(): String = "应用"
    override fun layout(): Int = R.layout.user_activity_app_add

    val title: String
        get() = name.text?.toString() ?: ""

    lateinit var name: EditText
    lateinit var icon: ImageView

    override fun bindView() {
        super.bindView()
        name = findViewById(R.id.name)
        icon = findViewById(R.id.icon)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        icon.setShakelessClickListener {
            chooseImage(true) {

            }
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        TODO("Not yet implemented")
    }

    abstract fun appBlockStructure(): String
}