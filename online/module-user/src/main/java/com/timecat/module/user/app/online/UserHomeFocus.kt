package com.timecat.module.user.app.online

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.service.HomeService
import com.timecat.module.user.adapter.block.MomentItem
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
class UserHomeFocus(
    I: User,
    pageSize: Int = 10,
    focus_ids: List<User> = mutableListOf()
) : UserFocus(I, pageSize, focus_ids) {
    override fun transform(context: Context, block: Block, homeService: HomeService): BaseItem<*> {
        return MomentItem(context, block)
    }

    override fun query(): AVQuery<Block> {
        // 合并两个条件，进行"或"查询
        // 查询 我关注的人的动态 和 自己的动态
        val queries: MutableList<AVQuery<Block>> = ArrayList()
        for (user in focus_ids) {
            queries.add(user.findAllMoment())
        }
        queries.add(I.findAllMoment())
        return AVQuery.or(queries)
            .include("user")
            .include("parent")
            .order("-createdAt")
    }

}