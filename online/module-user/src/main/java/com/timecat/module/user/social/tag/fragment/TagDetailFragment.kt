package com.timecat.module.user.social.tag.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.social.tag.vm.TagViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class TagDetailFragment : BaseLoginListFragment() {

    private fun loadDetail(block: Block) {
        val list = mutableListOf<BaseItem<*>>()
//        list.add(SingleAuthorItem(block.user))TODO 不要显示创建着
        list.add(SimpleContentItem(requireActivity(), block.content))
        list.add(ActionItem(block))
        adapter.reload(list)
    }

    lateinit var viewModel: TagViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(TagViewModel::class.java)
        viewModel.tag.observe(viewLifecycleOwner, {
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
        viewModel.tag.value?.let {
            loadDetail(it)
        }
    }
}