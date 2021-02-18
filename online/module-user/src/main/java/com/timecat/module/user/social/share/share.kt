package com.timecat.module.user.social.share

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
const val USER_ShareBottomSheet: String = RouterHub.USER + RouterHub.FRAGMENT + "/ShareBottomSheet"
const val USER_MoreBottomSheet: String = RouterHub.USER + RouterHub.FRAGMENT + "/MoreBottomSheet"

fun Block.herf(): String {
    return RouterHub.URL_HEADER + RouterHub.EDITOR_BlockDetailFragment
}

fun Block.shareText(): String {
    return "${herf()}\n@${user.nickName}\n${content}"
}


fun showShare(m: FragmentManager, block: Block) {
    val f = NAV.fragment(USER_ShareBottomSheet, "block", block)
    if (f is DialogFragment) {
        f.show(m, block.objectId)
    }
}

fun showMore(m: FragmentManager, block: Block) {
    val f = NAV.fragment(USER_MoreBottomSheet, "block", block)
    if (f is DialogFragment) {
        f.show(m, block.objectId)
    }
}