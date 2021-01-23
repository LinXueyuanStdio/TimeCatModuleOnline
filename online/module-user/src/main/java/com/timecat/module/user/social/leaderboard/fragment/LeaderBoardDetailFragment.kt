package com.timecat.module.user.social.leaderboard.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ForumBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.NinePhotoItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.social.leaderboard.vm.LeaderBoardViewModel
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
        val header = ForumBlock.fromJson(forum.structure)
        list.add(SimpleContentItem(requireActivity(), forum.content))
        list.add(ActionItem(forum))
        list.add(NinePhotoItem(requireActivity(), forum.objectId, header.mediaScope))
        adapter.reload(list)
    }

    lateinit var viewModel: LeaderBoardViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(LeaderBoardViewModel::class.java)
        viewModel.board.observe(viewLifecycleOwner, {
            loadDetail(it)
        })
    }

    lateinit var adapter: DetailAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        adapter = DetailAdapter(ArrayList())
        return adapter
    }

    //第一次不加载啦，交给 ViewModel
    override fun loadData() {
        mRefreshLayout.isRefreshing = false
    }

    override fun onRefresh() {
        viewModel.board.value?.let {
            loadDetail(it)
        }
    }
}