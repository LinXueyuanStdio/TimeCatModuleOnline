package com.timecat.module.user.ext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description null
 * @usage null
 */
operator fun String.div(other: String): String {
    return "$this/$other"
}
