package com.timecat.module.user.game.task.rule

import cn.leancloud.AVQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.data.bmob.data.game.OwnTask
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.requestOwnActivity
import com.timecat.data.bmob.ext.bmob.requestOwnTask
import com.timecat.data.bmob.ext.net.allOwnActivity
import com.timecat.data.bmob.ext.net.allOwnTask
import com.timecat.data.bmob.ext.net.allTask
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.*
import com.timecat.module.user.game.task.channal.ChannelState
import com.timecat.module.user.game.task.channal.TaskChannel
import com.timecat.module.user.permission.UserContext
import com.timecat.module.user.social.cloud.channel.TabChannel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/8
 * @description 活动上下文
 * 分享文章*1     ---|   |---条件1 ---> 任务1--->活动1
 *                   检查|---条件2 -/-->任务2--->活动2
 * 拥有3个60级方块 ---|   |---条件3 /---/
 * @usage null
 */
object ActivityContext {
    @JvmStatic
    var I: User? = UserDao.getCurrentUser()

    @JvmStatic
    val ownActivity: MutableList<OwnActivity> = mutableListOf()

    @JvmStatic
    val main: MutableList<OwnActivity> = mutableListOf()

    @JvmStatic
    val card: MutableList<OwnActivity> = mutableListOf()

    @JvmStatic
    val tasks: MutableList<Block> = mutableListOf()

    @JvmStatic
    val taskProgress: MutableList<OwnTask> = mutableListOf()

    @JvmStatic
    val taskRewardProgress: MutableMap<String, Boolean> = mutableMapOf()

    @JvmStatic
    val rules: MutableList<TaskRule> = mutableListOf()

    @JvmStatic
    val channels: MutableList<TabChannel> = mutableListOf()

    @JvmStatic
    fun loadByUser(user: User?) {
        UserContext.I = user ?: return
        loadOwnActivity(user)
    }

    @JvmStatic
    private fun loadOwnActivity(user: User) {
        LogUtil.sd(user)
        requestOwnActivity {
            query = user.allOwnActivity().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                ownActivity.clear()
            }
            onSuccess = {
                LogUtil.d(it)
                ownActivity.clear()
                ownActivity.addAll(it)
                progressOwnActivities(ownActivity)
            }
        }
    }

    private fun progressOwnActivities(owns: List<OwnActivity>) {
        val taskIds = mutableListOf<String>()
        val state = ChannelState()
        for (own in owns) {
            val activity = own.activity
            when (activity.subtype) {
                ACTIVITY_Url -> {
                    state.addStatus(TaskChannel.Home.id)
                }
                ACTIVITY_Text_url -> {
                    state.addStatus(TaskChannel.Home.id)
                }
                ACTIVITY_Custom -> {
                    //TODO
                    state.addStatus(TaskChannel.Custom.id)
                }
                ACTIVITY_Dream -> {
                    state.addStatus(TaskChannel.Dream.id)
                }
                ACTIVITY_Double -> {
                    state.addStatus(TaskChannel.Double.id)
                }
                ACTIVITY_Card -> {
                    state.addStatus(TaskChannel.Card.id)
                }
                ACTIVITY_Price -> {
                    state.addStatus(TaskChannel.Price.id)
                }
                ACTIVITY_Life -> {
                    state.addStatus(TaskChannel.Life.id)
                }
                ACTIVITY_Achievement -> {
                    state.addStatus(TaskChannel.Achievement.id)
                }
                ACTIVITY_Get_back -> {
                    state.addStatus(TaskChannel.Get_back.id)
                }
                ACTIVITY_Seven_day_sign -> {
                    state.addStatus(TaskChannel.Home.id)
                }
                ACTIVITY_Everyday_main -> {
                    state.addStatus(TaskChannel.Home.id)
                }
                ACTIVITY_One_task -> {
                    state.addStatus(TaskChannel.Home.id)
                    val head = ActivityBlock.fromJson(activity.structure)
                    val head2 = ActivityOneTaskBlock.fromJson(head.structure)
                    val taskId = head2.taskId
                    taskIds.add(taskId)
                }
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
        channels.clear()
        channels.addAll(ans.map { it.asTabChannel() })
        loadTasks(taskIds)
    }

    private fun loadTasks(ids: List<String>) {
        requestBlock {
            query = allTask().apply {
                whereContainedIn("objectId", ids)
            }
            onEmpty = {
                tasks.clear()
            }
            onError = {
                LogUtil.e(it)
            }
            onSuccess = {
                tasks.clear()
                tasks.addAll(it)
                progressTasks(it)
            }
        }
        requestOwnTask {
            query = I!!.allOwnTask().apply {
                whereContainedIn("task.objectId", ids)
            }
            onEmpty = {
                taskProgress.clear()
            }
            onError = {
                LogUtil.e(it)
            }
            onSuccess = {
                taskProgress.clear()
                taskProgress.addAll(it)
                progressOwnTasks(it)
            }
        }
    }

    private fun progressOwnTasks(owns: List<OwnTask>) {
        for (own in owns) {
            taskRewardProgress[own.task.objectId] = own.receive
        }
    }

    /**
     * 加载所有的task，获取所有的触发条件，设置好触发条件
     */
    private fun progressTasks(tasks: List<Block>) {
        val taskRules: MutableList<TaskRule> = mutableListOf()
        for (task in tasks) {
            when (task.subtype) {
                TASK_Data -> {
                    val head = TaskBlock.fromJson(task.structure)
                    val head2 = TaskDataBlock.fromJson(head.structure)
                    taskRules.addAll(head2.rules)
                }
            }
        }
        rules.clear()
        rules.addAll(taskRules)
    }
}