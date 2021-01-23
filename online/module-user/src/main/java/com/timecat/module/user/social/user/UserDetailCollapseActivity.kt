package com.timecat.module.user.social.user

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestOneUser
import com.timecat.data.bmob.ext.bmob.requestUser
import com.timecat.data.bmob.ext.net.oneUserOf
import com.timecat.extend.image.IMG
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseDetailCollapseActivity
import com.timecat.module.user.social.user.fragment.CommentListFragment
import com.timecat.module.user.social.user.fragment.MomentListFragment
import com.timecat.module.user.social.user.fragment.PostListFragment
import com.timecat.module.user.social.user.fragment.UserDetailFragment
import com.timecat.module.user.social.user.vm.UserViewModel
import com.timecat.module.user.view.UserCard
import com.timecat.module.user.view.dsl.setupFollowUserButton
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/8
 * @description 用户信息
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_UserDetailActivity)
class UserDetailCollapseActivity : BaseDetailCollapseActivity() {
    @AttrValueAutowiredAnno("userId")
    lateinit var userId: String
    lateinit var viewModel: UserViewModel
    lateinit var card: UserCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        viewModel.user.observe(this, {
            it?.let { loadDetail(it) }
        })
        card = UserCard(this)
        card.placeholder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = getStatusBarHeightPlusToolbarHeight()
        }
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    private fun loadDetail(user: _User) {
        // 1. 加载头部卡片
        titleString = user.nickName
        card.apply {
            title = user.nickName
            desc = user.brief_intro
            icon = user.avatar
            cover = user.cover
            if (I().objectId == user.objectId) {
                isEnabled = true
                buttonColor = Attr.getPrimaryColor(this@UserDetailCollapseActivity)
                buttonText = "编辑资料"
                buttonClick = {
                    NAV.go(RouterHub.USER_UserSettingActivity, "user", user as Serializable)
                }
            } else {
                setupFollowUserButton(this@UserDetailCollapseActivity, button, user)
            }

            iconClick = {
                IMG.preview(this@UserDetailCollapseActivity)
                    .setIndex(0)
                    .setImageList(listOf(user.avatar))
                    .start()
            }
            coverClick = {
                IMG.preview(this@UserDetailCollapseActivity)
                    .setIndex(0)
                    .setImageList(listOf(user.cover))
                    .start()
            }
        }
    }

    override fun fetch() {
        if (userId == I().objectId) {
            viewModel.user.postValue(I())
        } else {
            requestOneUser {
                query = oneUserOf(userId)
                onSuccess = {
                    viewModel.user.postValue(it)
                }
                onError = {
                    mStatefulLayout?.showError("出错啦") {
                        fetch()
                    }
                }
            }
        }
    }

    override fun getAdapter(): FragmentStatePagerAdapter {
        return DetailAdapter(supportFragmentManager)
    }

    class DetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 4
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> UserDetailFragment()
                1 -> MomentListFragment()
                2 -> PostListFragment()
                3 -> CommentListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "动态"
                2 -> "帖子"
                3 -> "讨论"
                else -> super.getPageTitle(position)
            }
        }
    }

}