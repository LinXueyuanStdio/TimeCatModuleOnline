package com.timecat.module.user.social.leaderboard

import android.view.ViewGroup
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.LeaderBoard
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkLeaderBoardExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.NoteBody
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.LeaderBoardBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditActivity
import com.timecat.module.user.base.GO
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
@RouterAnno(hostAndPath = RouterHub.USER_AddLeaderBoardActivity)
open class AddLeaderBoardActivity : BaseBlockEditActivity() {
    @AttrValueAutowiredAnno("name")
    lateinit var name: String

    @AttrValueAutowiredAnno("content")
    lateinit var content: String

    @AttrValueAutowiredAnno("icon")
    lateinit var icon: String

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建排行榜"

    data class FormData(
        var name: String = "新建排行榜",
        var content: String = "",
        var icon: String = "R.drawable.ic_folder"
    )

    val formData: FormData = FormData()

    override fun addSettingItems(container: ViewGroup) {
        formData.name = name
        formData.content = content
        formData.icon = icon
        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("排行榜名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("备注", formData.content) {
                formData.content = it ?: ""
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入排行榜名!")
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
            target = I() create LeaderBoard {
                title = formData.name
                content = formData.content
                headerBlock = LeaderBoardBlock(
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
                GO.leaderBoardDetail(it.objectId)
                finish()
            }
            onError = {
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}