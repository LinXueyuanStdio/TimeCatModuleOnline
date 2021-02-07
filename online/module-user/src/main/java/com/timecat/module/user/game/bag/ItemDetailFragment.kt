package com.timecat.module.user.game.bag

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.setting.MaterialForm
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
class ItemDetailFragment : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("ownItem")
    lateinit var ownItem: OwnItem

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    fun buildView(context: Context): View {
        val container = simpleUIContainer(context)
        val apply = when (ownItem.item.subtype) {
            ITEM_Thing -> thing()
            ITEM_Package -> pack()
            ITEM_Data -> data()
            ITEM_Equip -> equip()
            ITEM_Buff->buff()
            else -> thing()
        }
        MaterialForm(context, container).apply(apply)
        return container
    }

    fun thing(): MaterialForm.() -> Unit = {
        H2(ownItem.item.title).apply {
            gravity = Gravity.CENTER
        }
        Body(ownItem.item.content)
        Body("拥有 ${ownItem.count}")
    }

    fun pack(): MaterialForm.() -> Unit = {
        H2(ownItem.item.title).apply {
            gravity = Gravity.CENTER
        }
        Body(ownItem.item.content)
        Body("拥有 ${ownItem.count}")

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
    }

    fun data(): MaterialForm.() -> Unit = {
        H2(ownItem.item.title).apply {
            gravity = Gravity.CENTER
        }
        Body(ownItem.item.content)
        Body("拥有 ${ownItem.count}")

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
    }

    fun equip(): MaterialForm.() -> Unit = {
        H2(ownItem.item.title).apply {
            gravity = Gravity.CENTER
        }
        Body(ownItem.item.content)
        Body("拥有 ${ownItem.count}")

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
    }
    fun buff(): MaterialForm.() -> Unit = {
        H2(ownItem.item.title).apply {
            gravity = Gravity.CENTER
        }
        Body(ownItem.item.content)
        Body("拥有 ${ownItem.count}")

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
    }

}