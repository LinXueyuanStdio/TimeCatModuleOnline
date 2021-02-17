package com.timecat.module.user.game.bag

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.setPadding
import cn.leancloud.AVCloud
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.game.*
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Body
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.layout.dp
import com.timecat.module.user.R
import com.timecat.module.user.game.cube.isExpItem
import com.timecat.module.user.game.item.StepSliderButton
import com.timecat.module.user.game.item.buildRewardListItem
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_OwnItemDetailFragment)
class OwnItemDetailFragment : ItemDetailFragment() {
    @AttrValueAutowiredAnno("ownItem")
    lateinit var ownItem: OwnItem

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        item = ownItem.item
        super.onCreate(savedInstanceState)
    }

    override fun pack(head: ItemBlock): ViewGroup.() -> Unit = {
        apply(super.pack(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton("使用", count) { button, value ->
            button.isEnabled = false
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<Any?>("useItem", params).subscribe({
                MaterialDialog(requireActivity()).show {
                    title(text = "获得了")
                    val head2 = PackageItemBlock.fromJson(head.structure)
                    val items = head2.items.map { Reward(it.uuid, it.count * value) }
                    val view = buildRewardListItem(requireActivity(), items)
                    view.setPadding(10.dp)
                    customView(view = view)
                    positiveButton(R.string.ok)
                    this@OwnItemDetailFragment.dismiss()
                }
            }, {
                button.isEnabled = true
                errUsingItem(it)
            })
        }
    }

    override fun data(head: ItemBlock): ViewGroup.() -> Unit = {
        apply(super.data(head))
        val count = ownItem.count
        Body("拥有 $count")
        MaterialButton("使用") { button ->
            val id = ownItem.item.objectId
            val path: String = when {
                isExpItem(id) -> RouterHub.USER_AllOwnCubeActivity
                else -> RouterHub.USER_AllOwnCubeActivity
            }
            NAV.go(path)
        }
    }

    override fun equip(head: ItemBlock): ViewGroup.() -> Unit = {
        apply(super.equip(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton("使用", count) { button, value ->
            button.isEnabled = false
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<Any?>("useItem", params).subscribe({
                MaterialDialog(requireActivity()).show {
                    title(text = "获得了")
                    val head2 = PackageItemBlock.fromJson(head.structure)
                    val items = head2.items.map { Reward(it.uuid, it.count * value) }
                    val view = buildRewardListItem(requireActivity(), items)
                    view.setPadding(10.dp)
                    customView(view = view)
                    positiveButton(R.string.ok)
                    this@OwnItemDetailFragment.dismiss()
                }
            }, {
                button.isEnabled = true
                errUsingItem(it)
            })
        }
    }

    override fun buff(head: ItemBlock): ViewGroup.() -> Unit = {
        apply(super.buff(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton("使用", count) { button, value ->
            button.isEnabled = false
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<Any?>("useItem", params).subscribe({
                MaterialDialog(requireActivity()).show {
                    title(text = "获得了")
                    val head2 = BuffItemBlock.fromJson(head.structure)
                    positiveButton(R.string.ok)
                    this@OwnItemDetailFragment.dismiss()
                }
            }, {
                button.isEnabled = true
                errUsingItem(it)
            })
        }
    }

    override fun cube(head: ItemBlock): ViewGroup.() -> Unit = {
        apply(super.cube(head))
        val count = ownItem.count
        Body("拥有 $count")
        if (count > 0) {
            Body("多余的方块将转化为混沌石")
        }
        Body("拥有 $count")
        StepSliderButton("使用", count) { button, value ->
            button.isEnabled = false
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<Any?>("useItem", params).subscribe({
                MaterialDialog(requireActivity()).show {
                    title(text = "使用成功")
                    message(text = "多余的方块将转化为混沌石")
                    positiveButton(R.string.ok)
                    this@OwnItemDetailFragment.dismiss()
                }
            }, {
                button.isEnabled = true
                errUsingItem(it)
            })
        }
    }

    private fun errUsingItem(err: Throwable) {
        ToastUtil.e_long("出现错误：$err")
    }
}