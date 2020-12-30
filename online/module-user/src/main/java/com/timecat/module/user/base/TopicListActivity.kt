package com.timecat.module.user.base

import android.app.Activity
import android.content.Intent
import com.shuyu.textutillib.model.TopicModel
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.block.TopicDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.adapter.BlockItem
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener


class TopicListActivity : BaseBlockListActivity() {

    override fun title(): String = "选择话题"

    override fun onRefresh() {
        TopicDao.getAll(object : OnFindListener<Block> {
            override fun success(data: List<Block>) {
                mRefreshLayout.isRefreshing = false
                if (data.isEmpty()) {
                    ToastUtil.w("什么也没找到")
                } else {
                    mAdapter.onClick = {
                        val intent = Intent()
                        intent.putExtra(DATA, TopicModel(it.title, it.objectId))
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                    mAdapter.setList(data.map { BlockItem(it) })
                }
            }

            override fun error(e: DataError) {
                mRefreshLayout.isRefreshing = false
                ToastUtil.w("查询失败")
            }
        })
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    companion object {
        const val DATA = "data"
    }
}
