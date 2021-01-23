package com.timecat.module.user.view.dsl

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.User2User
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.follow
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.data.bmob.ext.net.allFollowBlock
import com.timecat.data.bmob.ext.net.allLikeBlock
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.layout.drawable_top
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description null
 * @usage null
 */
fun setupFollowUserButton(
    context: Context,
    button: Button,
    user: _User
) {
    button.apply {
        text = "关注"
        isEnabled = false
        var relation: User2User? = null
        val I = UserDao.getCurrentUser() ?: return@apply
        requestOneUserRelation {
            query = I.allFollow(user)
            onError = {
                isEnabled = true
                text = "关注"
                tag = "关注"
                setTextColor(Attr.getPrimaryColor(context))
            }
            onSuccess = {
                if (it == null) {
                    isEnabled = true
                    text = "关注"
                    tag = "关注"
                    setTextColor(Attr.getPrimaryColor(context))
                } else {
                    isEnabled = true
                    relation = it
                    text = "已关注"
                    tag = "已关注"
                    setTextColor(Attr.getBackgroundDarkestColor(context))
                }
            }
        }
        setShakelessClickListener {
            if (relation == null) {
                saveUserRelation {
                    target = I follow user
                    onSuccess = {
                        setBackgroundResource(R.drawable.shape_4)
                        text = "已关注"
                        setTextColor(Attr.getPrimaryTextColor(context))
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
                        setBackgroundResource(R.drawable.shape_3)
                        text = "关注"
                        setTextColor(Attr.getPrimaryTextColorReverse(context))
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
}

fun setupFollowBlockButton(
    context: Context,
    button: Button,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    button.apply {
        isEnabled = false
        text = "关注"
        var relation: Action? = null
        val I = UserDao.getCurrentUser() ?: return@apply
        requestOneAction {
            query = I.allFollowBlock(block)
            onError = {
                isEnabled = true
                text = "关注"
                tag = "关注"
                setTextColor(Attr.getPrimaryColor(context))
            }
            onSuccess = {
                if (it == null) {
                    isEnabled = true
                    text = "关注"
                    tag = "关注"
                    setTextColor(Attr.getPrimaryColor(context))
                } else {
                    isEnabled = true
                    relation = it
                    text = "已关注"
                    tag = "已关注"
                    setTextColor(Attr.getBackgroundDarkestColor(context))
                }
            }
        }
        setShakelessClickListener {
            if (relation == null) {
                saveAction {
                    target = I follow block
                    onSuccess = {
                        needRefresh?.invoke()
                        setBackgroundResource(R.drawable.shape_4)
                        text = "已关注"
                        setTextColor(Attr.getPrimaryTextColor(context))
                        ToastUtil.ok("关注成功")
                    }
                    onError = { e ->
                        ToastUtil.ok("关注失败")
                        LogUtil.e(e.toString())
                    }
                }
            } else {
                deleteAction {
                    target = relation!!
                    onSuccess = {
                        needRefresh?.invoke()
                        setBackgroundResource(R.drawable.shape_3)
                        text = "关注"
                        setTextColor(Attr.getPrimaryTextColorReverse(context))
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
}

fun setupLikeBlockButton(
    context: Context,
    iv: TextView,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    iv.apply {
        isEnabled = false
        var relation: Action? = null
        val I = UserDao.getCurrentUser() ?: return@apply
        requestOneAction {
            query = I.allLikeBlock(block)
            onError = {
                isEnabled = true
                drawable_top = R.drawable.user_ic_love
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    compoundDrawableTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                }
                text = "点赞"
                tag = "点赞"
            }
            onSuccess = {
                if (it == null) {
                    isEnabled = true
                    drawable_top = R.drawable.user_ic_love
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        compoundDrawableTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                    }
                    text = "点赞"
                    tag = "点赞"
                } else {
                    isEnabled = true
                    relation = it
                    drawable_top = R.drawable.user_ic_favorite_24dp
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        compoundDrawableTintList = ColorStateList.valueOf(Attr.getAccentColor(context))
                    }
                    text = "已点赞"
                    tag = "已点赞"
                }
            }

        }
        setShakelessClickListener {
            if (relation == null) {
                saveAction {
                    target = I follow block
                    onSuccess = {
                        needRefresh?.invoke()
                        drawable_top = R.drawable.user_ic_favorite_24dp
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            compoundDrawableTintList = ColorStateList.valueOf(Attr.getAccentColor(context))
                        }
                        tag = "已点赞"
                        ToastUtil.ok("点赞成功")
                    }
                    onError = { e ->
                        ToastUtil.ok("点赞失败")
                        LogUtil.e(e.toString())
                    }
                }
            } else {
                deleteAction {
                    target = relation!!
                    onSuccess = {
                        needRefresh?.invoke()
                        drawable_top = R.drawable.user_ic_love
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            compoundDrawableTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                        }
                        tag = "点赞"
                        ToastUtil.ok("解除点赞成功")
                    }
                    onError = { e ->
                        ToastUtil.e("解除点赞失败")
                        LogUtil.e(e.toString())
                    }
                }
            }
        }
    }
}

fun setupLikeBlockButton(
    context: Context,
    iv: ImageView,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    iv.apply {
        isEnabled = false
        var relation: Action? = null
        val I = UserDao.getCurrentUser() ?: return@apply
        requestOneAction {
            query = I.allLikeBlock(block)
            onError = {
                isEnabled = true
                setImageResource(R.drawable.user_ic_love)
                imageTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                tag = "点赞"
                tag = "点赞"
            }
            onSuccess = {
                if (it == null) {
                    isEnabled = true
                    setImageResource(R.drawable.user_ic_love)
                    imageTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                    tag = "点赞"
                    tag = "点赞"
                } else {
                    isEnabled = true
                    relation = it
                    setImageResource(R.drawable.user_ic_favorite_24dp)
                    imageTintList = ColorStateList.valueOf(Attr.getAccentColor(context))
                    tag = "已点赞"
                    tag = "已点赞"
                }
            }
        }
        setShakelessClickListener {
            if (relation == null) {
                saveAction {
                    target = I follow block
                    onSuccess = {
                        needRefresh?.invoke()
                        setImageResource(R.drawable.user_ic_favorite_24dp)
                        imageTintList = ColorStateList.valueOf(Attr.getAccentColor(context))
                        tag = "已点赞"
                        ToastUtil.ok("点赞成功")
                    }
                    onError = { e ->
                        ToastUtil.ok("点赞失败")
                        LogUtil.e(e.toString())
                    }
                }
            } else {
                deleteAction {
                    target = relation!!
                    onSuccess = {
                        needRefresh?.invoke()
                        setImageResource(R.drawable.user_ic_love)
                        imageTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                        tag = "点赞"
                        ToastUtil.ok("解除点赞成功")
                    }
                    onError = { e ->
                        ToastUtil.e("解除点赞失败")
                        LogUtil.e(e.toString())
                    }
                }
            }
        }
    }
}