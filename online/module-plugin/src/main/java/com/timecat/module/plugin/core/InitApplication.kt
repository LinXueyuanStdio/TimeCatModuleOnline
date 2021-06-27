package com.timecat.module.plugin.core

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.tencent.shadow.core.common.LoggerFactory
import com.tencent.shadow.dynamic.host.DynamicRuntime
import com.tencent.shadow.dynamic.host.PluginManager
import com.timecat.component.commonsdk.utils.override.LogUtil

object InitApplication {
    /**
     * 这个PluginManager对象在Manager升级前后是不变的。它内部持有具体实现，升级时更换具体实现。
     */
    @JvmStatic
    var pictureBedPluginManager: PluginManager? = null
        private set

    @JvmStatic
    fun onApplicationCreate(application: Application) {
        LogUtil.se("")
        if (isProcess(application, ":plugin")) {
            //在全动态架构中，Activity组件没有打包在宿主而是位于被动态加载的runtime，
            //为了防止插件crash后，系统自动恢复crash前的Activity组件，此时由于没有加载runtime而发生classNotFound异常，导致二次crash
            //因此这里恢复加载上一次的runtime
            DynamicRuntime.recoveryRuntime(application)
        }

        LogUtil.se("")
        //Log接口Manager也需要使用，所以主进程也初始化。
        LoggerFactory.setILoggerFactory(AndroidLoggerFactory())
        LogUtil.se("")
    }

    private fun isProcess(context: Context, processName: String): Boolean {
        var currentProcName: String? = null
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                currentProcName = processInfo.processName
                break
            }
        }
        return processName == currentProcName
    }
}