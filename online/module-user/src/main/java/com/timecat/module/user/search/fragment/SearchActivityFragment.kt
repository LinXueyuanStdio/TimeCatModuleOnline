package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.AVObject
import com.timecat.data.bmob.ext.bmob.blockQuery
import com.timecat.identity.data.block.type.BLOCK_ACTIVITY
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.SelectBlockItem


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/9
 * @description null
 * @usage null
 */
open class SearchActivityFragment : SearchBlockFragment() {
    override fun query(q: String) = blockQuery(q).apply {
        queryString = "type:$BLOCK_ACTIVITY AND $q"
    }

    override fun transform(activity: Activity, block: AVObject): BaseItem<*> {
        return SelectBlockItem(activity, transform(block))
    }
}