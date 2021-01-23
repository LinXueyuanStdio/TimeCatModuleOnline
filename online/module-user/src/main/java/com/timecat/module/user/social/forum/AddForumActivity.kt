package com.timecat.module.user.social.forum

import android.view.ViewGroup
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.Forum
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkForumExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.NoteBody
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.ForumBlock
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
 * @description 创建论坛
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddForumActivity)
open class AddForumActivity : BaseLoginEditActivity() {
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

    override fun title(): String = "创建论坛"

    data class FormData(
        var name: String = "新建论坛",
        var content: String = "",
        var icon: String = "R.drawable.ic_folder"
    )

    val formData: FormData = FormData()

    override fun addSettingItems(container: ViewGroup) {
        name?.let { formData.name = it }
        content?.let { formData.content = it }
        icon?.let { formData.icon = it }

        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("论坛名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("备注", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入论坛名!")
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
                query = checkForumExistByTitle(formData.name)
                onError = {
                    ToastUtil.e("创建失败！${it.msg}")
                    LogUtil.e("创建失败！${it.msg}")
                }
                onSuccess = { exist ->
                    if (exist) {
                        ToastUtil.w("已存在，请修改论坛名！")
                    } else {
                        save()
                    }
                }
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create Forum {
                title = formData.name
                content = formData.content
                headerBlock = ForumBlock(
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
                GO.forumDetail(it.objectId)
                finish()
            }
            onError = {
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}