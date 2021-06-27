package com.timecat.module.plugin.manager

import android.content.Context
import android.os.Environment
import com.tencent.shadow.dynamic.host.PluginManager

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
object Plugin {
    val managerPool: MutableMap<String, PluginManager> = mutableMapOf()

    @JvmStatic
    fun manager(uuid: String) :PluginManager? {
        val m = managerPool[uuid]
        if (m == null) {

        }
        return null
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
}