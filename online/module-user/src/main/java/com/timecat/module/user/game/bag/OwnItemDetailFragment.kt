package com.timecat.module.user.game.bag

import android.os.Bundle
import cn.leancloud.AVCloud
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.game.item.Button
import com.timecat.module.user.game.item.StepSliderButton
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

    override fun pack(head: ItemBlock): MaterialForm.() -> Unit = {
        apply(super.pack(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton(count, "使用") { _, value ->
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<String>("useItem", params).subscribe({

            }, {
                errUsingItem(it)
            })
        }
    }

    override fun data(head: ItemBlock): MaterialForm.() -> Unit = {
        apply(super.data(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton(count, "使用") { _, value ->
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<String>("useItem", params).subscribe({

            }, {
                errUsingItem(it)
            })
        }
    }

    override fun equip(head: ItemBlock): MaterialForm.() -> Unit = {
        apply(super.equip(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton(count, "使用") { _, value ->
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<String>("useItem", params).subscribe({

            }, {
                errUsingItem(it)
            })
        }
    }

    override fun buff(head: ItemBlock): MaterialForm.() -> Unit = {
        apply(super.buff(head))
        val count = ownItem.count
        Body("拥有 $count")
        StepSliderButton(count, "使用") { _, value ->
            val params = mutableMapOf<String, Any>()
            params["ownItemId"] = ownItem.objectId
            params["count"] = value
            AVCloud.callFunctionInBackground<String>("useItem", params).subscribe({

            }, {
                errUsingItem(it)
            })
        }
    }

    private fun errUsingItem(err: Throwable) {
        ToastUtil.e_long("出现错误：$err")
    }
}