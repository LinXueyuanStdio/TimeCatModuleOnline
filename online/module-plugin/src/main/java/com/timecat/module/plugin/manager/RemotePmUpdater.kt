package com.timecat.module.plugin.manager

import android.content.Context
import com.tencent.shadow.dynamic.host.PluginManagerUpdater
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 这个Updater没有任何升级能力。直接将指定路径作为其升级结果。
 * @usage 更新 Manager
 */
class RemotePmUpdater(
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
        if (latestInfo.versionCode <= currentInfo.versionCode) {
            callback(this)
        } else {
            forceUpdate(latestInfo, callback)
        }
    }

    fun forceUpdate(latestInfo: PluginInfo, callback: (PluginManagerUpdater) -> Unit) {
        callback(this)
    }

    override fun onUpdate(callback: (PluginManagerUpdater) -> Unit) {
        loadLatestVersion {
            updateBy(it, pluginInfo, callback)
        }
    }

}