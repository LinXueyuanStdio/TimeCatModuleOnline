package com.timecat.module.user.social.user

import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.data.bmob.ext.net.fansOf
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
 * @description id == user.objId 的粉丝列表
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_UserFansListActivity)
class UserFansListActivity : BaseRefreshListActivity() {
    @AttrValueAutowiredAnno("id")
    var id: String? = null
    override fun title(): String {
        return "粉丝列表"
    }

    override fun routerInject() {
        NAV.inject(this)
    }

    lateinit var userAdapter: UserAdapter
    override fun getAdapter(): RecyclerView.Adapter<out RecyclerView.ViewHolder?> {
        userAdapter = UserAdapter(this, object : UserListener {
            override fun onClick(user: User) {
                GO.userDetail(user.objectId)
            }
        })
        return userAdapter
    }

    override fun onRefresh() {
        // 查询当前用户的关注列表
        requestUserRelation {
            query = fansOf(User(id))
            onError = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showError("没有粉丝") {
                    onRefresh()
                }
                ToastUtil.e("粉丝列表查询失败")
                LogUtil.se(it)
            }
            onEmpty = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showError("没有粉丝") {
                    onRefresh()
                }
                ToastUtil.e("当前还未有粉丝，请多造物攒人气吧~")
            }
            onSuccess = {
                mRefreshLayout.isRefreshing = false
                mStatefulLayout?.showContent()
                val users: MutableList<User> = ArrayList()
                for (f in it) {
                    users.add(f.author)
                }
                userAdapter.setList(users)
            }
        }
    }
}