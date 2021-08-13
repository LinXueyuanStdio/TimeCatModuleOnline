package com.timecat.module.user.app.online

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.SubTypeCard
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.HomeService

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */

/**
 * 时光猫在线服务，路由地图
 */
class MapSubItem(
    title: String,
    desc: String,
    icon: String,
    uuid: String
) : SubItem(0, 0, title, desc, icon, "", RouterHub.ABOUT_HelpActivity, uuid)

fun Map(title: String) = TypeItem(0, title, title)

fun SubItem.toPath(): Path = Path(title, uuid, -1000)

fun mapSubItem2Card(context: Context, homeService: HomeService, subItem: SubItem): SubTypeCard {
    return SubTypeCard(subItem, context, object : SubTypeCard.Listener {
        override fun loadFor(subItem: SubItem) {
            homeService.resetTo(subItem.toPath())
        }

        override fun more(subItem: SubItem) {
            NAV.go(subItem.helpUrl)
        }
    })
}