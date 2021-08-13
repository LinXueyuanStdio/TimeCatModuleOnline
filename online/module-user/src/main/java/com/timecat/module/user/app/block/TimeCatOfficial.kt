package com.timecat.module.user.app.block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 * 时光猫官方 重定向到在线块
 * world://official.timecat.online?redirect={url = world://xxx}
 */
object TimeCatOfficial {
    const val SCHEMA = "world"
    const val Host = "official.timecat.online"
    const val url = "$SCHEMA://${Host}"
    const val redirect = "redirect"
}