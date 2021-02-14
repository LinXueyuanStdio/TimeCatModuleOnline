package com.timecat.module.user.game.task.activity

import android.view.ViewGroup
import com.afollestad.vvalidator.form.Form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.ActivityBlock
import com.timecat.identity.data.block.ActivityUrlBlock
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.type.ACTIVITY_Url
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AchievementActivityEditorActivity)
class AchievementActivityEditorActivity : BaseActivityAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var task: Block? = null
    override fun title(): String = "外部链接活动"
    override fun routerInject() = NAV.inject(this)

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        formData.content = content
        val head = ItemBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.icon = head.header.avatar
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder") {
            chooseImage(isAvatar = true) { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.coverItem = Image("背景图", "R.drawable.ic_folder") {
            chooseImage(isAvatar = false) { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.cover = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建活动")
        formData.urlItem = OneLineInput("url", "")

        add(
            formData.iconItem to 0,
            formData.coverItem to 0,
            formData.titleItem to 1,
            formData.urlItem to 1,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
        inputLayout(formData.urlItem.inputLayout) {
            isNotEmpty().description("请输入url!")
        }
    }

    override fun currentBlock(): Block? = task

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            formData.titleItem.inputEditText.hasFocus() -> formData.iconItem.height + formData.coverItem.height
            formData.urlItem.inputEditText.hasFocus() -> formData.iconItem.height + formData.coverItem.height + formData.titleItem.height
            emojiEditText.hasFocus() -> formData.iconItem.height + formData.coverItem.height + formData.titleItem.height + formData.urlItem.height
            else -> 0
        }
    }

    override fun subtype(): Int = ACTIVITY_Url
    override fun getItemBlock(): ActivityBlock {
        return ActivityBlock(
            type = subtype(),
            structure = ActivityUrlBlock(formData.url).toJsonObject(),
            mediaScope = formData.attachments,
            topicScope = formData.topicScope,
            atScope = formData.atScope,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.cover,
            )
        )
    }
}