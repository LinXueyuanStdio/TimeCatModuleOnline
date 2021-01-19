package com.timecat.module.user.adapter.game

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.identity.data.action.*
import com.timecat.identity.data.block.type.*
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.friendlyCreateTimeText
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
class MailItem(
    val activity: Activity,
    val ownMail: OwnMail,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<MailItem.DetailVH>(ownMail.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.user_block_small_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = ownMail.friendlyCreateTimeText()
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        val mail = ownMail.mail
        val title = mail.name
        val type = mail.type
        LOAD.image("R.drawable.ic_launcher", holder.iv_avatar)
        holder.root.safeClick {
        }

        val typeStr =when(type){
            else-> ""
        } 
        when (type) {

            else -> {
                holder.tv_name.setText("$typeStr ${title}")
            }
        }
    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}