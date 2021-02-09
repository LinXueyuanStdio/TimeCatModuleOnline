package com.timecat.module.user.game.bag

import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.Item
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.ThingItemBlock
import com.timecat.identity.data.block.type.ITEM_Thing
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.timecat.module.user.ext.receieveImage
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
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "图标"
                setImage(formData.icon)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                       receieveImage(I(), listOf(path), false) {
                            formData.icon = it.first()
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

    override fun publish(content: String, attachments: AttachmentTail?) {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        save()
    }

    open fun save() {
        saveBlock {
            target = I() create Item {
                title = formData.name
                content = formData.content
                subtype = ITEM_Thing
                headerBlock = ItemBlock(
                    type = ITEM_Thing,
                    structure = ThingItemBlock().toJson(),
                    mediaScope = formData.attachments,
                    topicScope = TopicScope(emojiEditText.realTopicList.map {
                        TopicItem(it.topicName, it.topicId)
                    }.toMutableList()),
                    atScope = AtScope(emojiEditText.realUserList.map {
                        AtItem(it.user_name, it.user_id)
                    }.toMutableList()),
                    header = PageHeader(
                        icon = formData.icon,
                        avatar = formData.icon,
                        cover = formData.icon,
                    )
                )
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = errorCallback
        }
    }
}