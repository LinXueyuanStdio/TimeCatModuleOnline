package com.timecat.module.user.game.bag

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.RelativeLayout
import androidx.core.view.updateLayoutParams
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.PackageItemBlock
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.CenterIconItem
import com.timecat.layout.ui.layout.dp
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.game.item.BigTitle
import com.timecat.module.user.game.item.RewardList
import com.timecat.module.user.game.item.buildRewardListItem
import com.timecat.page.base.extension.simpleUIContainer
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_ItemDetailFragment)
open class ItemDetailFragment : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("item")
    @JvmField
    var item: Block? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    open fun buildView(context: Context): View {
        val container = simpleUIContainer(context)
        val structure = item!!.structure
        val head = ItemBlock.fromJson(structure)
        val apply = when (item!!.subtype) {
            ITEM_Thing -> thing(head)
            ITEM_Package -> pack(head)
            ITEM_Data -> data(head)
            ITEM_Equip -> equip(head)
            ITEM_Buff -> buff(head)
            else -> thing(head)
        }
        MaterialForm(context, container).apply(apply)
        return container
    }

    open fun thing(head: ItemBlock): MaterialForm.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun pack(head: ItemBlock): MaterialForm.() -> Unit = {
        Icon(head)
        Title()
        Content()

        val head2 = PackageItemBlock.fromJson(head.structure)
        val items = head2.items
        RewardList(requireActivity(), items)
    }

    open fun data(head: ItemBlock): MaterialForm.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun equip(head: ItemBlock): MaterialForm.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun buff(head: ItemBlock): MaterialForm.() -> Unit = {
        Icon(head)
        Title()
        Content()
    }

    open fun MaterialForm.Icon(head: ItemBlock) {
        val iconItem = CenterIconItem(windowContext)
        iconItem.setImage(head.header.avatar)
        iconItem.imageView.updateLayoutParams<RelativeLayout.LayoutParams> {
            width = 50.dp
            height = 50.dp
        }
        container.addView(iconItem)
    }

    open fun MaterialForm.Title() {
        BigTitle(item!!.title)
    }

    open fun MaterialForm.Content() {
        val content = item!!.content
        if (content.isNotEmpty()) {
            Body(content)
        }
    }
}