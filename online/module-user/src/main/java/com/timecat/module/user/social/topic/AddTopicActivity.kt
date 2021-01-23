package com.timecat.module.user.social.topic

import android.view.ViewGroup
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.Topic
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkTopicExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.NoteBody
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.TopicBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.login.BaseLoginEditActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description 创建话题
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddTopicActivity)
open class AddTopicActivity : BaseLoginEditActivity() {
    @AttrValueAutowiredAnno("name")
    @JvmField
    var name: String? = null

    @AttrValueAutowiredAnno("content")
    @JvmField
    var content: String? = null

    @AttrValueAutowiredAnno("icon")
    @JvmField
    var icon: String? = null

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建话题"

    data class FormData(
        var name: String = "新建话题",
        var content: String = "",
        var icon: String = "R.drawable.ic_folder"
    )

    val formData: FormData = FormData()

    override fun addSettingItems(container: ViewGroup) {
        name?.let { formData.name = it }
        content?.let { formData.content = it }
        icon?.let { formData.icon = it }

        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("话题名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("备注", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入话题名!")
                }

                submitWith(R.id.ok) { result ->
                    ok()
                }
            }
        }
    }

    override fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            requestExistBlock {
                query = checkTopicExistByTitle(formData.name)
                onError = {
                    ToastUtil.e("创建失败！${it.msg}")
                    LogUtil.e("创建失败！${it.msg}")
                }
                onSuccess = { exist ->
                    if (exist) {
                        ToastUtil.w("已存在，请修改话题名！")
                    } else {
                        save()
                    }
                }
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create Topic {
                title = formData.name
                content = formData.content
                headerBlock = TopicBlock(
                    content = NoteBody(),
                    header = PageHeader(
                        icon = formData.icon,
                        avatar = formData.icon,
                        cover = formData.icon,
                    )
                )
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                GO.topicDetail(it.objectId)
                finish()
            }
            onError = {
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}