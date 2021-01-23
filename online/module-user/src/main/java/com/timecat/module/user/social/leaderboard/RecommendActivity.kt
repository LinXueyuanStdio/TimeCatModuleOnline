package com.timecat.module.user.social.leaderboard


import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.readonly.RouterHub
import com.timecat.page.base.friend.toolbar.BaseRefreshListActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
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

    @AttrValueAutowiredAnno("block")
    @JvmField
    var block: Block? = null
    override fun routerInject() = NAV.inject(this)
    lateinit var mAdapter: RecommendAdapter

    override fun title(): String = "推荐中心"

    override fun getAdapter(): RecyclerView.Adapter<*> {
        mAdapter = RecommendAdapter(ArrayList())
        return mAdapter
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        val user = UserDao.getCurrentUser()
    }

}