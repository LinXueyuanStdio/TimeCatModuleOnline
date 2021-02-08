package com.timecat.module.user.game.task.fragment.custom

import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.login.BaseLoginMainFragment
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_ActivityCustomFragment)
class ActivityCustomFragment : BaseLoginMainFragment() {
    override fun layout(): Int {
        return super.layout()
    }
}