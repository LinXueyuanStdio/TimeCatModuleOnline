package com.timecat.module.user.view

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
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
class UserHerfView : LinearLayout {
    lateinit var root: View

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, layout: Int) : super(context) {
        init(context, layout)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
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
            root.userSection.userName.text = "${user.nick}(我)"
            root.userSection.focusUser.visibility = View.GONE
        } else {
            root.userSection.userName.text = user.nick
            setupFollowUserButton(context, root.userSection.focusUser, user)
        }
        LOAD.image(user.avatar, root.userSection.userHead)
        root.userSection.setOnClickListener {
            GO.userDetail(user.objectId)
        }
    }

}