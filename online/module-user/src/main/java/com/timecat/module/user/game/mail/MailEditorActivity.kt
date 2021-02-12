package com.timecat.module.user.game.mail

import android.text.InputType
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Mail
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.BLOCK_MAIL
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.MailBlock
import com.timecat.identity.data.block.Reward
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseComplexEditorActivity
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.timecat.module.user.game.item.showItemDialog
import com.timecat.module.user.view.item.OwnCountItem
import com.timecat.page.base.extension.simpleUIContainer
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description 编辑邮件
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_MailEditorActivity)
class MailEditorActivity : BaseComplexEditorActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var mail: Block? = null
    override fun title(): String = "邮件"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var name: String = "新邮件",
        var content: String = "",
        var items: MutableMap<String, Long> = mutableMapOf(),
        var attachments: AttachmentTail? = null
    ) {
        fun setRewardListItems(items: List<Reward>) {
            this.items = mutableMapOf(*items.map {
                it.uuid to it.count
            }.toTypedArray())
        }

        fun getRewardListItems(): MutableList<Reward> {
            return items.toList().map { Reward(it.first, it.second) }.toMutableList()
        }
    }

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var packageItem: NextItem
    lateinit var packageDetailContainer: LinearLayout

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        mail?.let {
            formData.name = it.title
            formData.content = it.content
            val head = MailBlock.fromJson(it.structure)
            formData.attachments = head.mediaScope
            formData.icon = head.header.avatar
            formData.setRewardListItems(head.rewards)
        }
        emojiEditText.setText(formData.content)
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "邮件图标"
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
                hint = "邮件标题"
                text = formData.name
                onTextChange = {
                    formData.name = it ?: ""
                }

                container.addView(this, 1)
            }
            packageItem = Next("礼包",
                hint = formData.items.toString(),
                initialText = "${formData.items.size}") {
                selectItems()
            }
            packageDetailContainer = simpleUIContainer(windowContext)
            container.addView(packageDetailContainer)
            setItems()

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

    fun setItems() {
        requestBlock {
            query = allItem().whereContainedIn("objectId", formData.items.keys)
            onSuccess = {
                setBlockItems(it)
            }
        }
    }

    fun setBlockItems(items: List<Block>) {
        LogUtil.e(items)
        packageDetailContainer.removeAllViews()
        for (block in items) {
            val head = ItemBlock.fromJson(block.structure)
            val itemView = OwnCountItem(this).apply {
                icon = head.header.icon
                left_field = {
                    hint = "物品"
                    text = block.title
                    inputEditText.isEnabled = false
                }
                right_field = {
                    hint = "数量"
                    text = "${formData.items[block.objectId]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formData.items[block.objectId] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onIconClick {
                    showItemDialog(block)
                }
                onCloseIconClick {
                    formData.items.remove(block.objectId)
                    packageDetailContainer.removeView(this)
                }
            }
            packageDetailContainer.addView(itemView)
        }
    }

    fun selectItems() {
        mStatefulLayout?.showLoading()
        requestBlock {
            query = allItem()
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
            title(text = "选择物品")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsMultiChoice(items = texts) { _, intArr, _ ->
                val blocks = items.filterIndexed { index, block -> index in intArr }
                formData.setRewardListItems(blocks.map { Reward(it.objectId, 1) })
                setBlockItems(blocks)
                packageItem.hint = formData.items.toString()
                packageItem.text = "${formData.items.size}"
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
        if (mail == null) {
            save()
        } else {
            update()
        }
    }

    fun subtype() = BLOCK_MAIL
    fun getHeadBlock(): MailBlock {
        val topicScope = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
        val atScope = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }
        return MailBlock(
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            ),
            topicScope = topicScope,
            atScope = atScope,
            mediaScope = formData.attachments,
            rewards = formData.getRewardListItems()
        )
    }

    fun update() {
        mail?.let {
            updateBlock {
                target = it.apply {
                    title = formData.name
                    content = formData.content
                    subtype = subtype()
                    structure = getHeadBlock().toJson()
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
            target = I() create Mail {
                title = formData.name
                content = formData.content
                subtype = subtype()
                headerBlock = getHeadBlock()
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = errorCallback
        }
    }
}