package com.timecat.module.user.game.cube.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.game.agent.OwnCube
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.game.cube.vm.CubeViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class CubeSkillFragment : BaseLoginListFragment() {

    private fun loadDetail(cube: OwnCube) {
        val list = mutableListOf<BaseItem<*>>()
//        list.add(SingleAuthorItem(forum.user))TODO 不要显示创建着
//        list.add(SimpleContentItem(requireActivity(), cube.content))
//        list.add(ActionItem(cube))
        adapter.reload(list)
    }

    lateinit var viewModel: CubeViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(CubeViewModel::class.java)
        viewModel.cube.observe(viewLifecycleOwner, {
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
        viewModel.cube.value?.let { loadDetail(it) }
    }
}