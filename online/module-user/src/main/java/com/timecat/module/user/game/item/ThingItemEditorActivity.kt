package com.timecat.module.user.game.item

import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Item
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.create
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.ThingItemBlock
import com.timecat.identity.data.block.type.ITEM_Thing
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
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
@RouterAnno(hostAndPath = RouterHub.USER_ThingItemEditorActivity)
class ThingItemEditorActivity : BaseItemAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var item: Block? = null

    override fun title(): String = "物产"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var name: String = "新建物产",
        var content: String = "",
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var titleItem: InputItem
    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        item?.let {
            formData.name = it.title
            formData.content = it.content
            val head = ItemBlock.fromJson(it.structure)
            formData.attachments = head.mediaScope
            formData.icon = head.header.avatar
        }
        emojiEditText.setText(formData.content)
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "图标"
                setImage(formData.icon)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        receieveImage(I(), listOf(path), false) {
                            formData.icon = it.first()
                            imageItem.setImage(formData.icon)
                        }
                    }
                }

                container.addView(this, 0)
            }
            titleItem = InputItem(windowContext).apply {
                hint = "名称"
                text = formData.name
                onTextChange = {
                    formData.name = it ?: ""
                }

                container.addView(this, 1)
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }

                submitWith(R.id.ok) { result ->
                    publish()
                }
            }
        }
    }

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            titleItem.inputEditText.hasFocus() -> imageItem.height
            emojiEditText.hasFocus() -> imageItem.height + titleItem.height
            else -> 0
        }
    }

    override fun release() {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        if (item == null) {
            save()
        } else {
            update()
        }
    }

    fun subtype() = ITEM_Thing
    fun getItemBlock(): ItemBlock {
        val topicScope = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
        val atScope = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }
        return ItemBlock(
            type = subtype(),
            structure = ThingItemBlock().toJsonObject(),
            mediaScope = formData.attachments,
            topicScope = topicScope,
            atScope = atScope,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            )
        )
    }

    fun update() {
        item?.let {
            updateBlock {
                target = it.apply {
                    title = formData.name
                    content = formData.content
                    subtype = subtype()
                    structure = getItemBlock().toJson()
                }
                onSuccess = {
                    ToastUtil.ok("更新成功！")
                    finish()
                }
                onError = errorCallback
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create Item {
                title = formData.name
                content = formData.content
                subtype = subtype()
                headerBlock = getItemBlock()
            }
            onSuccess = {
                ToastUtil.ok("成功！")
                finish()
            }
            onError = errorCallback
        }
    }
}