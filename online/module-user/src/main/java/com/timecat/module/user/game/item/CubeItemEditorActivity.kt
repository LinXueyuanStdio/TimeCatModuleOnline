package com.timecat.module.user.game.item

import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.afollestad.vvalidator.form
import com.afollestad.vvalidator.form.Form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allIdentity
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.CubeItemBlock
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.type.ITEM_Cube
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.timecat.module.user.view.item.MaterialForm
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeItemEditorActivity)
class CubeItemEditorActivity : BaseItemAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var item: Block? = null
    override fun title(): String = "方块"
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
        formData.titleItem = OneLineInput("标题", "新建方块")
        formData.blockItem = Next("方块") {
            chooseCube()
        }
        add(
            formData.iconItem to 0,
            formData.titleItem to 1,
            formData.blockItem  to 2,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
    }

    override fun currentBlock(): Block? = item

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            formData.titleItem.inputEditText.hasFocus() -> formData.iconItem.height
            emojiEditText.hasFocus() -> formData.iconItem.height + formData.titleItem.height
            else -> 0
        }
    }

    fun chooseCube() {
        mStatefulLayout?.showLoading()
        requestBlock {
            query = allIdentity()
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long(it.msg)
            }
            onEmpty = {
                mStatefulLayout?.showContent()
                ToastUtil.w("空")
            }
            onComplete = {
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                showSelectDialog(it)
            }
        }
    }

    fun showSelectDialog(items: List<Block>) {
        MaterialDialog(this, BottomSheet()).show {
            title(text = "选择方块")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsSingleChoice(items = texts) { _, idx, _ ->
                val cube = items[idx]
                formData.title = cube.title
                formData.uuid = cube.objectId
                formData.content = cube.content
            }
        }
    }

    override fun subtype() = ITEM_Cube
    override fun getItemBlock(): ItemBlock {
        return ItemBlock(
            type = subtype(),
            structure = CubeItemBlock(formData.uuid).toJsonObject(),
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