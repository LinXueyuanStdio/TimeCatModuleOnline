package com.timecat.module.user.social.leaderboard.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.LeaderBoardBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.social.common.BlockViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 排行榜的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class LeaderBoardDetailFragment : BaseLoginListFragment() {

    private fun loadDetail(forum: Block) {
        val list = mutableListOf<BaseItem<*>>()
//        list.add(SingleAuthorItem(forum.user))TODO 不要显示创建着
        val head = LeaderBoardBlock.fromJson(forum.structure)
        list.add(SimpleContentItem(requireActivity(), forum.content, head.atScope, head.topicScope))
        list.add(ActionItem(forum))
        adapter.reload(list)
    }

    lateinit var viewModel: BlockViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        viewModel.block.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
        })
    }

    lateinit var adapter: DetailAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        adapter = DetailAdapter(ArrayList())
        return adapter
    }
}