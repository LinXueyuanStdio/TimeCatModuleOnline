package com.timecat.module.user.search.fragment

import android.app.Activity
import cn.leancloud.AVObject
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.user.SelectUserItem
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
@FragmentAnno(RouterHub.SEARCH_SelectUserFragment)
class SelectUserFragment : SearchUserFragment() {
    override fun transform(activity: Activity, user: AVObject): BaseItem<*> {
        return SelectUserItem(activity, transform(user))
    }
}