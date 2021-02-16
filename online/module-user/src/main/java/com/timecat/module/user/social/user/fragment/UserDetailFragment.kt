package com.timecat.module.user.social.user.fragment

import android.view.View
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Next
import com.timecat.module.user.base.login.BaseLoginScrollContainerFragment
import com.timecat.module.user.view.dsl.Content
import com.timecat.module.user.view.dsl.Contribution
import com.timecat.module.user.view.dsl.UserRelation
import com.timecat.module.user.view.item.ContentItem
import com.timecat.module.user.view.item.ContributionItem
import com.timecat.module.user.view.item.UserRelationItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class UserDetailFragment : BaseLoginScrollContainerFragment() {

    lateinit var contentItem: ContentItem
    lateinit var userRelationItem: UserRelationItem
    lateinit var contributionItem: ContributionItem
    lateinit var traceItem: View
    override fun loadDetail(user: User) {
        container.apply {
            contentItem = Content(user.intro)
            userRelationItem = UserRelation()
            contributionItem = Contribution()
            if (user.objectId == UserDao.getCurrentUser()?.objectId) {
                traceItem = Next("浏览记录") {
                    NAV.raw(activity, RouterHub.USER_AllTraceActivity)
                        .putParcelable("user", user)
                        .forward()
                }
            }
        }
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        userViewModel.intro.observe(this) {
            contentItem.setRichText(it)
        }
    }
}