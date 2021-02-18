package com.timecat.module.user.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.game.core.level
import com.timecat.module.user.view.dsl.roundGrayDrawable
import com.timecat.module.user.view.dsl.setupFollowUserButton
import kotlinx.android.synthetic.main.user_block_cube_item.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class UserHeadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    lateinit var root: View

    init {
        init(context)
    }

    lateinit var avatarView: ImageView
    lateinit var titleView: TextView
    lateinit var levelView: TextView
    lateinit var contentView: TextView
    lateinit var followView: TextView
    lateinit var moreView: ImageView
    private fun init(context: Context, layout: Int = R.layout.user_base_item_user_head) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        avatarView = root.findViewById(R.id.head_image)
        titleView = root.findViewById(R.id.head_title)
        levelView = root.findViewById(R.id.head_level)
        contentView = root.findViewById(R.id.head_content)
        followView = root.findViewById(R.id.head_follow)
        moreView = root.findViewById(R.id.head_more)

        levelView.background = roundGrayDrawable()
        followView.background = roundGrayDrawable(Attr.getAccentColor(context))
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

    var content: String = ""
        set(value) {
            contentView.text = value
            field = value
        }
    var level: Int = 1
        set(value) {
            levelView.text = "Lv.${value}"
            field = value
        }

    /**
     * 必须调用，初始化
     */
    fun bindBlock(user: User) {
        val curUser = UserDao.getCurrentUser()
        level = user.level
        icon = user.avatar
        if (user.objectId == curUser?.objectId) {
            followView.setOnClickListener(null)
            title = "${user.nickName}(我)"
            followView.visibility = View.GONE
        } else {
            title = user.nickName
            setupFollowUserButton(context, followView, user)
        }
        titleView.setShakelessClickListener {
            GO.userDetail(user.objectId)
        }
        avatarView.setShakelessClickListener {
            GO.userDetail(user.objectId)
        }
    }

}