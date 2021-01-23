package com.timecat.module.user.ext

import com.timecat.data.bmob.data.common.Block
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.type.*
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
infix fun String.replaceWith(count: Int): String = if (count <= 0) this else "$count"
fun Block.commentText(commentText: String = "评论"): String = commentText replaceWith comments
fun Block.likeText(likeText: String = "点赞"): String = likeText replaceWith likes
fun Block.shareText(shareText: String = "分享"): String = shareText replaceWith relays
fun Block.starText(starText: String = "收藏"): String = starText replaceWith stars

fun Block.simpleAvatar(): String {
    return when (type) {
        BLOCK_COMMENT -> {
            val appBlock = AppBlock.fromJson(structure)
            appBlock.header?.avatar ?: "R.drawable.ic_comment"
        }
        BLOCK_MOMENT -> "R.drawable.ic_cloud_white_24dp"
        BLOCK_TOPIC -> "R.drawable.ic_cloud_white_24dp"
        BLOCK_TAG -> "R.drawable.ic_cloud_white_24dp"
        else -> "R.drawable.ic_block_type_accent_24dp"
    }
}

fun Block.showDetail() {
    when (type) {
        BLOCK_COMMENT -> GO.commentDetail(objectId)
        BLOCK_MOMENT -> GO.momentDetail(objectId)
        BLOCK_FORUM -> GO.forumDetail(objectId)
        else -> ToastUtil.e_long("不支持的格式，请升级")
    }
}

