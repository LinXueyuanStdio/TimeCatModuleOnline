package com.timecat.module.user.game.task.fragment.card

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/29
 * @description 动态卡池
 * @usage null
 */
open class DynamicCardPoolFragment(
    val ownActivity: OwnActivity
) : NormalCardPoolFragment() {
    override fun loadDetail(user: User) {
        titleItem.text = ownActivity.activity.title
        contentItem.text = ownActivity.activity.content
        onContentLoaded()
    }

    /**
     * 抽卡算法，写成云函数
     */
    override fun requestCard(count: Int) {
        val cards = mutableListOf<Card>()
        for (i in 0..count) {
            cards.add(Card("", 4))
        }
        receiveCard(cards)
    }

}