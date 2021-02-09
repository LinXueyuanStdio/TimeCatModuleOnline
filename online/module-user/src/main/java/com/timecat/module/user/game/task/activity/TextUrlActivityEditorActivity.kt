package com.timecat.module.user.game.task.activity

import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.ext.Activity
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.ActivityBlock
import com.timecat.identity.data.block.ActivityUrlBlock
import com.timecat.identity.data.block.type.ACTIVITY_Text_url
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.recieveImage

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
class TextUrlActivityEditorActivity : BaseActivityAddActivity() {

    override fun title(): String = "文本活动"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var cover: String = "R.drawable.ic_folder",
        var name: String = "新建活动",
        var content: String = "",
        var url: String = "",
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var coverItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var urlItem: InputItem
    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "图标"
                setImage(formData.icon)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        recieveImage(I(), listOf(path), false) {
                            formData.icon = it.first()
                        }
                    }
                }

                container.addView(this, 0)
            }
            coverItem = ImageItem(windowContext).apply {
                title = "背景图"
                setImage(formData.cover)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        recieveImage(I(), listOf(path), false) {
                            formData.cover = it.first()
                        }
                    }
                }

                container.addView(this, 1)
            }
            titleItem = InputItem(windowContext).apply {
                hint = "名称"
                text = formData.name
                onTextChange = {
                    formData.name = it ?: ""
                }

                container.addView(this, 2)
            }
            urlItem = InputItem(windowContext).apply {
                hint = "url"
                text = formData.url
                onTextChange = {
                    formData.url = it ?: ""
                }

                container.addView(this, 3)
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }
                inputLayout(urlItem.inputLayout) {
                    isNotEmpty().description("请输入url!")
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
            urlItem.inputEditText.hasFocus() -> imageItem.height + titleItem.height
            emojiEditText.hasFocus() -> imageItem.height + titleItem.height + urlItem.height
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
            target = I() create Activity {
                title = formData.name
                content = formData.content
                subtype = ACTIVITY_Text_url
                headerBlock = ActivityBlock(
                    type = ACTIVITY_Text_url,
                    structure = ActivityUrlBlock(formData.url).toJson(),
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
                        cover = formData.cover,
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