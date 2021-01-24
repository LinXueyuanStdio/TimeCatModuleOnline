package com.timecat.module.user.game.core

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import cn.iwgang.countdownview.DynamicConfig
import cn.leancloud.AVOSCloud
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.business.TimerView
import com.timecat.middle.setting.MaterialForm
import io.reactivex.disposables.Disposable
import kotlin.math.max

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/24
 * @description null
 * @usage null
 */
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

    fun timerView(context: Context, trueWater: Int, needWaitTime: Long): ViewGroup {
        val container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL
        MaterialForm(context, container).apply {
            val textView = Body("体力：${trueWater}").apply {
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                gravity = Gravity.CENTER
                setTextColor(Attr.getPrimaryTextColor(context))
                setTextSize(16f)
            }
            val timer = TimerView(context).apply {
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                val dynamicConfigBuilder = DynamicConfig.Builder()
                dynamicConfigBuilder
                    .setTimeTextColor(Attr.getPrimaryTextColor(context))
                    .setSuffixTextColor(Attr.getSecondaryTextColor(context))
                    .setShowSecond(true)
                    .setShowMinute(true)
                    .setSuffixMinute("分")
                    .setSuffixSecond("秒")
                    .setTimeTextBold(true)
                dynamicShow(dynamicConfigBuilder.build())
                start(needWaitTime)
                var water = trueWater
                setOnCountdownEndListener {
                    water++
                    textView.text = "体力：${water}"
                    it.start(6 * 60 * 1000)
                }
            }
            container.addView(timer)
        }
        return container
    }
}