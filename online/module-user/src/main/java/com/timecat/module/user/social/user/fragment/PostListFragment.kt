package com.timecat.module.user.social.user.fragment

import com.timecat.data.bmob.ext.net.findAllPost
import com.timecat.module.user.base.BaseEndlessBlockFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的帖子
 * @usage null
 */
class PostListFragment : BaseEndlessBlockFragment() {
    override fun name(): String = "帖子"
    override fun query() = userViewModel.user.value!!.findAllPost()
}