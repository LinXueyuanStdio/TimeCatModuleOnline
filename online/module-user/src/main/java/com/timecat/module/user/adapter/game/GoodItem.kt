package com.timecat.module.user.adapter.game

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ItemBlock
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.game.item.showItemDialog
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description 需要传入货币，表示用什么货币来购买本货物
 * @usage null
 */
class GoodItem(
    val activity: FragmentActivity,
    val good: GoodBlock,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<GoodItem.DetailVH>(good.item.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.user_item_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = good.item.friendlyCreateTimeText()
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        val item = good.item
        val title = item.title
        holder.tv_name.setText(title)

        val structure = item.structure
        val head = ItemBlock.fromJson(structure)
        LOAD.image(head.header.avatar, holder.iv_avatar)
        holder.root.safeClick {
            activity.showItemDialog(item)
        }
        holder.root.setOnLongClickListener {
            activity.showItemDialog(item)
            true
        }
    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}

data class GoodBlock(
    /**
     * 商品
     */
    val item: Block,
    /**
     * 价格
     */
    val value: Int,
    /**
     * 限购
     */
    val max: Int
)