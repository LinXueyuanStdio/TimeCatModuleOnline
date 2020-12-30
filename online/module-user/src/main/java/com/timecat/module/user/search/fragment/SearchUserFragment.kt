package com.timecat.module.user.search.fragment

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.requestUser
import com.timecat.component.commonsdk.utils.override.LogUtil

import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.view.dsl.setupFollowUserButton
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
@FragmentAnno(RouterHub.SEARCH_SearchUserFragment)
class SearchUserFragment : BaseSearchFragment() {
    private lateinit var searchResultAdapter: SearchResultAdapter
    override fun getAdapter(): RecyclerView.Adapter<*> {
        searchResultAdapter = SearchResultAdapter(requireContext(), ArrayList())
        return searchResultAdapter
    }

    override fun onSearch(q: String) {
        LogUtil.se(q)
        mStatefulLayout.showLoading()
        //本来是可以直接使用bmob的模糊查询的，但是要付费，所以只能另辟蹊径
        requestUser {
            query = BmobQuery<_User>().or(mutableListOf(
                BmobQuery<_User>().apply { addWhereContains("username", q) },
                BmobQuery<_User>().apply { addWhereContains("objectId", q) }
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
                searchResultAdapter.setList(listOf(it))
            }
            onListSuccess = {
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
            holder.setText(R.id.tv_name, "${item.username}\nuid : ${item.objectId}")
            holder.getView<View>(R.id.img).setShakelessClickListener {
                GO.userDetail(item.objectId)
            }
            holder.getView<View>(R.id.tv_name).setShakelessClickListener {
                GO.userDetail(item.objectId)
            }
            setupFollowUserButton(requireContext(), holder.getView(R.id.follow), item)
        }
    }
}