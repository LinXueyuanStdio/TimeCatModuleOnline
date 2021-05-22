package com.timecat.module.user.app

import androidx.lifecycle.LifecycleOwner
import com.timecat.data.bmob.data.User
import com.timecat.module.user.game.task.rule.ActivityContext
import com.timecat.module.user.game.task.rule.GameService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/22
 * @description null
 * @usage null
 */
@ServiceAnno(GameService::class)
class GameServiceImpl : GameService {
    var activityContextPool:MutableMap<String, ActivityContext> = mutableMapOf()
    override fun init(user: User?) {

    }

    override fun activityContext(owner: LifecycleOwner, user: User, onLoading: () -> Unit, onLoaded: (ActivityContext) -> Unit) {
        var activityContext = activityContextPool[user.objectId]
        if (activityContext != null) {
            onLoaded(activityContext)
            return
        }
        activityContext = ActivityContext(owner, user, onLoading, onLoaded)
        activityContext.load()
    }
}