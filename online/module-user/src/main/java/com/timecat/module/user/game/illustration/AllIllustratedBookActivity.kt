package com.timecat.module.user.game.illustration

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.SubTypeCard
import com.timecat.module.user.base.BaseSimpleListActivity
import com.xiaojinzi.component.anno.RouterAnno
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/24
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllIllustratedBookActivity)
class AllIllustratedBookActivity : BaseSimpleListActivity() {
    override fun title(): String = "图鉴"

    override fun onRefresh() {
        val data = listOf(
            IllustratedBook("物品图鉴", IconLoader.randomAvatar(), RouterHub.USER_AllItemIllustratedActivity),
            IllustratedBook("角色图鉴", IconLoader.randomAvatar(), RouterHub.USER_AllIdentityIllustratedActivity),
        ).map {
            it.toCard(this)
        }
        mAdapter.reload(data)
        mRefreshLayout.isRefreshing = false
    }

    data class IllustratedBook(
        val title: String,
        val icon: String = IconLoader.randomAvatar(),
        val path: String,
    ) {
        fun toSubItem() = SubItem(0, 0, title, path, icon, "", path, UUID.randomUUID().toString())
        fun toCard(context: Context) = SubTypeCard(toSubItem(), context, object : SubTypeCard.Listener {
            override fun loadFor(subItem: SubItem) {
                NAV.go(subItem.desc)
            }

            override fun more(subItem: SubItem) {
                NAV.go(subItem.helpUrl)
            }
        })
    }
}