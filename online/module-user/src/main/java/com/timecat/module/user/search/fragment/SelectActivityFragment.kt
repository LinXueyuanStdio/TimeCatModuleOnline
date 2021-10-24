package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.AVObject
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
@FragmentAnno(RouterHub.SEARCH_SelectActivityFragment)
class SelectActivityFragment : SearchActivityFragment() {
    override fun transform(activity: Activity, block: AVObject): BaseItem<*> {
        return SelectBlockItem(activity, transform(block))
    }
}