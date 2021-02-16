package com.timecat.module.user.game.cube.fragment.detail

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.bmob.requestBlockRelation
import com.timecat.data.bmob.ext.net.findAllRole
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.block.RoleItem
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
class CubeRolesFragment : BaseLoginListFragment() {

    private fun loadDetail(cube: OwnCube) {
        val block = cube.cube
        requestBlockRelation {
            query = block.findAllRole()
            onError = {
                mStatefulLayout?.showError {
                    loadDetail(cube)
                }
            }
            onEmpty = {
                LogUtil.e("empty")
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                roles = it.map { it.to }
                mStatefulLayout?.showContent()
            }
        }
    }

    var roles: List<Block> = listOf()
        set(value) {
            val list = value.map { RoleItem(requireActivity(), it) }
            adapter.reload(list)
            field = value
        }

    lateinit var viewModel: CubeViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(CubeViewModel::class.java)
        viewModel.ownCube.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
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
        viewModel.ownCube.value?.let { loadDetail(it) }
    }
}