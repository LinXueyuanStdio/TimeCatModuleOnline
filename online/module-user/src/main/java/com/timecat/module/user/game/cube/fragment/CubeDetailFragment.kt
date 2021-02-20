package com.timecat.module.user.game.cube.fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.router.app.FallBackFragment
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.module.user.base.login.BaseLoginTabsFragment
import com.timecat.module.user.game.cube.fragment.detail.*
import com.timecat.module.user.game.cube.vm.CubeViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class CubeDetailFragment : BaseLoginTabsFragment() {

    private fun loadDetail(cube: OwnCube) {
        //特定种类的方块有特定的养成页面，这个以后再实现
    }

    lateinit var cubeViewModel: CubeViewModel
    override fun initViewAfterLogin() {
        cubeViewModel = ViewModelProvider(requireActivity()).get(CubeViewModel::class.java)
        cubeViewModel.ownCube.observe(viewLifecycleOwner, {
            it?.let { loadDetail(it) }
        })
        setupViewPager()
    }

    override fun getAdapter(): FragmentStatePagerAdapter {
        return DetailAdapter(requireActivity().supportFragmentManager)
    }

    class DetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 2 //TODO 目前实现的只有前两个
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CubeAttrFragment()
                1 -> CubeRolesFragment()
                2 -> CubeSettingFragment()
                3 -> CubeSkillFragment()
                4 -> CubeEquipFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "属性"
                1 -> "权柄"
                2 -> "设置"
                3 -> "技能"
                4 -> "装备"
                else -> super.getPageTitle(position)
            }
        }
    }
}