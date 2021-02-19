package com.timecat.module.user.social.common


import androidx.fragment.app.FragmentActivity
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.CommentItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题讨论
 * @usage null
 */
class CommentListFragment : BaseBlockListFragment() {
    override fun name(): String = "讨论"
    override fun query() = viewModel.block.value!!.findAllComment()
    override fun addNew(block: Block) {
        GO.addCommentFor(block)
    }
    override fun block2Item(activity: FragmentActivity, block: Block): BaseItem<out BaseDetailVH> {
        return CommentItem(activity, block)
    }
}