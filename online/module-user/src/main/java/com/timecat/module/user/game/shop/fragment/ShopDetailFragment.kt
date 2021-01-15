package com.timecat.module.user.game.shop.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.game.shop.vm.ShopViewModel
import com.timecat.module.user.social.topic.vm.TopicViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 商店的详细信息
 * 1. 简要介绍商店（content、截止日期等等）
 * 2. 有什么商品
 * @usage null
 */
class ShopDetailFragment : BaseLoginListFragment() {

    private fun loadDetail(forum: Block) {
        val list = mutableListOf<BaseItem<*>>()
//        list.add(SingleAuthorItem(forum.user))TODO 不要显示创建着
        list.add(SimpleContentItem(requireActivity(), forum.content))
        list.add(ActionItem(forum))
        adapter.reload(list)
    }

    lateinit var viewModel: ShopViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(ShopViewModel::class.java)
        viewModel.shop.observe(viewLifecycleOwner, {
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
        viewModel.shop.value?.let {
            loadDetail(it)
        }
    }
}