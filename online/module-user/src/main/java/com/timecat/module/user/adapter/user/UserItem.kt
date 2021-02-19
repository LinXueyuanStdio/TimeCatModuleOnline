package com.timecat.module.user.adapter.user

import android.app.Activity
import android.text.TextUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.commonsdk.utils.LetMeKnow
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.util.CopyToClipboard
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.friendlyCreateTimeText
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_user_head.view.*

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

    }

    override fun getLayoutRes(): Int = R.layout.user_base_item_user_head

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
        holder.apply {
            IconLoader.loadIcon(activity, root.head_image, user.avatar)
            root.head_image.setShakelessClickListener {
                GO.userDetail(user.objectId)
            }
            root.head_title.setText(user.nickName)
            root.head_title.setShakelessClickListener {
                GO.userDetail(user.objectId)
            }
            if (!TextUtils.isEmpty(user.intro)) {
                root.head_content.setText("$timeString | ${user.intro}")
            } else {
                root.head_content.setText("$timeString")
            }
            root.head_content.beVisible()
            root.head_content.setShakelessClickListener {
                GO.userDetail(user.objectId)
            }
            root.head_more.setShakelessClickListener {
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

}