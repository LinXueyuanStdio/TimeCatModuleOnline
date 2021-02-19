package com.timecat.module.user.base

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.gturedi.views.CustomStateOptions
import com.timecat.component.router.app.NAV
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.service.DataError
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginListActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description null
 * @usage null
 */
abstract class BaseSimpleListActivity : BaseLoginListActivity() {
    lateinit var mAdapter: BaseAdapter

    override fun routerInject() = NAV.inject(this)

    override fun getAdapter(): RecyclerView.Adapter<*> {
        mAdapter = BaseAdapter(null)
        return mAdapter
    }

    override fun initViewAfterLogin() {
        mAdapter.setDisplayHeadersAtStartUp(false) //Show Headers at startUp!
            .setStickyHeaders(false) //Make headers sticky
            .setLoadingMoreAtStartUp(false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mAdapter.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mAdapter.onRestoreInstanceState(savedInstanceState)
    }

    var errorCallback: (DataError) -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showError("查询失败") {
            mRefreshLayout.isRefreshing = true
            mStatefulLayout?.showLoading()
            onRefresh()
        }
        ToastUtil.e("查询失败")
        it.printStackTrace()
    }
    var emptyCallback: () -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showCustom(
            CustomStateOptions()
                .buttonText("什么也没找到")
                .image(R.drawable.stf_ic_empty)
                .buttonClickListener {
                    onRefresh()
                }
        )
    }
}