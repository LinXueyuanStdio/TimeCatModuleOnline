package com.timecat.module.user.adapter.user

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.util.CopyToClipboard
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.friendlyCreateTimeText
import com.timecat.module.user.view.UserHeadView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/10
 * @description null
 * @usage null
 */
class UserItem(
    val activity: Activity,
    val user: User
) : BaseHeaderItem<UserItem.DetailVH>(user.objectId) {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val head: UserHeadView = root.findViewById(R.id.userHead)
    }

    override fun getLayoutRes(): Int = R.layout.user_item_user

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
        holder.head.bindBlock(user)
        if (!TextUtils.isEmpty(user.intro)) {
            holder.head.content = "$timeString | ${user.intro}"
        } else {
            holder.head.content = timeString
        }
        holder.head.moreView.beGone()
        holder.head.setShakelessClickListener {
            GO.userDetail(user.objectId)
        }
        holder.head.moreView.setShakelessClickListener {
            PopupMenu(activity, it).apply {
                inflate(R.menu.social_head)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.copy -> {
                            LetMeKnow.report(LetMeKnow.CLICK_TIMECAT_COPY)
                            CopyToClipboard.copy(activity, user.objectId)
                            true
                        }
                        else -> false
                    }
                }
                show()
            }
        }
    }

}