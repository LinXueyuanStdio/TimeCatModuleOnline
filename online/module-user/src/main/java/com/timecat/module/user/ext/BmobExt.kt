package com.timecat.module.user.ext

import cn.leancloud.AVObject
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.commonsdk.utils.string.StringUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.net.childrenOf
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.type.BLOCK_DATABASE
import com.timecat.identity.data.service.DataError
import org.joda.time.DateTime
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun AVObject.friendlyUpdateTimeText(): String = friendlyTimeText(updatedAt)
fun AVObject.friendlyCreateTimeText(): String = friendlyTimeText(createdAt)
fun friendlyTimeText(time: Date): String {
    return StringUtil.friendlyTime(DateTime(time))
}

val simpleErrorCallback: (DataError) -> Unit = {
    ToastUtil.e_long("发生错误：$it")
    LogUtil.e("发生错误：$it")
}

fun User.findAllSpace() = childrenOf(this, listOf(BLOCK_DATABASE))
