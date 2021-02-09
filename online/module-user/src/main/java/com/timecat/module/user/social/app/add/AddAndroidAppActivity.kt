package com.timecat.module.user.social.app.add

import android.text.InputType
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
import com.timecat.identity.data.block.APP_AndroidApp
import com.timecat.identity.data.block.AndroidApp
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.recieveImage
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
@RouterAnno(hostAndPath = RouterHub.USER_AddAndroidAppActivity)
class AddAndroidAppActivity : BaseAddAppActivity() {
    override fun title(): String = "Android 应用"
    override fun routerInject() = NAV.inject(this)

    data class FormData(
        var name: String = "新建应用",
        var url: String = "",
        var content: String = "",
        var icon: String = "R.drawable.ic_folder",
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        MaterialForm(this, container).apply {
            ImageItem(windowContext).apply {
                title = "图标"
                setImage(formData.icon)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        recieveImage(I(), listOf(path), false) {
                            formData.icon = it.first()
                        }
                    }
                }

                container.addView(this, 0)
            }
            val titleItem = InputItem(windowContext).apply {
                hint = "名称"
                text = formData.name
                onTextChange = {
                    formData.name = it ?: ""
                }

                container.addView(this, 1)
            }
            val urlItem = InputItem(windowContext).apply {
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
                    isNotEmpty().description("请输入名称!")
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

    override fun publish(content: String, attachments: AttachmentTail?) {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            requestExistBlock {
                query = checkLeaderBoardExistByTitle(formData.name)
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
                title = formData.name
                content = formData.url
                headerBlock = AppBlock(
                    type = APP_AndroidApp,
                    structure = AndroidApp(
                        formData.url,
                        show = formData.attachments?.getAllPath() ?: mutableListOf()
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
            onError = errorCallback
        }
    }
}