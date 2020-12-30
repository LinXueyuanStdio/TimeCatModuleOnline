package com.timecat.module.user.social.user.fragment

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.FindListener
import com.timecat.data.bmob.dao.TraceDao
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.trace.Trace
import com.timecat.page.base.friend.list.BaseStatefulListFragment
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.TraceAdapter
import com.timecat.module.user.adapter.TraceItem
import com.xiaojinzi.component.anno.FragmentAnno
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 足迹
 * Trace 是 log，指向 string 的 target id，持有一个 type 和 target type
 * @usage null
 */
@FragmentAnno(RouterHub.USER_TraceFragment)
class TraceFragment : BaseStatefulListFragment() {
    private var adapter: TraceAdapter? = null
    override fun loadData() {
        if (mStatefulLayout != null) {
            mStatefulLayout.showLoading()
            onRefresh()
        }
    }

    override fun getAdapter(): RecyclerView.Adapter<*> {
        if (adapter == null) adapter = TraceAdapter(ArrayList())
        return adapter!!
    }

    fun onRefresh() {
        TraceDao.getAllTrace(UserDao.getCurrentUser())
            .findObjects(object : FindListener<Trace?>() {
                override fun done(list: List<Trace?>, e: BmobException) {
                    if (list != null) {
                        val items: MutableList<TraceItem> = ArrayList()
                        for (t in list) {
                            items.add(TraceItem(t!!))
                        }
                        adapter!!.replaceData(items)
                    }
                    if (e != null || list == null || list.size <= 0) {
                        if (mStatefulLayout != null) {
                            mStatefulLayout.showError("没有足迹👣") { v: View? -> onRefresh() }
                        }
                    } else {
                        if (mStatefulLayout != null) {
                            mStatefulLayout.showContent()
                        }
                    }
                }
            })
    }
}