package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.drawabe.selectableItemBackground
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.view.dsl.setupFollowUserButton
import com.timecat.module.user.view.widget.UserCircleImageView

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

    lateinit var avatarView: UserCircleImageView
    lateinit var titleView: TextView
    lateinit var focusUser: TextView
    lateinit var more: View
    private fun init(context: Context, layout: Int = R.layout.user_base_item_user_herf) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        avatarView = root.findViewById(R.id.userHead)
        titleView = root.findViewById(R.id.userName)
        focusUser = root.findViewById(R.id.focusUser)
        more = root.findViewById(R.id.more)

        gravity = Gravity.CENTER
        orientation = HORIZONTAL
        background = selectableItemBackground(context)
    }

    var icon: String = ""
        set(value) {
            IconLoader.loadIcon(context, avatarView, value)
            field = value
        }

    var title: String = ""
        set(value) {
            titleView.text = value
            field = value
        }
    /**
     * 必须调用，初始化
     */
    fun bindBlock(user: User) {
        val curUser = UserDao.getCurrentUser()
        if (user.objectId == curUser?.objectId) {
            focusUser.visibility = View.GONE
            focusUser.setOnClickListener(null)
            title = "${user.nickName}(我)"
        } else {
            title = user.nickName
            setupFollowUserButton(context, focusUser, user)
        }
        icon = user.avatar
        setShakelessClickListener {
            GO.userDetail(user.objectId)
        }
    }

}