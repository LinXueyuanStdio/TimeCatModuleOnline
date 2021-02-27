package com.timecat.module.user.social.user.fragment

import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.module.user.base.BaseEndlessBlockFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题讨论
 * @usage null
 */
class CommentListFragment : BaseEndlessBlockFragment() {
    override fun name(): String = "讨论"
    override fun query() = userViewModel.user.value!!.findAllComment()
}