package com.timecat.module.user.adapter.user

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.view.dsl.setupFollowUserButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class SearchUserItem(
    val activity: Activity,
    val user: User
) : BaseHeaderItem<SearchUserItem.DetailVH>(user.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val img: ImageView = root.findViewById(R.id.img)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
        val follow: Button = root.findViewById(R.id.follow)
    }

    override fun getLayoutRes(): Int = R.layout.search_item_user

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = user.friendlyCreateTimeText()

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        IconLoader.loadIcon(activity, holder.img, user.avatar)
        holder.img.setShakelessClickListener {
            GO.userDetail(user.objectId)
        }
        holder.tv_name.setText(user.nickName)
        holder.tv_name.setShakelessClickListener {
            GO.userDetail(user.objectId)
        }
        setupFollowUserButton(activity, holder.follow, user)
    }

}