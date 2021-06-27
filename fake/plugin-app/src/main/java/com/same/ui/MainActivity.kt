package com.same.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.same.ui.manager.AssetsPmUpdater
import com.same.ui.manager.assetPluginInfo
import com.tencent.shadow.dynamic.host.DynamicPluginManager
import com.tencent.shadow.dynamic.host.EnterCallback
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.PluginHub
import com.timecat.layout.ui.layout.dp
import com.timecat.module.plugin.PluginContainerView
import com.timecat.module.plugin.manager.Plugin
import com.timecat.plugin.shared.HostUiLayerProvider
import com.xiaojinzi.component.impl.*

class MainActivity : AppCompatActivity() {
    lateinit var containerView: PluginContainerView
    val updater: AssetsPmUpdater by lazy { AssetsPmUpdater(this, assetPluginInfo, updateListener) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LogUtil.DEBUG = true
        LogUtil.OPEN_LOG = true
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL

        linearLayout.addView(createText("插件"))
        linearLayout.addView(createButton("初始化插件系统") {
            HostUiLayerProvider.init(this)
            ToastUtil.ok("初始化成功！")
        })
        linearLayout.addView(createButton("测试插件") {
            val partKey = ""
            updater.prepare {
                val newManager = DynamicPluginManager(it)
                val bundle = Bundle()
                bundle.putString(PluginHub.KEY_PLUGIN_ZIP_PATH, assetPluginInfo.getPluginZipFile(this).absolutePath)
                bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, partKey)
                bundle.putString(PluginHub.KEY_ACTIVITY_CLASSNAME, "com.tencent.shadow.sample.plugin.app.lib.gallery.splash.SplashActivity")

                newManager.enter(this, PluginHub.FROM_ID_START_ACTIVITY, bundle, object : EnterCallback by containerView {

                })
            }
        })
        linearLayout.addView(createButton("测试插件-跨进程") {
            val partKey = ""
            updater.prepare {
                val newManager = DynamicPluginManager(it)
                val bundle = Bundle()
                bundle.putString(PluginHub.KEY_PLUGIN_ZIP_PATH, assetPluginInfo.getPluginZipFile(this).absolutePath)
                bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, partKey)
                bundle.putString(PluginHub.KEY_ACTIVITY_CLASSNAME, "com.tencent.shadow.sample.plugin.app.lib.gallery.splash.SplashActivity")

                newManager.enter(this, PluginHub.FROM_ID_START_ACTIVITY, bundle, object : EnterCallback by containerView {
                })
            }
        })
        containerView = PluginContainerView(this).apply {

        }
        linearLayout.addView(containerView)

        val sv = ScrollView(this)
        sv.addView(linearLayout)
        setContentView(sv)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            }
        }
    }

    val updateListener: Plugin.UpdateListener
        get() {
            return containerView
        }

    private fun createButton(name: String, path: String): Button {
        val button = createButton(name)
        button.setOnClickListener { go(path) }
        return button
    }

    private fun createPathButton(path: String): Button {
        val button = createButton(path)
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
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            marginStart = 10.dp
            marginEnd = 10.dp
        }
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER_VERTICAL
        button.isAllCaps = false
        return button
    }

    private fun createText(name: String): TextView {
        val button = TextView(this)
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1) {
            for (i in permissions.indices) {
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        throw RuntimeException("必须赋予权限.")
                    }
                }
            }
        }
    }
}