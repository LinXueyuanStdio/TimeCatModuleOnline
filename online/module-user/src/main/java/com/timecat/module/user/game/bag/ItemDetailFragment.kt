package com.timecat.module.user.game.bag

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.updateLayoutParams
import cn.leancloud.AVCloud
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.CenterIconItem
import com.timecat.layout.ui.layout.dp
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

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

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
            ITEM_Buff -> buff()
            else -> thing()
        }
        MaterialForm(context, container).apply(apply)
        return container
    }

    fun thing(): MaterialForm.() -> Unit = {
        Icon()
        Title()
        Content()
    }

    fun pack(): MaterialForm.() -> Unit = {
        Icon()
        Title()
        Content()

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
        container.addView(button)
    }

    fun data(): MaterialForm.() -> Unit = {
        Icon()
        Title()
        Content()

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {
//            val item = ownItem.item
//            val head = ItemBlock.fromJson(item.structure)
//            val head2 = DataItemBlock.fromJson(head.structure)
//            when (head2.where) {
//                DataItemBlock.WHERE_UserExp -> {
//                    val I = UserDao.getCurrentUser() ?: return@setShakelessClickListener
//                    I.exp += head2.num
//                    I.saveEventually()
//                }
//            }
            val I = UserDao.getCurrentUser() ?: return@setShakelessClickListener
            I.exp
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = "1"
            AVCloud.callFunctionInBackground<String>("useItem", params).subscribe({

            }, {

            })
        }
        container.addView(button)
    }

    fun equip(): MaterialForm.() -> Unit = {
        Icon()
        Title()
        Content()

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
        container.addView(button)
    }

    fun buff(): MaterialForm.() -> Unit = {
        Icon()
        Title()
        Content()

        val button = MaterialButton(windowContext)
        button.setText("使用")
        button.setShakelessClickListener {

        }
        container.addView(button)
    }

    private fun MaterialForm.Icon() {
        val iconItem = CenterIconItem(windowContext)
        val structure = ownItem.item.structure
        val head = ItemBlock.fromJson(structure)
        iconItem.setImage(head.header.avatar)
        iconItem.imageView.updateLayoutParams<LinearLayout.LayoutParams> {
            width = 48.dp
            height = 48.dp
        }
        container.addView(iconItem)
    }

    private fun MaterialForm.Title() {
        H2(ownItem.item.title).apply {
            gravity = Gravity.CENTER
        }
    }
    private fun MaterialForm.Content() {
        Body(ownItem.item.content)
        Body("拥有 ${ownItem.count}")
    }

}