package com.timecat.module.user.adapter.detail

import android.view.View
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.view.dsl.setupFollowUserButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_user_herf.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 一个作者
 * @usage 可关注作者，点击进入作者个人详情页
 */
class SingleAuthorItem(
    val author: User
) : BaseDetailItem<SingleAuthorItem.DetailVH>("作者") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {

        /**
         * 必须调用，初始化
         */
        fun bindBlock(user: User) {
            val curUser = UserDao.getCurrentUser()
            if (user.objectId == curUser?.objectId) {
                root.userSection.focusUser.setOnClickListener(null)
                root.userSection.userName.text = "${user.nickName}(我)"
                root.userSection.focusUser.visibility = View.GONE
            } else {
                root.userSection.userName.text = user.nickName
                setupFollowUserButton(root.context, root.userSection.focusUser, user)
            }
            LOAD.image(user.avatar, root.userSection.userHead)
            root.userSection.setOnClickListener {
                GO.userDetail(user.objectId)
            }
        }
    }

    override fun getLayoutRes(): Int = R.layout.user_base_item_user_herf

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)
        holder.bindBlock(author)
    }
}