package com.timecat.module.user.view.dsl

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.deleteAction
import com.timecat.data.bmob.ext.bmob.requestOneActionOrNull
import com.timecat.data.bmob.ext.bmob.saveAction
import com.timecat.data.bmob.ext.isLiked
import com.timecat.data.bmob.ext.isUnLiked
import com.timecat.data.bmob.ext.like
import com.timecat.data.bmob.ext.net.allLikeBlock
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.layout.drawable_start
import com.timecat.layout.ui.layout.drawable_top
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/19
 * @description null
 * @usage null
 */
fun setupLikeBlockButton(
    context: Context,
    iv: TextView,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    iv.apply {
        isEnabled = false
        val onActive = {
            if (tag == block.objectId) {
                drawable_top = R.drawable.user_ic_favorite_24dp
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    TextViewCompat.setCompoundDrawableTintList(iv, ColorStateList.valueOf(Attr.getAccentColor(context)))
                }
                text = "${block.likes + 1}"
            }
        }
        val onInActive = {
            if (tag == block.objectId) {
                drawable_top = R.drawable.user_ic_love
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    TextViewCompat.setCompoundDrawableTintList(iv, ColorStateList.valueOf(Attr.getIconColor(context)))
                }
                text = "${if (block.likes == 0) "点赞" else block.likes}"
            }
        }
        setupLikeBlock(block, needRefresh, onActive, onInActive)
    }
}

fun setupLikeBlockButton2(
    context: Context,
    iv: TextView,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    iv.apply {
        isEnabled = false
        tag = block.objectId
        val onActive = {
            if (tag == block.objectId) {
                drawable_start = R.drawable.user_ic_favorite_24dp
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    TextViewCompat.setCompoundDrawableTintList(iv, ColorStateList.valueOf(Attr.getAccentColor(context)))
                }
                text = "${if (block.likes == 0) "点赞" else block.likes}"
            }
        }
        val onInActive = {
            if (tag == block.objectId) {
                drawable_start = R.drawable.user_ic_love
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    TextViewCompat.setCompoundDrawableTintList(iv, ColorStateList.valueOf(Attr.getSecondaryTextColor(context)))
                }
                text = "${if (block.likes == 0) "点赞" else block.likes}"
            }
        }
        setupLikeBlock(block, needRefresh, onActive, onInActive)
    }
}

fun View.setupLikeBlock(
    block: Block,
    needRefresh: (() -> Unit)? = null,
    onActive: () -> Unit = {},
    onInActive: () -> Unit = {}
) = apply {
    var relation: Action? = null
    val I = UserDao.getCurrentUser() ?: return@apply
    tag = block.objectId
    requestOneActionOrNull {
        query = I.allLikeBlock(block)
        onError = {
            isEnabled = false
            LogUtil.se("onError$it")
            onInActive()
        }
        onEmpty = {
            isEnabled = true
            LogUtil.se("onEmpty")
            onInActive()
        }
        onSuccess = {
            isEnabled = true
            LogUtil.se("onSuccess")
            relation = it
            onActive()
        }
    }
    setShakelessClickListener {
        if (relation == null) {
            saveAction {
                target = I like block
                onSuccess = {
                    needRefresh?.invoke()
                    relation = it
                    block.isLiked()
                    block.likes += 1
                    onActive()
                    ToastUtil.ok("点赞成功")
                }
                onError = { e ->
                    ToastUtil.e("点赞失败")
                    LogUtil.e(e.toString())
                }
            }
        } else {
            deleteAction {
                target = relation!!
                onSuccess = {
                    needRefresh?.invoke()
                    relation = null
                    block.isUnLiked()
                    block.likes -= 1
                    onInActive()
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

fun setupLikeBlockButton(
    context: Context,
    container: View,
    iv: ImageView,
    tv: TextView,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    iv.apply {
        val onActive = {
            if (container.tag == block.objectId) {
                setImageResource(R.drawable.user_ic_favorite_24dp)
                imageTintList = ColorStateList.valueOf(Attr.getAccentColor(context))
                tv.text = "${if (block.likes == 0) "点赞" else block.likes}"
            }
        }
        val onInActive = {
            if (container.tag == block.objectId) {
                setImageResource(R.drawable.user_ic_love)
                imageTintList = ColorStateList.valueOf(Attr.getIconColor(context))
                tv.text = "${if (block.likes == 0) "点赞" else block.likes}"
            }
        }
        container.isEnabled = false
        container.setupLikeBlock(block, needRefresh, onActive, onInActive)
    }
}
