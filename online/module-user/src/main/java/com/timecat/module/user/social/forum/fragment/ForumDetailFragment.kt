package com.timecat.module.user.social.forum.fragment

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
import com.timecat.module.user.social.forum.vm.ForumViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 论坛的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class ForumDetailFragment : BaseLoginListFragment() {

    private fun loadDetail(block: Block) {
        val list = mutableListOf<BaseItem<*>>()
//        list.add(SingleAuthorItem(forum.user))TODO 不要显示创建着
        val head = ForumBlock.fromJson(block.structure)
        list.add(SimpleContentItem(requireActivity(), block.content, head.atScope, head.topicScope))
        list.add(ActionItem(block))
        list.add(NinePhotoItem(requireActivity(), block.objectId, head.mediaScope))
        adapter.reload(list)
    }

    lateinit var viewModel: ForumViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(ForumViewModel::class.java)
        viewModel.forum.observe(viewLifecycleOwner, {
            it?.let{loadDetail(it)}
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
        viewModel.forum.value?.let { loadDetail(it) }
    }
}