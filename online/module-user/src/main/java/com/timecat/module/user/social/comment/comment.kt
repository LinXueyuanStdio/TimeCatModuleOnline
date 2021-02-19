package com.timecat.module.user.social.comment

import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/18
 * @description null
 * @usage null
 */
const val USER_CommentDetailBottomSheet: String = RouterHub.USER + RouterHub.FRAGMENT + "/CommentDetailBottomSheet"

fun showSubComments(m: FragmentManager, block: Block) {
    val f = NAV.fragment(USER_CommentDetailBottomSheet, "block", block)
    if (f is DialogFragment) {
        f.show(m, block.objectId)
    }
}
