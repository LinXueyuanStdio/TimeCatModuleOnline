package com.timecat.module.user.game.task.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.data.bmob.data.game.OwnTask
import com.timecat.identity.data.base.IStatus
import com.timecat.identity.data.block.TaskRule
import com.timecat.identity.data.block.type.*
import com.timecat.module.user.ext.RxViewModel
import com.timecat.module.user.game.task.channal.TaskChannel
import com.timecat.module.user.social.cloud.channel.TabChannel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class TaskViewModel : RxViewModel() {
    /**
     * 所有活动
     */
    val activities: MutableLiveData<List<OwnActivity>> = MutableLiveData()

    class ChannelState(var status: Long = 0) : IStatus {
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

    val channelState: LiveData<ChannelState> = Transformations.map(activities) {
        val state = ChannelState()
        it.forEach {
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
        state
    }
    val taskChannels: LiveData<List<TaskChannel>> = Transformations.map(channelState) {
        val ans = mutableListOf<TaskChannel>()
        if (it.isStatusEnabled(TaskChannel.Home.id)) {
            ans.add(TaskChannel.Home)
        }
        if (it.isStatusEnabled(TaskChannel.Custom.id)) {
            ans.add(TaskChannel.Custom)
        }
        if (it.isStatusEnabled(TaskChannel.Dream.id)) {
            ans.add(TaskChannel.Dream)
        }
        if (it.isStatusEnabled(TaskChannel.Double.id)) {
            ans.add(TaskChannel.Double)
        }
        if (it.isStatusEnabled(TaskChannel.Card.id)) {
            ans.add(TaskChannel.Card)
        }
        if (it.isStatusEnabled(TaskChannel.Price.id)) {
            ans.add(TaskChannel.Price)
        }
        if (it.isStatusEnabled(TaskChannel.Life.id)) {
            ans.add(TaskChannel.Life)
        }
        if (it.isStatusEnabled(TaskChannel.Achievement.id)) {
            ans.add(TaskChannel.Achievement)
        }
        if (it.isStatusEnabled(TaskChannel.Get_back.id)) {
            ans.add(TaskChannel.Get_back)
        }
        ans
    }
    val channels: LiveData<List<TabChannel>> = Transformations.map(taskChannels) {
        it.map { it.asTabChannel() }
    }

    private fun isMain(subtype: Int): Boolean =
        subtype == ACTIVITY_One_task ||
            subtype == ACTIVITY_Text_url ||
            subtype == ACTIVITY_Url ||
            subtype == ACTIVITY_Seven_day_sign ||
            subtype == ACTIVITY_Everyday_main

    /**
     * 主要活动
     */
    val mainActivities = Transformations.map(activities) {
        it.filter { isMain(it.activity.subtype) }
    }

    /**
     * 抽卡
     */
    val cardActivities = Transformations.map(activities) {
        it.filter { it.activity.subtype == ACTIVITY_Card }
    }

    /**
     * 用户自定义
     */
    val customActivities = Transformations.map(activities) {
        it.filter { it.activity.subtype == ACTIVITY_Custom }
    }

    /**
     * 双线
     */
    val doubleActivities = Transformations.map(activities) {
        it.filter { it.activity.subtype == ACTIVITY_Double }
    }

    /**
     * 悬赏
     */
    val priceActivities = Transformations.map(activities) {
        it.filter { it.activity.subtype == ACTIVITY_Price }
    }

    val selectOwnActivityIndex: MutableLiveData<Int> = MutableLiveData(0)

    /**
     * 当前选中的活动
     */
    val ownActivity = Transformations.map(selectOwnActivityIndex) {
        activities.value?.get(it)
    }

    val block: MutableLiveData<Block?> = MutableLiveData()

    val tasks: MutableLiveData<MutableList<Block>> = MutableLiveData(mutableListOf())
    val taskProgress: MutableLiveData<MutableList<OwnTask>> = MutableLiveData(mutableListOf())
    val taskRewardProgress: MutableLiveData<MutableMap<String, Boolean>> = MutableLiveData(mutableMapOf())
    val rules: MutableLiveData<MutableList<TaskRule>> = MutableLiveData(mutableListOf())
}