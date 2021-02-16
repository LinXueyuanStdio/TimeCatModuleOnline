package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.view.dsl.setupFollowUserButton
import kotlinx.android.synthetic.main.user_base_item_user_herf.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class UserHerfView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    lateinit var root: View

    init {
        init(context)
    }

    private fun init(context: Context, layout: Int = R.layout.user_base_item_user_herf) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        root.userSection.focusUser.tag = "关注"
    }

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
            setupFollowUserButton(context, root.userSection.focusUser, user)
        }
        LOAD.image(user.avatar, root.userSection.userHead)
        root.userSection.setOnClickListener {
            GO.userDetail(user.objectId)
        }
    }

}