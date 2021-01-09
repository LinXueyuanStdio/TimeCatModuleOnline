package com.timecat.module.user.social.app.add

import android.text.InputType
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.App
import com.timecat.data.bmob.ext.bmob.requestExist
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkLeaderBoardExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.APP_Plugin
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.PluginApp
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.uploadImageByUser
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 上传 一个 Android 应用
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddPluginAppActivity)
class AddPluginAppActivity : BaseAddAppActivity() {
    override fun title(): String = "时光猫插件"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var name: String = "新建应用",
        var url: String = "",
        var content: String = "",
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var urlItem: InputItem
    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "应用图标"
                setImage(formData.icon)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        uploadImageByUser(I(), listOf(path), false) {
                            formData.icon = it.first()
                        }
                    }
                }

                container.addView(this, 0)
            }
            titleItem = InputItem(windowContext).apply {
                hint = "应用名"
                text = formData.name
                onTextChange = {
                    formData.name = it ?: ""
                }

                container.addView(this, 1)
            }
            urlItem = InputItem(windowContext).apply {
                hint = "下载地址（url）"
                text = formData.content
                onTextChange = {
                    formData.content = it ?: ""
                }
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE

                container.addView(this, 2)
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入应用名!")
                }
                inputLayout(urlItem.inputLayout) {
                    isNotEmpty().description("请输入应用下载地址!")
                }

                submitWith(R.id.ok) { result ->
                    publish()
                }
            }
        }
    }

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            titleItem.inputEditText.hasFocus() -> imageItem.height
            urlItem.inputEditText.hasFocus() -> imageItem.height + titleItem.height
            emojiEditText.hasFocus() -> imageItem.height + titleItem.height + urlItem.height
            else -> 0
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            requestExist {
                query = checkLeaderBoardExistByTitle(formData.name)
                onError = {
                    ToastUtil.e("创建失败！${it.msg}")
                    LogUtil.e("创建失败！${it.msg}")
                }
                onSuccess = { exist ->
                    if (exist) {
                        ToastUtil.w("已存在，请修改排行榜名！")
                    } else {
                        save()
                    }
                }
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create App {
                title = formData.name
                content = formData.content
                headerBlock = AppBlock(
                    type = APP_Plugin,
                    structure = PluginApp(
                        formData.content,
                        preview_urls = formData.attachments?.getAllPath() ?: mutableListOf()
                    ).toJson(),
                    header = PageHeader(
                        icon = formData.icon,
                        avatar = formData.icon,
                        cover = formData.icon,
                    )
                )
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                GO.appDetail(it.objectId)
                finish()
            }
            onError = {
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}