package com.timecat.module.user.social.user.fragment

import androidx.lifecycle.ViewModelProvider
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.timecat.module.user.social.user.vm.UserViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 用户所创建的子块组成的列表
 * @usage null
 */
abstract class BaseListFragment : BaseEndlessBlockFragment() {

    lateinit var viewModel: UserViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        viewModel.user.observe(viewLifecycleOwner, {
            load()
        })
    }

    override fun loadData() {}
}