package com.timecat.module.user.search.fragment


import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVQuery
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestUser
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/9
 * @description null
 * @usage null
 */
class SearchAppFragment : BaseSearchFragment() {
    private lateinit var searchResultAdapter: SearchResultAdapter

    override fun getAdapter(): RecyclerView.Adapter<*> {
        searchResultAdapter = SearchResultAdapter(requireContext(), ArrayList())
        return searchResultAdapter
    }

    override fun onSearch(q: String) {
        mStatefulLayout.showLoading()
        //本来是可以直接使用bmob的模糊查询的，但是要付费，所以只能另辟蹊径
        requestUser {
            query = AVQuery.or(mutableListOf(
                AVQuery<_User>("_User").apply { whereContains("username", q) },
                AVQuery<_User>("_User").apply { whereContains("objectId", q) }
            ))
            onError = {
                mStatefulLayout.showEmpty()
                ToastUtil.e("查询失败")
            }
            onEmpty = {
                mStatefulLayout.showEmpty()
            }
            onSuccess = {
                mStatefulLayout.showContent()
                searchResultAdapter.setList(it)
            }
        }
    }

    inner class SearchResultAdapter(
        private val mContext: Context,
        data: MutableList<_User>
    ) : BaseQuickAdapter<_User, BaseViewHolder>(R.layout.search_item_user, data) {
        override fun convert(holder: BaseViewHolder, item: _User) {
            LogUtil.e(item.toString())
            IconLoader.loadIcon(mContext, holder.getView(R.id.img), item.avatar)
            holder.setText(R.id.tv_name, "${item.username}\nid : ${item.objectId}")
            holder.getView<View>(R.id.img).setShakelessClickListener {
                GO.userDetail(item.objectId)
            }
            holder.getView<View>(R.id.tv_name).setShakelessClickListener {
                GO.userDetail(item.objectId)
            }
            holder.getView<View>(R.id.follow).setShakelessClickListener { v ->
            }
        }
    }
}