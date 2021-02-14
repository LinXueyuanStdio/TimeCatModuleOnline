package com.timecat.module.user.social.app.add

import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.App
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkLeaderBoardExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.APP_Windows
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.WindowsApp
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
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
@RouterAnno(hostAndPath = RouterHub.USER_AddWindowsAppActivity)
class AddWindowsAppActivity : BaseAddAppActivity() {
    override fun title(): String = "Windows 应用"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var name: String = "新建应用",
        var content: String = "",
        var icon: String = "R.drawable.ic_folder",
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        container.apply {
            Image("图标") { formData.iconItem ->
                chooseImage(isAvatar = true) { path ->
                    receieveImage(I(), listOf(path), false) {
                        formData.icon = it.first()
                        formData.iconItem.setImage(formData.icon)
                    }
                }
            }
            val formData.titleItem = OneLineInput("名称", formData.title) {
                formData.title = it ?: ""
            }
            MultiLineInput("下载地址（url）", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(formData.titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }

                submitWith(R.id.ok) { result ->
                    publish()
                }
            }
        }
    }

    override fun release() {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            requestExistBlock {
                query = checkLeaderBoardExistByTitle(formData.title)
                onError = errorCallback
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
                title = formData.title
                content = formData.content
                headerBlock = AppBlock(
                    type = APP_Windows,
                    structure = WindowsApp(
                        formData.content,
                        formData.attachments?.getAllPath() ?: mutableListOf()
                    ).toJsonObject(),
                    header = PageHeader(
                        icon = formData.icon,
                        avatar = formData.icon,
                        cover = formData.icon,
                    )
                )
            }
            onSuccess = {
                ToastUtil.ok("成功！")
                GO.appDetail(it.objectId)
                finish()
            }
            onError = errorCallback
        }
    }
}