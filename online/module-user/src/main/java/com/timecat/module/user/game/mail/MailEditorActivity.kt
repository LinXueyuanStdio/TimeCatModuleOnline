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
import com.timecat.module.user.base.BaseBlockEditorActivity
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
class MailEditorActivity : BaseBlockEditorActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var mail: Block? = null

    override fun title(): String = "邮件"
    override fun routerInject() = NAV.inject(this)
    var formIcon: String
    get() = imageItem
    set(value) {
        imageItem.setImage(value)
    }
    var formTitle: String = "新邮件"
    var formItems: MutableMap<String, Long> = mutableMapOf()
    var formRewardListItems: List<Reward>
        get() = formItems.toList().map { Reward(it.first, it.second) }
        set(value) {
            formItems = mutableMapOf(*value.map {
                it.uuid to it.count
            }.toTypedArray())
        }

    lateinit var imageItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var packageItem: NextItem
    lateinit var packageDetailContainer: LinearLayout

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        mail?.let {
            formTitle = it.title
            formContent = it.content
            val head = MailBlock.fromJson(it.structure)
            formAttachments = head.mediaScope
            formIcon = head.header.avatar
            formRewardListItems = head.rewards
        }
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "邮件图标"
                setImage("R.drawable.ic_folder")
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        receieveImage(I(), listOf(path), false) {
                            formIcon = it.first()
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
                hint = formItems.toString(),
                initialText = "${formItems.size}") {
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
            query = allItem().whereContainedIn("objectId", formItems.keys)
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
                    text = "${formItems[block.objectId]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formItems[block.objectId] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onIconClick {
                    showItemDialog(block)
                }
                onCloseIconClick {
                    formItems.remove(block.objectId)
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

    override fun currentBlock(): Block? = mail
    override fun subtype() = BLOCK_MAIL
    override fun savableBlock(): Block = I() create Mail {
        title = formData.name
        content = formContent
        subtype = subtype()
        headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.name
        content = formContent
        subtype = subtype()
        structure = getHeadBlock().toJson()
    }

    fun getHeadBlock(): MailBlock {
        return MailBlock(
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            ),
            topicScope = formTopicScope,
            atScope = formAtScope,
            mediaScope = formAttachments,
            rewards = formData.getRewardListItems()
        )
    }
}