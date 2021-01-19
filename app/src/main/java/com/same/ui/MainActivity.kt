package com.same.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.ext.bmob.requestAction
import com.timecat.data.bmob.ext.net.allAction
import com.timecat.identity.readonly.RouterHub
import com.xiaojinzi.component.impl.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(createButton("登录", RouterHub.LOGIN_LoginActivity))
        linearLayout.addView(createButton("添加插件", RouterHub.USER_AddPluginAppActivity))
        linearLayout.addView(createButton("添加动态", RouterHub.USER_AddMomentActivity))
        linearLayout.addView(createButton("背包", RouterHub.USER_BagActivity))
        val user = _User.getCurrentUser(_User::class.java)
        linearLayout.addView(createButton("用户") {
            Router.with().hostAndPath(RouterHub.USER_UserDetailActivity)
                .putString("userId", user.objectId)
                .forward()
        })
        linearLayout.addView(createButton("用户浏览记录") {
            NAV.raw(this@MainActivity, RouterHub.USER_AllTraceActivity)
                .putSerializable("user", user)
                .forward()
        })
        linearLayout.addView(createButton("用户浏览记录2") {
            requestAction {
                query = user.allAction().apply{
                    order("-createdAt")
                    cachePolicy = BmobQuery.CachePolicy.CACHE_ELSE_NETWORK
                }
                onError = {
                    it.printStackTrace()
                }
                onSuccess = {
                    Log.e("s", it.createdAt.toString())
                }
                onListSuccess = {
                    for (action in it) {
                        Log.e("s", action.createdAt.toString())
                    }
                }
            }
        })
        linearLayout.addView(createButton("test") {
            Log.e("s", "test")
            val qa = BmobQuery<Action>()
            qa.addWhereEqualTo("user", UserDao.getCurrentUser())
            qa.include("block")
            qa.setLimit(10)
            qa.setSkip(100)
            qa.order("-createdAt")
            qa.findObjects(object : FindListener<Action>() {
                override fun done(list: List<Action>, e: BmobException) {
                    for (action in list) {
                        Log.e("s", action.createdAt.toString())
                    }
                    if (e != null) {
                        e.printStackTrace()
                    }
                }
            })
            Log.e("s", "test end")
        })
        setContentView(linearLayout)
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
        val button = Button(this)
        button.text = name
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER
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