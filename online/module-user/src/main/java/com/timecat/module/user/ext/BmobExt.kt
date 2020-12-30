package com.timecat.module.user.ext

import cn.bmob.v3.BmobObject
import com.timecat.component.commonsdk.utils.string.StringUtil
import org.joda.time.format.DateTimeFormat

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/7
 * @description null
 * @usage null
 */
fun BmobObject.friendlyUpdateTimeText(): String = friendlyTimeText(updatedAt)
fun BmobObject.friendlyCreateTimeText(): String = friendlyTimeText(createdAt)
fun friendlyTimeText(time: String): String {
    val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
    return StringUtil.friendlyTime(dateTimeFormat.parseDateTime(time))
}
