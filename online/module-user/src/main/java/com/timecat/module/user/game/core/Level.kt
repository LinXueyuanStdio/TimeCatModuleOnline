package com.timecat.module.user.game.core

import com.timecat.data.bmob.data.User

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/24
 * @description null
 * @usage null
 */

val User.level: Int
    get() = Level.getLevel(exp).first

val User.expLimit: Long
    get() = Level.expLimit(level)

val User.waterLimit: Int
    get() = Level.waterLimit(water)

object Level {

    /**
     * 等级为 level 时的最大经验值限制
     */
    fun expLimit(level: Int): Long = when (level) {
        0 -> 0
        1 -> 24
        in 2..5 -> 8
        in 5..8 -> 16
        8 -> 24
        9 -> 48
        10 -> 56
        11 -> 56
        12 -> 64
        13 -> 76
        in 14..19 -> 82
        19 -> 100
        20 -> 110
        21 -> 138
        22 -> 138
        23 -> 142
        24 -> 146
        25 -> 151
        26 -> 155
        27 -> 160
        28 -> 165
        29 -> 170
        30 -> 174
        31 -> 181
        32 -> 185
        33 -> 191
        34 -> 197
        35 -> 202
        36 -> 209
        37 -> 230
        38 -> 252
        39 -> 278
        40 -> 306
        41 -> 342
        42 -> 383
        43 -> 430
        44 -> 481
        45 -> 558
        46 -> 647
        47 -> 850
        48 -> 871
        49 -> 950
        50 -> 1010
        51 -> 1172
        52 -> 1359
        53 -> 1576
        in 54..65 -> 1770
        in 65..76 -> 2655
        in 76..101 -> 3540
        in 101..164 -> 4425
        else -> 4425
    }

    fun getLevel(exp: Long): Pair<Int, Long> {
        var curExp = exp
        var level = 0
        var maxExp = expLimit(level)
        while (maxExp < curExp) {
            level++
            curExp -= maxExp
            maxExp = expLimit(level);
        }
        return level to curExp
    }

    /**
     * 等级为 level 时的最大累计经验值限制
     */
    fun expAccLimit(level: Int): Long {
        return (0..level).map { expLimit(it) }.sum()
    }

    /**
     * 当前等级为 level 时的最大体力上限
     */
    fun waterLimit(level: Int): Int = when (level) {
        0, 1, 2 -> 20
        3, 4 -> 21
        5, 6 -> 22
        7, 8 -> 23
        9, 10 -> 24
        11, 12 -> 25
        13, 14 -> 26
        15 -> 27
        16 -> 28
        17 -> 30
        18 -> 35
        19 -> 40
        20 -> 45
        21 -> 50
        22 -> 55
        23 -> 60
        24 -> 70
        25 -> 80
        26 -> 85
        //27级及以后
        else -> level + 58
    }
}