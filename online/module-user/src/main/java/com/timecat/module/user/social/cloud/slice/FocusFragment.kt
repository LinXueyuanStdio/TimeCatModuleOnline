package com.timecat.module.user.social.cloud.slice

import cn.bmob.v3.BmobQuery
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.xiaojinzi.component.anno.FragmentAnno
import java.util.*

/**
 * 关注
 */
@FragmentAnno(RouterHub.USER_FocusFragment)
class FocusFragment : BaseEndlessBlockFragment() {
    var focus_ids: List<_User> = mutableListOf()

    override fun loadData() {
        // 查询关注的所有用户，多对多关联，因此查询的是用户表
        // 合并两个条件，进行"或"查询
        requestUserRelation {
            query = I().allFollow()
            onError = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showError("没有关注的用户") {
                    loadData()
                }
                ToastUtil.e( "关注列表查询失败")
                LogUtil.se(it)
            }
            onEmpty = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                ToastUtil.e( "还没关注任何人哦")
                focus_ids = mutableListOf()
                load()
            }
            onSuccess = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                val users: MutableList<_User> = ArrayList()
                it.target?.let {
                    users.add(it)
                }
                focus_ids = users
                load()
            }
            onListSuccess = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                val users: MutableList<_User> = ArrayList()
                for (f in it) {
                    f.target?.let {
                        users.add(it)
                    }
                }
                focus_ids = users
                load()
            }
        }
    }

    override fun name() = "动态"
    override fun query(): BmobQuery<Block> {
        // 合并两个条件，进行"或"查询
        // 查询 我关注的人的动态 和 自己的动态
        val queries: MutableList<BmobQuery<Block>> = ArrayList()
        for (user in focus_ids) {
            queries.add(user.findAllMoment())
        }
        queries.add(I().findAllMoment())
        val mainQuery = BmobQuery<Block>()
        mainQuery.or(queries)
        mainQuery.include("user,parent")
        mainQuery.order("-createdAt")
        return mainQuery
    }
}