package com.timecat.module.user.developer.app

import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.dao.block.BlockDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.page.base.view.MyClickListener
import com.timecat.extend.image.IMG
import com.timecat.extend.image.selectForResult
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.middle.image.BaseImageSelectorActivity
import com.timecat.module.user.R
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnSaveListener
import kotlinx.android.synthetic.main.user_activity_app_add.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 开发者上传 app
 * @usage null
 */
abstract class BaseAddAppActivity : BaseImageSelectorActivity() {

    override fun title(): String = "应用"
    override fun layout(): Int = R.layout.user_activity_app_add

    val content: String
        get() = description.text?.toString() ?: ""

    val title: String
        get() = name.text?.toString() ?: ""

    override fun initView() {
        super.initView()
        icon.setOnClickListener(MyClickListener {
            selectIcon()
        })
        publish.setOnClickListener(MyClickListener {
            publish()
        })
    }

    fun selectIcon() {
        IMG.select(PictureSelector.create(this).openGallery(PictureMimeType.ofImage()))
            .maxSelectNum(1)
            .selectForResult {

            }
    }

    private fun publish() {
        if (title.isEmpty()) {
            ToastUtil.e("名字为空")
            return
        }
        val user  = UserDao.getCurrentUser()
        if (user == null) {
            NAV.go(RouterHub.LOGIN_LoginActivity)
            return
        }
        val block = Block.forApp(user, title)
        block.content = content
        block.structure = appBlock()
        BlockDao.save(block, object : OnSaveListener<Block> {
            override fun success(data: Block) {
                finish()
            }

            override fun error(e: DataError) {

            }
        })
    }

    abstract fun appBlock(): String
}