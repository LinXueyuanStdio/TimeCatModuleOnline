package com.timecat.module.user.social.tag

import android.text.InputType
import android.view.ViewGroup
import com.afollestad.vvalidator.form
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.Tag
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkTagExistByTitle
import com.timecat.data.bmob.ext.bmob.requestExist
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.middle.setting.MaterialForm
import com.timecat.middle.setting.BaseNewActivity
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.identity.data.base.NoteBody
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.TagBlock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description 创建标签
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddTagActivity)
open class AddTagActivity : BaseNewActivity() {
    @AttrValueAutowiredAnno("name")
    lateinit var name: String

    @AttrValueAutowiredAnno("content")
    lateinit var content: String

    @AttrValueAutowiredAnno("icon")
    lateinit var icon: String

    var I = UserDao.getCurrentUser()!!

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建标签"

    data class FormData(
        var name: String = "新建标签",
        var content: String = "",
        var icon: String = "R.drawable.ic_folder"
    )

    val formData: FormData = FormData()

    override fun addSettingItems(container: ViewGroup) {
        formData.name = name
        formData.content = content
        formData.icon = icon
        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("标签名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("备注", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入标签名!")
                }

                submitWith(R.id.ok) { result ->
                    ok()
                }
            }
        }
    }

    override fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            requestExist {
                query = checkTagExistByTitle(formData.name)
                onError = {
                    ToastUtil.e("创建失败！${it.msg}")
                    LogUtil.e("创建失败！${it.msg}")
                }
                onSuccess = {exist->
                    if (exist) {
                        ToastUtil.w("已存在，请修改标签名！")
                    } else {
                        save()
                    }
                }
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I create Tag {
                title = formData.name
                content = formData.content
                headerBlock = TagBlock(
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