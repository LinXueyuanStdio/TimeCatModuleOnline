package com.timecat.module.plugin.manager.picturebed

import android.content.Context
import com.timecat.module.plugin.manager.RemotePathPmUpdater

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 这个Updater没有任何升级能力。直接将指定路径作为其升级结果。
 * @usage 更新 Manager
 */
class PictureBedPmUpdater(context: Context) : RemotePathPmUpdater(context) {
    override fun getPluginManagerVersionCode(): Int {
        return PictureBedPluginConstants.getPluginManagerVersionCode(managerVersionCodeStr())
    }

    override fun getPluginManagerAbsPath(context: Context, code: Int): String {
        return PictureBedPluginConstants.getPluginManagerAbsPath(context, name() + code)
    }

    override fun updatingStr(): String = "PictureBedManagerUpdating"

    override fun managerVersionCodeStr(): String = "PictureBedManagerVersionCode"

    override fun name(): String = "PictureBed"

    override fun managerJsonFileName(): String = "manager-picturebed.json"
}