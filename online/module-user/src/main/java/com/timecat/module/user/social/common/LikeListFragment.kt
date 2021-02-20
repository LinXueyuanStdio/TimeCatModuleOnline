package com.timecat.module.user.social.common


import androidx.fragment.app.FragmentActivity
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.ext.net.allLikes
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.adapter.user.UserItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题讨论
 * @usage null
 */
class LikeListFragment : BaseActionListFragment() {
    override fun name(): String = "点赞"
    override fun query() = blockViewModel.block.value!!.allLikes().apply {
        include("user")
    }
    override fun action2Item(activity: FragmentActivity, action: Action): BaseItem<out BaseDetailVH> {
        return UserItem(activity, action.user)
    }
}