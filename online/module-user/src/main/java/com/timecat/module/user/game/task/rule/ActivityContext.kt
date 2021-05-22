package com.timecat.module.user.game.task.rule

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import cn.leancloud.AVQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.data.bmob.data.game.OwnTask
import com.timecat.data.bmob.ext.bmob.requestOwnActivity
import com.timecat.data.bmob.ext.net.allOwnActivity
import com.timecat.data.bmob.ext.net.allOwnTask
import com.timecat.data.bmob.ext.net.allTask
import com.timecat.data.bmob.ext.toDataError
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.*
import com.timecat.module.user.game.task.channal.ChannelState
import com.timecat.module.user.game.task.channal.TaskChannel
import com.timecat.module.user.social.cloud.channel.TabChannel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.ref.WeakReference

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
class ActivityContext(
    private val owner: LifecycleOwner,
    val user: User,
    private val onLoading: () -> Unit,
    private val onLoaded: (ActivityContext) -> Unit
) : LoadableContext() {
    val ownActivity: MutableList<OwnActivity> = mutableListOf()
    val tasks: MutableList<Block> = mutableListOf()
    val taskProgress: MutableList<OwnTask> = mutableListOf()
    val taskRewardProgress: MutableMap<String, Boolean> = mutableMapOf()
    val rules: MutableList<TaskRule> = mutableListOf()
    fun load() {
        onLoading()
        owner.bindLoadableContext(this)
        loadOwnActivity(user)
    }

    private fun loadOwnActivity(user: User) {
        LogUtil.sd(user)
        this attach requestOwnActivity {
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
        LogUtil.sd("progressOwnActivities")
        val taskIds = mutableListOf<String>()
        for (own in owns) {
            val activity = own.activity
            when (activity.subtype) {
                ACTIVITY_Url -> {
                }
                ACTIVITY_Text_url -> {
                }
                ACTIVITY_Custom -> {
                }
                ACTIVITY_Dream -> {
                }
                ACTIVITY_Double -> {
                }
                ACTIVITY_Card -> {
                }
                ACTIVITY_Price -> {
                }
                ACTIVITY_Life -> {
                }
                ACTIVITY_Achievement -> {
                }
                ACTIVITY_Get_back -> {
                }
                ACTIVITY_Seven_day_sign -> {
                }
                ACTIVITY_Everyday_main -> {
                }
                ACTIVITY_One_task -> {
                    val head = ActivityBlock.fromJson(activity.structure)
                    val head2 = ActivityOneTaskBlock.fromJson(head.structure)
                    val taskId = head2.taskId
                    taskIds.add(taskId)
                }
            }
        }
        LogUtil.sd(taskIds)
        if (taskIds.isEmpty()) {
            onLoaded(this)
            return
        }
        loadTasks(taskIds)
    }

    data class TasksAndOwnTasks(
        val tasks: List<Block>? = null,
        val ownTasks: List<OwnTask>? = null,
    )

    private fun loadTasks(ids: List<String>) {
        val getAllTasks = allTask().apply {
            whereContainedIn("objectId", ids)
        }.findInBackground()
        val getAllOwnTasks = user.allOwnTask().apply {
            whereContainedIn("task.objectId", ids)
        }.findInBackground()
        this attach Observable.zip(getAllTasks, getAllOwnTasks) { tasks, ownTasks ->
            TasksAndOwnTasks(tasks, ownTasks)
        }.map {
            if (it.tasks.isNullOrEmpty()) {
                tasks.clear()
            } else {
                tasks.clear()
                tasks.addAll(it.tasks)
                progressTasks(it.tasks)
            }
            if (it.ownTasks.isNullOrEmpty()) {
                taskProgress.clear()
            } else {
                taskProgress.clear()
                taskProgress.addAll(it.ownTasks)
                progressOwnTasks(it.ownTasks)
            }
            true
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onLoaded(this) }, { LogUtil.e(it.toDataError()) })
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

interface GameService {
    fun init(user: User?)

    fun activityContext(
        owner: LifecycleOwner,
        user: User,
        onLoading: () -> Unit,
        onLoaded: (ActivityContext) -> Unit
    )
}

abstract class LoadableContext {
    val disposables = CompositeDisposable()
    var attachLifecycle: Disposable? = null
        set(value) {
            value?.let { disposables.add(it) }
            field = value
        }

    infix fun attach(disposable: Disposable?) {
        attachLifecycle = disposable
    }

    fun stopAllTask() {
        disposables.dispose()
    }

    fun onDestroy() {
        disposables.clear()
    }
}

fun LifecycleOwner.bindLoadableContext(context: LoadableContext) {
    lifecycle.addObserver(ContextHolder(WeakReference(context)))
}

private class ContextHolder(
    private val weakReference: WeakReference<LoadableContext>
) : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_DESTROY) {
            weakReference.get()?.onDestroy()
        }
    }
}
