package com.timecat.module.user.game.item

import android.text.InputType
import android.view.ViewGroup
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form.Form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.PackageItemBlock
import com.timecat.identity.data.block.Reward
import com.timecat.identity.data.block.type.ITEM_Package
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.layout.ui.business.setting.*
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseAvatar
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receiveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_PackageItemEditorActivity)
class PackageItemEditorActivity : BaseItemAddActivity() {

    @AttrValueAutowiredAnno("block")
    var item: Block? = null
    override fun title(): String = "礼包"
    override fun routerInject() = NAV.inject(this)

    var formRewardItems: MutableMap<String, Long> = mutableMapOf()
    var formRewardListItems: List<Reward>
        get() = formRewardItems.toList().map { Reward(it.first, it.second) }
        set(value) {
            formRewardItems = mutableMapOf(*value.map {
                it.uuid to it.count
            }.toTypedArray())
        }

    lateinit var packageItem: NextItem
    lateinit var packageDetailContainer: LinearLayout

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = ItemBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        formData.icon = head.header.avatar
        val head2 = PackageItemBlock.fromJson(head.structure)
        formRewardListItems = head2.items
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "fontawesome://solid/fa_gift_solid", autoAdd = false) {
            chooseAvatar { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建礼包", autoAdd = false)
        packageItem = Next("礼包",
            hint = formRewardItems.toString(),
            initialText = "${formRewardItems.size}",
            autoAdd = false
        ) {
            selectItems()
        }
        packageDetailContainer = ContainerItem(context)

        add(
            formData.iconItem to 0,
            formData.titleItem to 1,
            packageItem to 2,
            packageDetailContainer to 3,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        setItems()
    }

    fun setItems() {
        requestBlock {
            query = allItem().whereContainedIn("objectId", formRewardItems.keys)
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
            val itemView = OwnCountItem(this@PackageItemEditorActivity).apply {
                icon = head.header.icon
                left_field = {
                    hint = "物品"
                    text = block.title
                    inputEditText.isEnabled = false
                }
                right_field = {
                    hint = "数量"
                    text = "${formRewardItems[block.objectId]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formRewardItems[block.objectId] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onIconClick {
                    showItemDialog(block)
                }
                onCloseIconClick {
                    formRewardItems.remove(block.objectId)
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
            title(text = "选择物品放入礼包中")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsMultiChoice(items = texts) { _, intArr, _ ->
                val blocks = items.filterIndexed { index, block -> index in intArr }
                formRewardListItems = blocks.map { Reward(it.objectId, 1) }
                setBlockItems(blocks)
                packageItem.hint = formRewardItems.toString()
                packageItem.text = "${formRewardItems.size}"
            }
        }
    }

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        var h = formData.iconItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
    }

    override fun currentBlock(): Block? = item
    override fun subtype() = ITEM_Package

   override fun getItemBlock(): ItemBlock {
        return ItemBlock(
            structure = PackageItemBlock(
                formRewardListItems
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