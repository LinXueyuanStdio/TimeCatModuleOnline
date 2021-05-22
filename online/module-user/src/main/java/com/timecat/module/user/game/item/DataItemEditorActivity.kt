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
import com.timecat.module.user.ext.chooseAvatar
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
        val head = ItemBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        formData.icon = head.header.avatar
        val head2 = DataItemBlock.fromJson(head.structure)
        formData.tableName = head2.tableName
        formData.where = head2.where
        formData.num = head2.num
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder", autoAdd = false) {
            chooseAvatar { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建装备", autoAdd = false)
        formData.tableNameItem = OneLineInput("表", "OwnCube", autoAdd = false)
        formData.whereItem = OneLineInput("字段", "exp", autoAdd = false)
        formData.numItem = NumberInput("数量", "0", autoAdd = false)
        add(
            formData.iconItem to 0,
            formData.titleItem to 1,
            formData.tableNameItem to 2,
            formData.whereItem to 3,
            formData.numItem to 4,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
        inputLayout(formData.tableNameItem.inputLayout) {
            isNotEmpty().description("请输入表!")
        }
        inputLayout(formData.whereItem.inputLayout) {
            isNotEmpty().description("请输入字段!")
        }
        inputLayout(formData.numItem.inputLayout) {
            isNotEmpty().description("请输入数量!")
        }
    }

    override fun currentBlock(): Block? = item

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        var h = formData.iconItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (formData.tableNameItem.inputEditText.hasFocus()) return h
        h += formData.tableNameItem.height
        if (formData.whereItem.inputEditText.hasFocus()) return h
        h += formData.whereItem.height
        if (formData.numItem.inputEditText.hasFocus()) return h
        h += formData.numItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
    }

    override fun subtype() = ITEM_Data
    override fun getItemBlock(): ItemBlock {
        return ItemBlock(
            structure = DataItemBlock(
                tableName = formData.tableName,
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