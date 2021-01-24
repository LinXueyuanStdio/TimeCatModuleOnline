package com.timecat.module.user.search.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVQuery
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allIdentity
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 选择角色
 * @usage null
 */
@FragmentAnno(RouterHub.SEARCH_SelectIdentityFragment)
class SelectIdentityFragment : SearchBlockFragment() {
    private lateinit var searchResultAdapter: SearchResultAdapter
    override fun getAdapter(): RecyclerView.Adapter<*> {
        searchResultAdapter = SearchResultAdapter(requireContext(), ArrayList())
        return searchResultAdapter
    }

    override fun onSearch(q: String) {
        LogUtil.se(q)
        mStatefulLayout.showLoading()
        //本来是可以直接使用bmob的模糊查询的，但是要付费，所以只能另辟蹊径
        requestBlock {
            query = AVQuery.or(mutableListOf(
                allIdentity().apply { whereContains("title", q) },
                allIdentity().apply { whereContains("content", q) },
                allIdentity().apply { whereContains("objectId", q) }
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
        data: MutableList<Block>
    ) : BaseQuickAdapter<Block, BaseViewHolder>(R.layout.search_item_user, data) {
        override fun convert(holder: BaseViewHolder, item: Block) {
            LogUtil.e(item.toString())
            IconLoader.loadIcon(mContext, holder.getView(R.id.img), "R.drawable.ic_launcher")
            holder.setText(R.id.tv_name, "${item.title}\nuid : ${item.objectId}\n${item.content}")
            holder.getView<View>(R.id.img).setShakelessClickListener {
            }
            holder.getView<View>(R.id.tv_name).setShakelessClickListener {
            }
            holder.getView<TextView>(R.id.follow).setText("选择")
            holder.getView<TextView>(R.id.follow).setShakelessClickListener {
                val intent = Intent()
                intent.putExtra("data", item)
                activity?.setResult(Activity.RESULT_OK, intent)
                activity?.finish()
            }
        }
    }
}