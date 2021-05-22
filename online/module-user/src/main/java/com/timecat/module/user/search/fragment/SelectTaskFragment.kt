package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.AVObject
import cn.leancloud.search.AVSearchQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.blockQuery
import com.timecat.identity.data.block.type.BLOCK_APP
import com.timecat.identity.data.block.type.BLOCK_TASK
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.block.SelectBlockItem
import com.xiaojinzi.component.anno.FragmentAnno


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/9
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.SEARCH_SelectTaskFragment)
class SelectTaskFragment : SearchTaskFragment() {
    override fun transform(activity: Activity, block: AVObject): BaseItem<*> {
        return SelectBlockItem(activity, transform(block))
    }
}