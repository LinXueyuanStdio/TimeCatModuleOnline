package com.timecat.module.user.social.leaderboard.fragment

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.module.user.base.GO
import com.timecat.module.user.social.common.BaseBlockListFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 推荐制，有身份的人来推荐
 * @usage null
 */
class RecommendListFragment : BaseBlockListFragment() {
    override fun name(): String = "推荐"
    override fun query() = viewModel.block.value!!.findAllComment() //TODO
    override fun addNew(block: Block) {
        GO.addCommentFor(block) // TODO
    }
}