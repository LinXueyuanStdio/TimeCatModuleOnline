package com.timecat.module.user.base

import androidx.recyclerview.widget.RecyclerView

import com.gturedi.views.CustomStateOptions
import com.timecat.element.alert.ToastUtil
import com.timecat.component.router.app.NAV
import com.timecat.module.user.R
import com.timecat.module.user.adapter.BlockAdapter
import com.timecat.module.user.base.login.BaseLoginListActivity
import com.timecat.identity.data.service.DataError
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/15
 * @description null
 * @usage null
 */
abstract class BaseBlockListActivity : BaseLoginListActivity() {
    lateinit var mAdapter: BlockAdapter

    override fun routerInject() = NAV.inject(this)

    override fun getAdapter(): RecyclerView.Adapter<*> {
        mAdapter = BlockAdapter(this, ArrayList())
        return mAdapter
    }

    var errorCallback: (DataError) -> Unit = {
        mRefreshLayout.isRefreshing = false
        mStatefulLayout?.showError("查询失败") {
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