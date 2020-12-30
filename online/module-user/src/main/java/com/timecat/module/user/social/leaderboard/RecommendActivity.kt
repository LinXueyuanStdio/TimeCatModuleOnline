package com.timecat.module.user.social.leaderboard


import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.xiaojinzi.component.anno.RouterAnno
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.dao.exec.RecommendDao
import com.timecat.data.bmob.data.common.Exec
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.LEADERBOARD_RecommendActivity)
class RecommendActivity : BaseRefreshListActivity() {

    lateinit var mAdapter: RecommendAdapter

    override fun title(): String = "推荐中心"

    override fun getAdapter(): RecyclerView.Adapter<*> {
        mAdapter = RecommendAdapter(ArrayList())
        return mAdapter
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        val user = UserDao.getCurrentUser()
        if (user == null) {
            mRefreshLayout.isRefreshing = false
            NAV.go(RouterHub.LOGIN_LoginActivity)
            return
        }
        RecommendDao.findAll_ongoing(user, object : OnFindListener<Exec> {
            override fun success(data: List<Exec>) {
                mRefreshLayout.isRefreshing = false
                if (data.isEmpty()) {
                    Toast.makeText(applicationContext, "什么也没找到", Toast.LENGTH_SHORT).show()
                } else {
                    mAdapter.replaceData(data)
                }
            }

            override fun error(e: DataError) {
                mRefreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, "查询失败", Toast.LENGTH_SHORT).show()

            }
        })
    }


}