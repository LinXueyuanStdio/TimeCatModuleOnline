package com.timecat.module.user.adapter

import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class DetailAdapter : BaseAdapter {
    constructor(items: MutableList<BaseItem<*>>?) : super(items)
    constructor(items: MutableList<BaseItem<*>>?, listeners: Any?) : super(items, listeners)
    constructor(items: MutableList<BaseItem<*>>?, listeners: Any?, stableIds: Boolean)
            : super(items, listeners, stableIds)

}