package com.timecat.module.user.social.app.add

import android.view.ViewGroup
import com.afollestad.vvalidator.form.Form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.APP_Plugin
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.PluginApp
import com.timecat.identity.data.block.UpdateInfo
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.NumberInput
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.module.user.ext.ImageAspectRatio
import com.timecat.module.user.ext.chooseAvatar
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receiveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 上传一个应用
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddPluginAppActivity)
open class AddPluginAppActivity : BaseAddAppActivity() {

    @AttrValueAutowiredAnno("block")
    var app: Block? = null

    override fun title(): String = "时光猫插件"
    override fun routerInject() = NAV.inject(this)

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = AppBlock.fromJson(structure)
        formData.icon = head.header.avatar
        val head2 = PluginApp.fromJson(head.structure)
        formData.url = head2.packageName
        formData.setContentScope(context, content, head.atScope, head.topicScope)
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder", autoAdd = false) {
            chooseAvatar { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.coverItem = Image("背景图", "R.drawable.ic_folder", autoAdd = false) {
            chooseImage(ImageAspectRatio.Wallpaper) { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.cover = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建应用", autoAdd = false)
        formData.urlItem = OneLineInput("下载地址", "http://", autoAdd = false)
        formData.packageNameItem = OneLineInput("包名", "", autoAdd = false)
        formData.versionCodeItem = NumberInput("版本号", "", autoAdd = false)
        formData.versionNameItem = OneLineInput("版本名", "", autoAdd = false)

        add(
            formData.iconItem to 0,
            formData.coverItem to 1,
            formData.titleItem to 2,
            formData.urlItem to 3,
            formData.packageNameItem to 4,
            formData.versionCodeItem to 5,
            formData.versionNameItem to 6,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
        inputLayout(formData.urlItem.inputLayout) {
            isNotEmpty().description("请输入下载地址!")
        }
        inputLayout(formData.packageNameItem.inputLayout) {
            isNotEmpty().description("请输入包名!")
        }
    }

    override fun currentBlock(): Block? = app
    override fun subtype(): Int = APP_Plugin

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        var h = formData.iconItem.height + formData.coverItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (formData.urlItem.inputEditText.hasFocus()) return h
        h += formData.urlItem.height
        if (formData.packageNameItem.inputEditText.hasFocus()) return h
        h += formData.packageNameItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
    }

    override fun getItemBlock(): AppBlock {
        return AppBlock(
            url = formData.url,
            mediaScope = formData.attachments,
            atScope = formData.atScope,
            topicScope = formData.topicScope,
            structure = PluginApp(
                formData.packageName,
                updateInfo = mutableListOf(
                    UpdateInfo(
                        "10 Kb",
                        formData.versionName,
                        formData.versionCode,
                        formData.url,
                        "更新",
                        System.currentTimeMillis()
                    )
                )
            ).toJsonObject(),
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.cover,
            )
        )
    }
}