package com.timecat.module.user.social.user.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.detail.ContributionItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.adapter.detail.UserRelationItem
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.social.user.vm.UserViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class UserDetailFragment : BaseLoginListFragment() {

    private fun loadDetail(user: _User) {
        val list = mutableListOf<BaseItem<*>>()
        list.add(SimpleContentItem(requireActivity(), user.brief_intro))
        list.add(UserRelationItem(requireActivity(), user))
        list.add(ContributionItem(requireActivity(), user))
        if (user.objectId == UserDao.getCurrentUser()?.objectId) {

        }
        adapter.reload(list)
        mRefreshLayout.isRefreshing = false
    }
    lateinit var viewModel: UserViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.user.observe(viewLifecycleOwner, {
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
        viewModel.user.value?.let {
            loadDetail(it)
        }
    }
}