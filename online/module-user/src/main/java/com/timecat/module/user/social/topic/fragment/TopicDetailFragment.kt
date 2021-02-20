package com.timecat.module.user.social.topic.fragment

import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.TopicBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.social.common.BaseBlockDetailFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class TopicDetailFragment : BaseBlockDetailFragment() {

    override fun loadDetail(block: Block) {
        val list = mutableListOf<BaseItem<*>>()
//        list.add(SingleAuthorItem(forum.user))TODO 不要显示创建着
        val head = TopicBlock.fromJson(block.structure)
        list.add(SimpleContentItem(requireActivity(), block.content, head.atScope, head.topicScope))
        list.add(ActionItem(block))
        adapter.reload(list)
    }
}