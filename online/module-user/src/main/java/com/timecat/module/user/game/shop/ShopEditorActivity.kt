package com.timecat.module.user.game.shop

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
import com.timecat.data.bmob.ext.Shop
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.SHOP_Basic
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.layout.ui.business.setting.OwnCountItem
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.timecat.module.user.ext.chooseAvatar
import com.timecat.module.user.ext.receieveImage
import com.timecat.module.user.game.cube.SendCubeActivity
import com.timecat.module.user.game.item.showItemDialog
import com.timecat.module.user.search.SelectActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/23
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_ShopEditorActivity)
class ShopEditorActivity : BaseBlockEditorActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var shop: Block? = null

    override fun title(): String = "商店"
    override fun routerInject() = NAV.inject(this)

    var formItems: MutableMap<String, Long> = mutableMapOf()
    var formRewardListItems: List<Reward>
        get() = formItems.toList().map { Reward(it.first, it.second) }
        set(value) {
            formItems = mutableMapOf(*value.map {
                it.uuid to it.count
            }.toTypedArray())
        }

    lateinit var packageItem: NextItem
    lateinit var packageDetailContainer: LinearLayout

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = ShopBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        formData.icon = head.header.avatar
        val h2 = BasicShopBlock.fromJson(head.structure)
        formRewardListItems = h2.goods.map { Reward(it.itemId, it.value) }
        formData.blockId = h2.moneyId
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "fontawesome://regular/fa_envelope", autoAdd = false) {
            chooseAvatar { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新邮件", autoAdd = false)
        formData.blockItem = Next("货币") {
            selectBlock(it)
        }
        packageItem = Next("商品",
            hint = formItems.toString(),
            initialText = "${formItems.size}",
            autoAdd = false
        ) {
            selectItems()
        }
        packageDetailContainer = ContainerItem(context)

        add(
            formData.iconItem to 0,
            formData.titleItem to 1,
            formData.blockItem to 2,
            packageItem to 3,
            packageDetailContainer to 4,
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
                    hint = "单价"
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

    private fun selectBlock(nextItem: NextItem) {
        val types = ArrayList<String>()
        types.add(SelectActivity.SelectType.Item.title)
        NAV.raw(this, RouterHub.SEARCH_SelectActivity)
            .withStringArrayList("types", types)
            .requestCode(SendCubeActivity.SEARCH_IDENTITY)
            .navigation()
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
                formRewardListItems = blocks.map { Reward(it.objectId, 1) }
                setBlockItems(blocks)
                packageItem.hint = formItems.toString()
                packageItem.text = "${formItems.size}"
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

    override fun currentBlock(): Block? = shop
    override fun subtype() = SHOP_Basic
    override fun savableBlock(): Block = I() create Shop {
        title = formData.title
        content = formData.content
        subtype = subtype()
        headerBlock = getHeadBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
        subtype = subtype()
        structure = getHeadBlock().toJson()
    }

    fun getHeadBlock(): ShopBlock {
        return ShopBlock(
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            ),
            topicScope = formData.topicScope,
            atScope = formData.atScope,
            mediaScope = formData.attachments,
            structure = BasicShopBlock(
                formData.blockId,
                formRewardListItems.map {
                    Good(it.uuid, it.count)
                }
            ).toJsonObject()
        )
    }
}