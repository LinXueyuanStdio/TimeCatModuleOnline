package com.timecat.module.user.ext

import cn.leancloud.AVObject
import com.timecat.component.commonsdk.utils.string.StringUtil
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
