package com.timecat.module.plugin.manager

import android.content.Context
import android.os.Environment
import com.tencent.shadow.dynamic.host.DynamicPluginManager
import com.tencent.shadow.dynamic.host.PluginManager
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
object Plugin {
    interface UpdateListener {
        fun onStart()
        fun onLoad()
        fun onPause()
        fun onResume()
        fun onStop()
        fun onComplete()
    }

    val managerPool: MutableMap<String, PluginManager> = mutableMapOf()

    @JvmStatic
    fun manager(
        context: Context,
        pluginInfo: PluginInfo,
        updateListener: UpdateListener,
        onPrepared: (PluginManager) -> Unit,
    ) {
        val uuid = pluginInfo.uuid
        val manager = managerPool[uuid]
        if (manager == null) {
            val updater = RemotePmUpdater(context, pluginInfo, updateListener)
            updater.prepare {
                val newManager = DynamicPluginManager(it)
                managerPool[uuid] = newManager
                onPrepared(newManager)
            }
        } else {
            onPrepared(manager)
        }
    }

    /**
     * 缓存目录
     * @param context context
     * @return 缓存目录
     */
    @JvmStatic
    fun getCacheDir(context: Context): String {
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val cacheDir = context.externalCacheDir
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir.absolutePath
            }
        }
        return context.cacheDir.absolutePath
    }

    const val PLUGIN_DEPLOY_PATH = "plugins"

    @JvmStatic
    fun getPluginDir(context: Context): String? {
        val pluginDir = File(getCacheDir(context), PLUGIN_DEPLOY_PATH)
        return if (pluginDir.exists() || pluginDir.mkdirs()) {
            pluginDir.absolutePath
        } else null
    }

    @JvmStatic
    fun fileInPluginDir(context: Context, filename: String): String {
        return File(getPluginDir(context), filename).absolutePath
    }
}