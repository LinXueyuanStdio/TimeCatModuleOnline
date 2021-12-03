package com.timecat.module.user.social.comment

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVQuery
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.gturedi.views.StatefulLayout
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.findAllSubComment
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.service.DataError
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.ToolbarUtils
import com.timecat.module.user.R
import com.timecat.module.user.adapter.block.CommentItem
import com.timecat.module.user.adapter.block.NotMoreItem
import com.timecat.module.user.base.GO
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.Payload

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/9
 * @description null
 * @usage null
 */
@FragmentAnno(USER_CommentDetailBottomSheet)
class CommentDetailBottomSheet : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("block")
    var _block: Block?=null
    lateinit var block: Block

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        block = _block!!
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
        loadData()
    }

    lateinit var toolbar: Toolbar
    lateinit var rv: RecyclerView
    lateinit var mStatefulLayout: StatefulLayout
    lateinit var source: TextView
    lateinit var response: TextView
    lateinit var background: View
    fun buildView(context: Context): View {
        val root = LayoutInflater.from(context).inflate(R.layout.user_comment_bottom_sheet, null)
        background = root.findViewById(R.id.background)
        toolbar = root.findViewById(R.id.toolbar)
        source = root.findViewById(R.id.source)
        mStatefulLayout = root.findViewById(R.id.ll_stateful)
        rv = root.findViewById(R.id.rv)
        response = root.findViewById(R.id.response)

        ToolbarUtils.initToolbarNav(toolbar, false) {
            dismiss()
        }
        toolbar.apply {
            setNavigationIcon(R.drawable.ic_close)
            val color = Attr.getSecondaryTextColor(context)
            navigationIcon?.setTintList(ColorStateList.valueOf(color))
            setTitleTextColor(color)
            title = "回复"
        }
        rv.apply {
            setLayoutManager(LinearLayoutManager(context))
            setItemAnimator(DefaultItemAnimator())
            setHasFixedSize(true) //Size of RV will not change

            val animationController = AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_on_load)
            setLayoutAnimation(animationController)

            mAdapter = BaseAdapter(null)
            mAdapter.addScrollableFooter(NotMoreItem())
            setAdapter(mAdapter)
            mAdapter.setDisplayHeadersAtStartUp(false) //Show Headers at startUp!
                .setStickyHeaders(false) //Make headers sticky
                .setLoadingMoreAtStartUp(false)
                .setEndlessPageSize(pageSize)  //if newItems < 7
                .setEndlessScrollThreshold(4) //Default=1
                .setEndlessScrollListener(object : FlexibleAdapter.EndlessScrollListener {
                    override fun noMoreLoad(newItemsSize: Int) {
                    }

                    override fun onLoadMore(lastPosition: Int, currentPage: Int) {
                        loadMore()
                    }
                }, notMoreItem)
        }
        source.setShakelessClickListener {
            GO.toAnyDetail(block.parent)
        }
        response.setShakelessClickListener {
            GO.replyComment(block)
        }
        return root
    }


    private val notMoreItem: NotMoreItem = NotMoreItem()
    lateinit var mAdapter: BaseAdapter
    fun loadData() {
        mStatefulLayout.showLoading()
        load()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mAdapter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            mAdapter.onRestoreInstanceState(savedInstanceState)
        }
    }

    var errorCallback: (DataError) -> Unit = {
        mStatefulLayout.showError("查询失败") {
            loadData()
        }
        ToastUtil.e("查询失败")
        it.printStackTrace()
    }
    var emptyCallback: () -> Unit = {
        mStatefulLayout.showContent()
        mAdapter.updateItem(notMoreItem, Payload.NO_MORE_LOAD)
    }

    var pageSize = 10
    var offset = 0

    open fun load() {
        offset = 0
        mAdapter.clear()
        mAdapter.setEndlessProgressItem(notMoreItem)
        loadFirst()
    }

    fun query(): AVQuery<Block> {
        return block.findAllSubComment()
    }

    fun loadFirst() {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onError = errorCallback
            onEmpty = {
                val activity = requireActivity()
                val items = mutableListOf(CommentItem(activity, block, true))
                mAdapter.reload(items)
                mAdapter.updateItem(notMoreItem, Payload.NO_MORE_LOAD)
                mStatefulLayout.showContent()
            }
            onSuccess = {
                offset += it.size
                val activity = requireActivity()
                val items = mutableListOf(CommentItem(activity, block, true))
                items.addAll(it.map {
                    CommentItem(activity, it)
                })
                mAdapter.reload(items)
                mStatefulLayout.showContent()
            }
        }
    }

    fun loadMore() {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onError = errorCallback
            onEmpty = emptyCallback
            onSuccess = {
                offset += it.size
                val activity = requireActivity()
                val items = it.map {
                    CommentItem(activity, it)
                }
                mAdapter.onLoadMoreComplete(items)
                mStatefulLayout.showContent()
            }
        }
    }
}