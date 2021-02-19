package com.timecat.module.user.view.dsl

import android.content.Context
import android.view.View
import android.widget.TextView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.deleteAction
import com.timecat.data.bmob.ext.bmob.requestOneActionOrNull
import com.timecat.data.bmob.ext.bmob.saveAction
import com.timecat.data.bmob.ext.follow
import com.timecat.data.bmob.ext.net.allFollowBlock
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/19
 * @description null
 * @usage null
 */
fun View.setupFollowBlock(
    block: Block,
    needRefresh: (() -> Unit)? = null,
    onActive: () -> Unit = {},
    onInActive: () -> Unit = {}
) = apply {
    var relation: Action? = null
    val I = UserDao.getCurrentUser() ?: return@apply
    tag = block.objectId
    requestOneActionOrNull {
        query = I.allFollowBlock(block)
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
            saveAction {
                target = I follow block
                onSuccess = {
                    needRefresh?.invoke()
                    onActive()
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
                    onInActive()
                    relation = null
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

fun setupFollowBlockButton(
    context: Context,
    button: TextView,
    block: Block,
    needRefresh: (() -> Unit)? = null
) {
    button.apply {
        text = "关注"
        isEnabled = false
        val onActive = {
            text = "已关注"
            setBackgroundResource(R.drawable.shape_4)
            setTextColor(Attr.getBackgroundDarkColor(context))
        }
        val onInActive = {
            text = "关注"
            setBackgroundResource(R.drawable.shape_3)
            setTextColor(Attr.getPrimaryTextColor(context))
        }
        setupFollowBlock(block, needRefresh, onActive, onInActive)
    }
}
