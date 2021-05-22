package com.timecat.module.user.game.shop.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.buyItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.PackageItemBlock
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.CenterBody
import com.timecat.layout.ui.business.form.Divider
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.business.form.StepCounter
import com.timecat.layout.ui.business.setting.CenterIconItem
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.layout.dp
import com.timecat.module.user.game.item.BigTitle
import com.timecat.module.user.game.item.RewardList
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_BuyItemFragment)
open class BuyItemFragment : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("item")
    @JvmField
    var item: Block? = null

    @AttrValueAutowiredAnno("value")
    @JvmField
    var value: Int = 99999999

    @AttrValueAutowiredAnno("max")
    @JvmField
    var max: Int = 0

    lateinit var shopViewModel: ShopViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
        shopViewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    open fun buildView(context: Context): View {
        val container = ContainerItem(context)
        val structure = item!!.structure
        val head = ItemBlock.fromJson(structure)
        val apply = when (item!!.subtype) {
            ITEM_Thing -> thing(head)
            ITEM_Package -> pack(head)
            ITEM_Data -> data(head)
            ITEM_Equip -> equip(head)
            ITEM_Buff -> buff(head)
            ITEM_Cube -> cube(head)
            else -> thing(head)
        }
        container.apply(apply)
        return container
    }

    open fun thing(head: ItemBlock): ViewGroup.() -> Unit = {
        Icon(head)
        Title()
        Content()

        Buy()
    }

    open fun pack(head: ItemBlock): ViewGroup.() -> Unit = {
        Icon(head)
        Title()
        Content()

        val head2 = PackageItemBlock.fromJson(head.structure)
        val items = head2.items
        RewardList(requireActivity(), items)

        Buy()
    }

    open fun data(head: ItemBlock): ViewGroup.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun equip(head: ItemBlock): ViewGroup.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun buff(head: ItemBlock): ViewGroup.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun cube(head: ItemBlock): ViewGroup.() -> Unit = {
        Icon(head)
        Title()
        Content()

        Buy()
    }

    open fun ViewGroup.Buy() {
        val counter = StepCounter(1, 1, 1, max)
        MaterialButton("购买") {
            val count = counter.value
            buy(item!!.objectId, count, value, shopViewModel.money.value!!.objectId)
        }
    }

    fun buy(
        goodId: String,
        count: Int,
        value: Int,
        moneyId: String
    ) {
        shopViewModel attach buyItem<Any?>(goodId, count, value, moneyId) {

        }
    }

    open fun ViewGroup.Icon(head: ItemBlock) {
        Divider().apply {
            alpha = 0f
            updateLayoutParams<LinearLayout.LayoutParams> {
                height = 20.dp
            }
        }
        val iconItem = CenterIconItem(context).apply {
            icon = head.header.avatar
        }
        addView(iconItem)
    }

    open fun ViewGroup.Title() {
        BigTitle(item!!.title)
    }

    open fun ViewGroup.Content() {
        val content = item!!.content
        if (content.isNotEmpty()) {
            CenterBody(content)
        }
    }
}