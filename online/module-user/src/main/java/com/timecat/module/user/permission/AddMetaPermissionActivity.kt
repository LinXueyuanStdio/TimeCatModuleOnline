package com.timecat.module.user.permission

import android.view.ViewGroup
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.MetaPermission
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkMetaPermExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 创建元权限
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddMetaPermissionActivity)
class AddMetaPermissionActivity : BaseBlockEditActivity() {
    @AttrValueAutowiredAnno("block")
    @JvmField
    var block: Block? = null

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建元权限"

    data class FormData(
        var name: String = "id",
        var content: String = ""
    )

    val formData: FormData = FormData()

    override fun addSettingItems(container: ViewGroup) {
        block?.let {
            setTitle("编辑元权限")
            formData.name = it.title
            formData.content = it.content
        }
        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("UI Id / API Id / Route Id", formData.name) {
                formData.name = it ?: ""
            }
            val contentItem = MultiLineInput("权限 id", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("UI Id / API Id / Route Id")
                }
                inputLayout(contentItem.inputLayout) {
                    isNotEmpty().description("权限 id")
                }

                submitWith(R.id.ok) { result ->
                    btnOk.isEnabled = false
                    ok()
                }
            }
        }
    }

    override fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            if (block != null) {
                updateBlock {
                    target = block!!.apply {
                        title = formData.name
                        content = formData.content
                    }
                    onSuccess = {
                        ToastUtil.ok("创建成功！")
                        finish()
                    }
                    onError = {
                        btnOk.isEnabled = true
                        ToastUtil.e("创建失败！${it.msg}")
                        LogUtil.e("创建失败！${it.msg}")
                    }
                }
            } else {
                requestExistBlock {
                    query = checkMetaPermExistByTitle(formData.name)
                    onError = {
                        btnOk.isEnabled = true
                        ToastUtil.e("创建失败！${it.msg}")
                        LogUtil.e("创建失败！${it.msg}")
                    }
                    onSuccess = { exist ->
                        if (exist) {
                            btnOk.isEnabled = true
                            ToastUtil.w("已存在，请修改 id ！")
                        } else {
                            save()
                        }
                    }
                }
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create MetaPermission {
                title = formData.name
                content = formData.content
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = {
                btnOk.isEnabled = true
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}