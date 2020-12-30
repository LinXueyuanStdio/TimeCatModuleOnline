package com.timecat.module.user.base

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shuyu.textutillib.model.UserModel
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.module.user.adapter.UserAdapter
import com.timecat.module.user.base.login.BaseLoginListActivity
import java.util.*


class UserListActivity : BaseLoginListActivity() {

    private val userAdapter: UserAdapter = UserAdapter(this, object : UserAdapter.UserListener {
        override fun onClick(user: _User) {
            val intent = Intent()
            intent.putExtra(DATA, UserModel(user.nickName, user.objectId))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    })

    override fun title(): String = "@"

    override fun getAdapter(): RecyclerView.Adapter<*> {
        return userAdapter
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(applicationContext, 2)
    }

    override fun onRefresh() {
        val user = I()
        requestUserRelation {
            query = user.allFollow()
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

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    companion object {
        const val DATA = "data"
    }


}
