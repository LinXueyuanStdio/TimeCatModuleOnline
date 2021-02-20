package com.timecat.module.user.view.dsl

import android.content.Context
import android.view.View
import android.widget.TextView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.User2User
import com.timecat.data.bmob.ext.bmob.deleteUserRelation
import com.timecat.data.bmob.ext.bmob.requestOneUserRelationOrNull
import com.timecat.data.bmob.ext.bmob.saveUserRelation
import com.timecat.data.bmob.ext.follow
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.drawabe.roundRectSelector
import com.timecat.layout.ui.layout.setShakelessClickListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/19
 * @description null
 * @usage null
 */
fun View.setupFollowUser(
    user: User,
    needRefresh: (() -> Unit)? = null,
    onActive: () -> Unit = {},
    onInActive: () -> Unit = {}
) = apply {
    var relation: User2User? = null
    val I = UserDao.getCurrentUser() ?: return@apply
    tag = user.objectId
    requestOneUserRelationOrNull {
        query = I.allFollow(user)
        onError = {
            LogUtil.se("onError$it")
            isEnabled = false
            onInActive()
        }
        onEmpty = {
            isEnabled = true
            onInActive()
        }
        onSuccess = {
            isEnabled = true
            relation = it
            onActive()
        }
    }
    setShakelessClickListener {
        if (relation == null) {
            saveUserRelation {
                target = I follow user
                onSuccess = {
                    needRefresh?.invoke()
                    relation = it
                    onActive()
                    ToastUtil.ok("关注成功")
                }
                onError = { e ->
                    ToastUtil.ok("关注失败")
                    LogUtil.e(e.toString())
                }
            }
        } else {
            deleteUserRelation {
                target = relation!!
                onSuccess = {
                    needRefresh?.invoke()
                    relation = null
                    onInActive()
                    ToastUtil.ok("解除关注成功")
                }
                onError = { e ->
                    ToastUtil.e("解除关注失败")
                    LogUtil.e(e.toString())
                }
            }
        }
    }
}

fun setupFollowUserButton(
    context: Context,
    button: TextView,
    user: User
) {
    button.apply {
        text = "关注"
        isEnabled = false
        val accentColor = Attr.getAccentColor(context)
        background = roundRectSelector(accentColor)
        val onActive = {
            text = "已关注"
            isSelected = false
            setTextColor(Attr.getSecondaryTextColor(context))
        }
        val onInActive = {
            text = "关注"
            isSelected = true
            setTextColor(Attr.getPrimaryTextColor(context))
        }
        setupFollowUser(user, null, onActive, onInActive)
    }
}
