package com.timecat.module.user.game.task.fragment.card

import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginMainFragment
import com.timecat.module.user.game.task.fragment.BaseActivityFragment
import com.xiaojinzi.component.anno.FragmentAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
@FragmentAnno(RouterHub.USER_ActivityCardFragment)
class ActivityCardFragment : BaseActivityFragment() {
    override fun layout(): Int = R.layout.user_fragment_game_activity_card

}