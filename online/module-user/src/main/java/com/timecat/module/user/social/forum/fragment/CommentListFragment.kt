package com.timecat.module.user.social.forum.fragment


import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题讨论
 * @usage null
 */
class CommentListFragment : BaseListFragment() {
    override fun name(): String = "讨论"
    override fun query() = viewModel.forum.value!!.findAllComment()
    override fun addNew(block: Block) {
        GO.addCommentFor(block)
    }
}