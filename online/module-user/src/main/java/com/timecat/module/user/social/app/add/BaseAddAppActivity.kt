package com.timecat.module.user.social.app.add

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import com.timecat.data.bmob.dao.block.BlockDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnSaveListener
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.timecat.module.user.ext.chooseImage

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 开发者上传 app
 * @usage null
 */
abstract class BaseAddAppActivity : BaseBlockEditorActivity() {

    override fun title(): String = "应用"
    override fun layout(): Int = R.layout.user_activity_app_add

    val content: String
        get() = description.text?.toString() ?: ""

    val title: String
        get() = name.text?.toString() ?: ""

    lateinit var name: EditText
    lateinit var description: EditText
    lateinit var icon: ImageView
    lateinit var publish: View

    override fun bindView() {
        super.bindView()
        name = findViewById(R.id.name)
        description = findViewById(R.id.description)
        icon = findViewById(R.id.icon)
        publish = findViewById(R.id.publish)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        icon.setShakelessClickListener {
            chooseImage(true) {

            }
        }
        publish.setShakelessClickListener {
            publish()
        }
    }

    private fun publish() {
        if (title.isEmpty()) {
            ToastUtil.e("名字为空")
            return
        }
        val block = Block.forApp(I(), title)
        block.content = content
        block.structure = appBlockStructure()
        BlockDao.save(block, object : OnSaveListener<Block> {
            override fun success(data: Block) {
                finish()
            }

            override fun error(e: DataError) {

            }
        })
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        TODO("Not yet implemented")
    }

    abstract fun appBlockStructure(): String
}