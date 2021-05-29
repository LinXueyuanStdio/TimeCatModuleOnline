package com.timecat.module.user.game.task.task

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/29
 * @description null
 * @usage null
 */
const val USER_ShowFinishTasksBottomSheet: String = RouterHub.USER + RouterHub.FRAGMENT + "/ShowFinishTasksBottomSheet"

fun showFinishTask(m: FragmentManager, block: Block) {
    val f = NAV.fragment(USER_ShowFinishTasksBottomSheet, "block", block)
    if (f is DialogFragment) {
        f.show(m, block.objectId)
    }
}
