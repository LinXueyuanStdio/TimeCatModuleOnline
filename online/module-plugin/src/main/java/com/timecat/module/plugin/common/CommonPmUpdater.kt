package com.timecat.module.plugin.common

import android.content.Context
import com.timecat.module.plugin.core.RemotePathPmUpdater

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 这个Updater没有任何升级能力。直接将指定路径作为其升级结果。
 * @usage 更新 Manager
 */
class CommonPmUpdater(context: Context) : RemotePathPmUpdater(context) {
    override fun updatingStr(): String = "ManagerUpdating"

    override fun managerVersionCodeStr(): String = "ManagerVersionCode"

}