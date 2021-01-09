package com.timecat.module.user.social.app.add

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
import com.timecat.identity.data.block.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.uploadImageByUser
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 上传 一个 iOS 应用
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddiOSAppActivity)
class AddiOSAppActivity : BaseAddAppActivity() {
    override fun title(): String = "iOS 应用"
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
        MaterialForm(this, container).apply {
            Image("应用图标") {
                chooseImage(isAvatar = true) { path ->
                    uploadImageByUser(I(), listOf(path), false) {
                        formData.icon = it.first()
                    }
                }
            }
            val titleItem = OneLineInput("应用名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("下载地址（url）", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入应用名!")
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
                    type = APP_iOS,
                    structure = iOSApp(
                        formData.content,
                        formData.attachments?.getAllPath() ?: mutableListOf()
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