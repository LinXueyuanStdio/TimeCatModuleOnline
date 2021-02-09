package com.timecat.module.user.game.task.channal

import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.identity.data.base.IStatus
import com.timecat.identity.data.block.type.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/8
 * @description null
 * @usage null
 */
class ChannelState(var status: Long = 0) : IStatus {
    companion object {
        @JvmStatic
        fun getChannelList(ownActivities: List<OwnActivity>): List<TaskChannel> {
            val state = ChannelState()
            ownActivities.forEach {
                when (it.activity.subtype) {
                    ACTIVITY_Url -> state.addStatus(TaskChannel.Home.id)
                    ACTIVITY_Text_url -> state.addStatus(TaskChannel.Home.id)
                    ACTIVITY_Custom -> state.addStatus(TaskChannel.Custom.id)
                    ACTIVITY_Dream -> state.addStatus(TaskChannel.Dream.id)
                    ACTIVITY_Double -> state.addStatus(TaskChannel.Double.id)
                    ACTIVITY_Card -> state.addStatus(TaskChannel.Card.id)
                    ACTIVITY_Price -> state.addStatus(TaskChannel.Price.id)
                    ACTIVITY_Life -> state.addStatus(TaskChannel.Life.id)
                    ACTIVITY_Achievement -> state.addStatus(TaskChannel.Achievement.id)
                    ACTIVITY_Get_back -> state.addStatus(TaskChannel.Get_back.id)
                    ACTIVITY_Seven_day_sign -> state.addStatus(TaskChannel.Home.id)
                    ACTIVITY_Everyday_main -> state.addStatus(TaskChannel.Home.id)
                    ACTIVITY_One_task -> state.addStatus(TaskChannel.Home.id)
                }
            }
            val ans = mutableListOf<TaskChannel>()
            if (state.isStatusEnabled(TaskChannel.Home.id)) {
                ans.add(TaskChannel.Home)
            }
            if (state.isStatusEnabled(TaskChannel.Custom.id)) {
                ans.add(TaskChannel.Custom)
            }
            if (state.isStatusEnabled(TaskChannel.Dream.id)) {
                ans.add(TaskChannel.Dream)
            }
            if (state.isStatusEnabled(TaskChannel.Double.id)) {
                ans.add(TaskChannel.Double)
            }
            if (state.isStatusEnabled(TaskChannel.Card.id)) {
                ans.add(TaskChannel.Card)
            }
            if (state.isStatusEnabled(TaskChannel.Price.id)) {
                ans.add(TaskChannel.Price)
            }
            if (state.isStatusEnabled(TaskChannel.Life.id)) {
                ans.add(TaskChannel.Life)
            }
            if (state.isStatusEnabled(TaskChannel.Achievement.id)) {
                ans.add(TaskChannel.Achievement)
            }
            if (state.isStatusEnabled(TaskChannel.Get_back.id)) {
                ans.add(TaskChannel.Get_back)
            }
            return ans
        }
    }
    //region IStatus 用 16 进制管理状态
    /**
     * 往状态集中加一个状态
     * @param status status
     */
    override fun addStatus(status: Long) {
        this.status = this.status or status
    }

    /**
     * 往状态集中移除一个状态
     * @param status status
     */
    override fun removeStatus(status: Long) {
        this.status = this.status and status.inv()
    }

    /**
     * 状态集中是否包含某状态
     * @param status status
     */
    override fun isStatusEnabled(status: Long): Boolean {
        return this.status and status != 0L
    }

    override fun statusDescription(): String = ""
    //endregion
}