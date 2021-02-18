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
        BLOCK_RECORD -> {
            "R.drawable.ic_comment"
        }
        BLOCK_DATABASE -> {
            "R.drawable.ic_comment"
        }
        BLOCK_NOVEL -> {
            "R.drawable.ic_comment"
        }
        BLOCK_ACCOUNT -> {
            "R.drawable.ic_comment"
        }
        BLOCK_MESSAGE -> {
            "R.drawable.ic_comment"
        }
        BLOCK_ABOUT -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_TAG -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_TOPIC -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_MEDIA -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_LEADER_BOARD -> {
            "R.drawable.ic_comment"
        }
        BLOCK_APP -> {
            val appBlock = AppBlock.fromJson(structure)
            appBlock.header.avatar
        }
        BLOCK_COMMENT -> {
            "R.drawable.ic_comment"
        }
        BLOCK_RECOMMEND -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_CONVERSATION -> {
            "R.drawable.ic_comment"
        }
        BLOCK_CONTAINER -> {
            "R.drawable.ic_folder"
        }
        BLOCK_ACTIVITY -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_FOCUS -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_PATH -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_TASK -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_MOMENT -> {
            "R.drawable.ic_comment"
        }
        BLOCK_DIALOG -> {
            "R.drawable.ic_comment"
        }
        BLOCK_PLUGIN -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_LINK -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_BUTTON -> {
            "R.drawable.ic_comment"//TODO
        }
        BLOCK_FORUM -> {
            "R.drawable.ic_comment"
        }
        BLOCK_POST -> {
            "R.drawable.ic_comment"
        }
        BLOCK_PERMISSION -> {
            "R.drawable.ic_person"
        }
        BLOCK_IDENTITY -> {
            "R.drawable.ic_person"
        }
        BLOCK_ROLE -> {
            "R.drawable.ic_person"
        }
        BLOCK_ITEM -> {
            "R.drawable.ic_comment"
        }
        BLOCK_MAIL -> {
            "R.drawable.ic_feed_list_mail_special_24dp"
        }
        else -> "R.drawable.ic_launcher"
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

