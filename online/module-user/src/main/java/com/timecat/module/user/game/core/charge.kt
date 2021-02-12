package com.timecat.module.user.game.core

import com.timecat.data.bmob.data.User

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/12
 * @description null
 * @usage null
 */
val User.allCharge: Long
    get() = charge + moneyCharge
