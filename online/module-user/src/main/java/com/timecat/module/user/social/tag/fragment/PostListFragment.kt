package com.timecat.module.user.social.tag.fragment

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllPost
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 标签的帖子
 * @usage null
 */
class PostListFragment : BaseListFragment() {
    override fun name(): String = "帖子"
    override fun query(): BmobQuery<Block> = viewModel.tag.value!!.findAllPost()
    override fun addNew(block: Block) {
        GO.addPostFor(block)
    }
}