package com.timecat.module.user.social.user

import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.UserAdapter
import com.timecat.module.user.adapter.UserAdapter.UserListener
import com.timecat.module.user.base.GO
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description id == user.objId 的关注列表
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_UserFollowListActivity)
class UserFollowListActivity : BaseRefreshListActivity() {
    @JvmField
    @AttrValueAutowiredAnno("id")
    var id: String? = null
    override fun title(): String {
        return "关注列表"
    }

    override fun routerInject() {
        NAV.inject(this)
    }

    lateinit var userAdapter: UserAdapter
    override fun getAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder?> {
        userAdapter = UserAdapter(this, object : UserListener {
            override fun onClick(user: _User) {
                GO.userDetail(user.objectId)
            }
        })
        return userAdapter
    }

    override fun onRefresh() {
        // 查询当前用户的关注列表
        requestUserRelation {
            query = _User(id).allFollow()
            onError = {
                mRefreshLayout.isRefreshing = false
                ToastUtil.e("关注列表查询失败")
                LogUtil.se(it)
                mStatefulLayout?.showError("没有关注的用户") {
                    onRefresh()
                }
            }
            onEmpty = {
                mRefreshLayout.isRefreshing = false
                ToastUtil.e("还没关注任何人哦")
                mStatefulLayout?.showError("没有关注的用户") {
                    onRefresh()
                }
            }
            onSuccess = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                val users: MutableList<_User> = ArrayList()
                it.target?.let {
                    users.add(it)
                }
                userAdapter.setList(users)
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
                userAdapter.setList(users)
            }
        }
    }
}