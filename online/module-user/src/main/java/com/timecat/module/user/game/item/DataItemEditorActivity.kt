package com.timecat.module.user.game.item

import android.text.InputType
import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Item
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.create
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.DataItemBlock
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.PackageItemBlock
import com.timecat.identity.data.block.type.ITEM_Data
import com.timecat.identity.data.block.type.ITEM_Package
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
@RouterAnno(hostAndPath = RouterHub.USER_DataItemEditorActivity)
class DataItemEditorActivity : BaseItemAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var item: Block? = null
    override fun title(): String = "数据"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var name: String = "新建数据",
        var content: String = "",
        var where: String = "",
        var num: Long = 0,
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var whereItem: InputItem
    lateinit var numItem: InputItem
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
            whereItem = InputItem(windowContext).apply {
                hint = "字段名称"
                text = formData.where
                onTextChange = {
                    formData.where = it ?: ""
                }

                container.addView(this, 2)
            }
            numItem = InputItem(windowContext).apply {
                hint = "数量"
                text = "${formData.num}"
                onTextChange = {
                    formData.num = it?.toLong() ?: 0
                }
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

                container.addView(this, 3)
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }
                inputLayout(whereItem.inputLayout) {
                    isNotEmpty().description("请输入字段名称!")
                }
                inputLayout(numItem.inputLayout) {
                    isNotEmpty().description("请输入数量!")
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
            whereItem.inputEditText.hasFocus() -> imageItem.height + titleItem.height
            numItem.inputEditText.hasFocus() -> imageItem.height + titleItem.height + whereItem.height
            emojiEditText.hasFocus() -> imageItem.height + titleItem.height + whereItem.height + numItem.height
            else -> 0
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
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
    fun subtype() = ITEM_Data
    fun getItemBlock(): ItemBlock {
        val topicScope = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
        val atScope = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }
        return ItemBlock(
            type = subtype(),
            structure = DataItemBlock(
                where = formData.where,
                num = formData.num
            ).toJson(),
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
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = errorCallback
        }
    }
}