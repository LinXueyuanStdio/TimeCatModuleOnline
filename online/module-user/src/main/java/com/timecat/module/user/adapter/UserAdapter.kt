package com.timecat.module.user.adapter

import android.app.Activity
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import de.hdodenhof.circleimageview.CircleImageView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description null
 * @usage null
 */
class UserAdapter(
    val activity: Activity,
    val userListener: UserListener
) : BaseQuickAdapter<User, BaseViewHolder>(R.layout.user_peoplelist_item) {
    override fun convert(holder: BaseViewHolder, item: User) {
        holder.setText(R.id.name, item.nickName)
        val iv = holder.getView<CircleImageView>(R.id.image)
        IconLoader.loadIcon(activity, iv, item.avatar)
        holder.getView<View>(R.id.people).setShakelessClickListener {
            userListener.onClick(item)
        }
    }

    interface UserListener {
        fun onClick(user: User)
    }
}