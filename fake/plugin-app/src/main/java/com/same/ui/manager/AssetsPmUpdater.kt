package com.same.ui.manager

import android.content.Context
import com.tencent.shadow.dynamic.host.PluginManagerUpdater
import com.timecat.component.commonsdk.utils.utils.FileUtil
import com.timecat.fake.plugin.BuildConfig
import com.timecat.module.plugin.manager.BasePluginManagerUpdater
import com.timecat.module.plugin.manager.Plugin
import com.timecat.module.plugin.manager.PluginInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 这个Updater没有任何升级能力。直接将指定路径作为其升级结果。
 * @usage 更新 Manager
 */
val assetPluginInfo = PluginInfo(
    UUID.randomUUID().toString(),
    0,
    "assets",
    0,
    "1.0.0",
    "",
    "pluginmanager.apk",
    "",
    if (BuildConfig.DEBUG) "plugin-debug.zip" else "plugin-release.zip"
)

class AssetsPmUpdater(
    context: Context,
    val pluginInfo: PluginInfo,
    val updateListener: Plugin.UpdateListener,
) : BasePluginManagerUpdater(context) {

    private var apk: File = pluginInfo.getPluginManagerFile(context)

    /**
     * 获取本地最新可用的
     *
     * @return `null`表示本地没有可用的
     */
    override fun getLatest(): File? {
        if (apk.exists()) return apk
        return null
    }

    fun loadLatestVersion(callback: (PluginInfo) -> Unit) {
        callback(pluginInfo)
    }

    fun updateBy(latestInfo: PluginInfo, currentInfo: PluginInfo, callback: (PluginManagerUpdater) -> Unit) {
        forceUpdate(latestInfo, callback)
    }

    fun forceUpdate(latestInfo: PluginInfo, callback: (PluginManagerUpdater) -> Unit) {
        updateListener.onStart()
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val pluginManagerFile = pluginInfo.getPluginManagerFile(context)
                val pluginZipFile = pluginInfo.getPluginZipFile(context)
                val inputStream = context.assets.open(latestInfo.managerFilename)
                val out: OutputStream = FileOutputStream(pluginManagerFile)
                FileUtil.copy(inputStream, out)
                val zip = context.assets.open(latestInfo.pluginZipFilename)
                val outZip: OutputStream = FileOutputStream(pluginZipFile)
                FileUtil.copy(zip, outZip)
                withContext(Dispatchers.Main) {
                    updateListener.onComplete()
                    callback(this@AssetsPmUpdater)
                }
            } catch (e: IOException) {
                throw RuntimeException("从assets中复制apk出错", e)
            }
        }
    }

    override fun onUpdate(callback: (PluginManagerUpdater) -> Unit) {
        loadLatestVersion {
            updateBy(it, pluginInfo, callback)
        }
    }

}