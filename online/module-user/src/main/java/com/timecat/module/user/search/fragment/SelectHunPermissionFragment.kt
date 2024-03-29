package com.timecat.module.user.search.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.search.AVSearchQuery
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.blockQuery
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allHunPermission
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.type.BLOCK_PERMISSION
import com.timecat.identity.data.block.type.PERMISSION_Hun
import com.timecat.identity.data.block.type.PERMISSION_Meta
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.adapter.block.SelectBlockItem
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 选择混权限
 * @usage null
 */
@FragmentAnno(RouterHub.SEARCH_SelectHunPermissionFragment)
class SelectHunPermissionFragment : SearchBlockFragment() {
    override fun query(q: String) = blockQuery(q).apply {
        queryString = "type:$BLOCK_PERMISSION AND subtype:$PERMISSION_Hun AND $q"
    }

    override fun transform(activity: Activity, block: AVObject): BaseItem<*> {
        return SelectBlockItem(activity, transform(block))
    }
}