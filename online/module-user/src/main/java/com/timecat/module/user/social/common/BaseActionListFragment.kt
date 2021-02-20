package com.timecat.module.user.social.common

import androidx.lifecycle.ViewModelProvider
import com.timecat.module.user.base.BaseEndlessActionFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 动作列表
 * @usage 监听 ViewModel 被动调用 loadData()，不需要自己手动调用
 */
abstract class BaseActionListFragment : BaseEndlessActionFragment() {

    lateinit var blockViewModel: BlockViewModel
    override fun initViewAfterLogin() {
        blockViewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        blockViewModel.block.observe(viewLifecycleOwner, {
            loadData()
        })
    }
}