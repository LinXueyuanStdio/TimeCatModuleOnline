package com.timecat.module.user.game.item

import android.view.ViewGroup
import com.afollestad.vvalidator.form.Form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.DataItemBlock
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.type.ITEM_Data
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.NumberInput
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_DataItemEditorActivity)
class DataItemEditorActivity : BaseItemAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var item: Block? = null
    override fun title(): String = "数据"
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
        formData.titleItem = OneLineInput("标题", "新建装备")
        formData.whereItem = OneLineInput("字段名称", "")
        formData.numItem = NumberInput("数量", "0")
        add(
            formData.iconItem to 0,
            formData.titleItem to 1,
            formData.whereItem to 2,
            formData.numItem to 3,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
        inputLayout(formData.whereItem.inputLayout) {
            isNotEmpty().description("请输入字段名称!")
        }
        inputLayout(formData.numItem.inputLayout) {
            isNotEmpty().description("请输入数量!")
        }
    }

    override fun currentBlock(): Block? = item

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            formData.titleItem.inputEditText.hasFocus() -> formData.iconItem.height
            formData.whereItem.inputEditText.hasFocus() -> formData.iconItem.height + formData.titleItem.height
            formData.numItem.inputEditText.hasFocus() -> formData.iconItem.height + formData.titleItem.height + formData.whereItem.height
            emojiEditText.hasFocus() -> formData.iconItem.height + formData.titleItem.height + formData.whereItem.height + formData.numItem.height
            else -> 0
        }
    }

    override fun subtype() = ITEM_Data
    override fun getItemBlock(): ItemBlock {
        return ItemBlock(
            type = subtype(),
            structure = DataItemBlock(
                where = formData.where,
                num = formData.num
            ).toJsonObject(),
            mediaScope = formData.attachments,
            topicScope = formData.topicScope,
            atScope = formData.atScope,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            )
        )
    }
}