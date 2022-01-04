package com.timecat.module.user.game.cube.fragment

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

    protected open fun loadDetail(ownCube: OwnCube) {}

    lateinit var cubeViewModel: CubeViewModel
    override fun initViewAfterLogin() {
        cubeViewModel = ViewModelProvider(requireActivity()).get(CubeViewModel::class.java)
        cubeViewModel.ownCube.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
        })
    }
}