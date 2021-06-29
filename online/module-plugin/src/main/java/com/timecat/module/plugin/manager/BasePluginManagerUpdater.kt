package com.timecat.module.plugin.manager

import android.content.Context
import com.tencent.shadow.dynamic.host.PluginManagerUpdater
import java.io.File
import java.util.concurrent.Future

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
abstract class BasePluginManagerUpdater(val context: Context) : PluginManagerUpdater {
    override fun wasUpdating(): Boolean {
        return false
    }

    override fun isAvailable(file: File?): Future<Boolean>? {
        return null
    }
    open fun prepare(callback: (PluginManagerUpdater) -> Unit) {
        if (latest == null || !latest.exists()) {
            onUpdate(callback)
        } else {
            callback(this)
        }
    }
    abstract fun onUpdate(callback: (PluginManagerUpdater) -> Unit)
    override fun update(): Future<File>? {
        onUpdate {}
        return null
    }
}