package com.timecat.module.user.base

import androidx.lifecycle.ViewModelProvider
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.base.login.BaseLoginScrollContainerFragment
import com.timecat.module.user.social.common.BlockViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description
 * @usage null
 */
abstract class BaseBlockFragment : BaseLoginScrollContainerFragment() {

    protected open fun loadDetail(block: Block) {}

    lateinit var blockViewModel: BlockViewModel
    override fun initViewAfterLogin() {
        blockViewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        blockViewModel.block.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
        })
    }
}