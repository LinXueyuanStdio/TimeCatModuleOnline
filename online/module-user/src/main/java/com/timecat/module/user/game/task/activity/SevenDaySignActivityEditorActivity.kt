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
import com.timecat.module.user.ext.ImageAspectRatio
import com.timecat.module.user.ext.chooseAvatar
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receiveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_SevenDaySignActivityEditorActivity)
class SevenDaySignActivityEditorActivity : BaseActivityAddActivity() {

    @AttrValueAutowiredAnno("block")
    var task: Block? = null
    override fun title(): String = "外部链接活动"
    override fun routerInject() = NAV.inject(this)

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = ItemBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        formData.icon = head.header.avatar
        formData.cover = head.header.cover
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder", autoAdd = false) {
            chooseAvatar { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.coverItem = Image("背景图", "R.drawable.ic_folder", autoAdd = false) {
            chooseImage(ImageAspectRatio.Wallpaper) { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.cover = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建活动", autoAdd = false)
        formData.urlItem = OneLineInput("url", "", autoAdd = false)

        add(
            formData.iconItem to 0,
            formData.coverItem to 1,
            formData.titleItem to 2,
            formData.urlItem to 3,
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
        var h = formData.iconItem.height + formData.coverItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (formData.urlItem.inputEditText.hasFocus()) return h
        h += formData.urlItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
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