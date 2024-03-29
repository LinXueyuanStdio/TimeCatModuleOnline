package com.timecat.module.user.game.core

import cn.leancloud.AVOSCloud
import com.timecat.data.bmob.data.User
import io.reactivex.disposables.Disposable
import kotlin.math.max

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/24
 * @description null
 * @usage null
 */
class WaterModified
object Water {

    // 6 分钟恢复 1 体力
    val recoverTime: Long = 6 * 60 * 1000
//    val recoverTime: Long = 6 * 1000

    fun save(nowWater: Int, user: User): Disposable {
        return AVOSCloud.getServerDateInBackground().subscribe {
            user.water = nowWater
            user.lastSettleTime = it.date
            user.saveEventually()
        }
    }

    fun compute(user: User, currentTime: Long, onNext: (trueWater: Int,
                                                        waterLimit: Int,
                                                        needWaitTime: Long) -> Unit) {
        val lastWater = user.water
        val lastTime = user.lastSettleTime.time
        val currentLevel = user.level
        compute(currentLevel, lastWater, lastTime, currentTime, onNext)
    }

    fun compute(currentLevel: Int,
                lastWater: Int,
                lastTime: Long,
                currentTime: Long,
                onNext: (trueWater: Int,
                         waterLimit: Int,
                         needWaitTime: Long) -> Unit) {
        val duration = max(currentTime - lastTime, 1)
        val recoverTime = Water.recoverTime
        val gainWater = (duration / recoverTime).toInt()
        val needWaitTime = duration % recoverTime

        val currentWater = lastWater + gainWater
        val waterLimit = Level.waterLimit(currentLevel)
        val trueWater = currentWater.coerceIn(0, waterLimit)
        onNext(trueWater, waterLimit, needWaitTime)
    }
}