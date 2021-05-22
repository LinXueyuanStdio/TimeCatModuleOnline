package com.timecat.module.user.app

import com.timecat.identity.service.ActivityTaskService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description 游戏任务初始化
 * @usage null
 */
@ServiceAnno(ActivityTaskService::class)
class ActivityTaskServiceImpl : ActivityTaskService {
    override fun init() {
    }
}