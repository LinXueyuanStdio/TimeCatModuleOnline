package com.same.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import cn.leancloud.AVLogger
import cn.leancloud.AVOSCloud
import cn.leancloud.AVQuery
import com.google.android.material.button.MaterialButton
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.requestAction
import com.timecat.data.bmob.ext.net.allAction
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.layout_height
import com.timecat.layout.ui.layout.layout_width
import com.timecat.layout.ui.layout.match_parent
import com.xiaojinzi.component.impl.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG)
        LogUtil.DEBUG = true
        LogUtil.OPEN_LOG = true
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(createButton("登录", RouterHub.LOGIN_LoginActivity))
        linearLayout.addView(createButton("游戏化", RouterHub.USER_GameHomeActivity))
        linearLayout.addView(createButton("添加插件", RouterHub.USER_AddPluginAppActivity))
        linearLayout.addView(createButton("添加动态", RouterHub.USER_AddMomentActivity))
        linearLayout.addView(createButton("背包", RouterHub.USER_BagActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllTaskActivity, RouterHub.USER_AllTaskActivity))
        linearLayout.addView(createButton(RouterHub.USER_ItemEditorActivity, RouterHub.USER_ItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_ThingItemEditorActivity, RouterHub.USER_ThingItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_PackageItemEditorActivity, RouterHub.USER_PackageItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_DataItemEditorActivity, RouterHub.USER_DataItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_EquipItemEditorActivity, RouterHub.USER_EquipItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_BuffItemEditorActivity, RouterHub.USER_BuffItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_CubeItemEditorActivity, RouterHub.USER_CubeItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_CubeItemEditorActivity, RouterHub.USER_CubeItemEditorActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllLeaderBoardActivity, RouterHub.USER_AllLeaderBoardActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllRecommendActivity, RouterHub.USER_AllRecommendActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllAppActivity, RouterHub.USER_AllAppActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllTraceActivity, RouterHub.USER_AllTraceActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllCubeActivity, RouterHub.USER_AllCubeActivity))
        linearLayout.addView(createButton(RouterHub.USER_AllMailActivity, RouterHub.USER_AllMailActivity))
        linearLayout.addView(createButton("game", RouterHub.USER_MailEditorActivity))
        linearLayout.addView(createButton("用户") {
            val user = UserDao.getCurrentUser()
            Router.with().hostAndPath(RouterHub.USER_UserDetailActivity)
                .putString("userId", user?.objectId)
                .forward()
        })
        linearLayout.addView(createButton("用户浏览记录") {
            val user = UserDao.getCurrentUser()
            NAV.raw(this@MainActivity, RouterHub.USER_AllTraceActivity)
                .putParcelable("user", user)
                .forward()
        })
        val sv = ScrollView(this)
        sv.addView(linearLayout)
        setContentView(sv)
    }

    private fun createButton(name: String, path: String): Button {
        val button = createButton(name)
        button.setOnClickListener { go(path) }
        return button
    }

    private fun createButton(name: String, onClickListener: View.OnClickListener): Button {
        val button = createButton(name)
        button.setOnClickListener(onClickListener)
        return button
    }

    private fun createButton(name: String): Button {
        val button = MaterialButton(this)
        button.text = name
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER
        button.isAllCaps = false
        return button
    }

    private fun go(path: String) {
        Router.with().hostAndPath(path)
            .forward(object : Callback {
                override fun onSuccess(result: RouterResult) {}
                override fun onEvent(successResult: RouterResult?, errorResult: RouterErrorResult?) {}
                override fun onCancel(originalRequest: RouterRequest?) {}
                override fun onError(errorResult: RouterErrorResult) {
                    Log.e("ui", errorResult.error.toString())
                }
            })
    }
}