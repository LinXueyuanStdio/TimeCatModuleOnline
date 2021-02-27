package com.timecat.module.user.social.user.fragment

import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.module.user.base.BaseEndlessBlockFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 转发的动态
 * @usage null
 */
class MomentListFragment : BaseEndlessBlockFragment() {
    override fun name(): String = "转发的动态"
    override fun query() = userViewModel.user.value!!.findAllMoment()
}