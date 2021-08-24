package com.timecat.module.user.app.online

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allMoment
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.service.HomeService
import com.timecat.module.user.adapter.block.MomentItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
class UserMomentRecommend(
    I: User,
    pageSize: Int = 10,
) : UserRecommend(I, pageSize) {
    override fun transform(context: Context, block: Block, homeService: HomeService): BaseItem<*> {
        return MomentItem(context, block)
    }

    override fun query(): AVQuery<Block> {
        // 合并两个条件，进行"或"查询
        // 查询 我关注的人的动态 和 自己的动态
        return allMoment()
    }
}