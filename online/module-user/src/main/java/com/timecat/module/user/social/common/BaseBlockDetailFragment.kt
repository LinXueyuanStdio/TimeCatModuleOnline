package com.timecat.module.user.social.common

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.base.login.BaseLoginListFragment
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 排行榜的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
open class BaseBlockDetailFragment : BaseLoginListFragment() {

    open fun loadDetail(block: Block) {}

    lateinit var blockViewModel: BlockViewModel
    override fun initViewAfterLogin() {
        blockViewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        blockViewModel.block.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
        })
    }

    lateinit var adapter: DetailAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        adapter = DetailAdapter(ArrayList())
        return adapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        adapter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            adapter.onRestoreInstanceState(savedInstanceState)
        }
    }
}