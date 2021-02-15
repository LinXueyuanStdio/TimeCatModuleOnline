package com.timecat.module.user.game.cube.fragment.detail

import androidx.lifecycle.ViewModelProvider
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.module.user.base.login.BaseLoginScrollContainerFragment
import com.timecat.module.user.game.cube.vm.CubeViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description
 * 属性
 * 种族
 * 元素
 * 等级，升级
 * 简单的介绍
 * @usage null
 */
abstract class BaseCubeFragment : BaseLoginScrollContainerFragment() {

    abstract fun loadDetail(ownCube: OwnCube)

    lateinit var viewModel: CubeViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(CubeViewModel::class.java)
        viewModel.cube.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
        })
    }
}