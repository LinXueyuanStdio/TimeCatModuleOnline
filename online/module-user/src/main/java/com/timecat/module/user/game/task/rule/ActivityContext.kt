package com.timecat.module.user.game.task.rule

import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnActivity

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
    val activity: MutableList<Block> = mutableListOf()

}