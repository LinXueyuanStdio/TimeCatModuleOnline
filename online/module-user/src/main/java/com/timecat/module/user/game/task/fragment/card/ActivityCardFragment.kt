package com.timecat.module.user.game.task.fragment.card

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.game.task.fragment.BaseActivityTabsFragment
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
@FragmentAnno(RouterHub.USER_ActivityCardFragment)
class ActivityCardFragment : BaseActivityTabsFragment() {
    override fun getAdapter(owns: List<OwnActivity>): FragmentStatePagerAdapter {
        return DetailAdapter(childFragmentManager, owns)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel.cardActivities.observe(viewLifecycleOwner) {
            setupViewPager(getAdapter(it))
        }
    }

    class DetailAdapter(
        fm: FragmentManager,
        val owns: List<OwnActivity>
    ) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            //1个常驻卡池+其他动态发布的卡池
            return 1 + owns.size
        }

        override fun getItem(position: Int): Fragment {
            if (position >= owns.size) {
                return NormalCardPoolFragment()
            }
            val own = owns[position]
            return DynamicCardPoolFragment(own)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            if (position >= owns.size) {
                return "常驻"
            }
            val own = owns[position]
            return own.activity.title
        }
    }


}