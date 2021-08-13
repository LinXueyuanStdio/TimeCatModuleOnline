package com.timecat.module.user.app.block

import android.content.Context
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.service.*
import com.timecat.module.user.app.online.HomeHeaderCard
import com.timecat.module.user.app.online.MapSubItem
import com.timecat.module.user.app.online.TimeCatOnline
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/6
 * @description null
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = [RouterHub.GLOBAL_BlockDetailService])
class OnlineBlockDetailServiceImpl : ContainerService {

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        TimeCatOnline.parsePath(parentUuid,
            onSpace = {

            }, onBlock = {

        }, onFail = {

        })
//        homeService.loadDatabase()
        homeService.loadContextRecord(null)
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        val tab = TimeCatOnline.parseTabPath(path.uuid)
        homeService.loadMenu(EmptyMenuContext())

        val allTabs = TimeCatOnline.homeMapSubItems
        val tabIdx = allTabs.indexOfFirst { it.title == tab }
        val selectedIdx = if (tabIdx == -1) 0 else tabIdx
        val header = HomeHeaderCard(allTabs, selectedIdx, object : HomeHeaderCard.Listener {
            override fun onSelect(item: MapSubItem) {
                homeService.resetTo(item.toPath())
            }
        })
        homeService.loadHeader(listOf(header))
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.loadCommand(EmptyCommandContext())
        homeService.loadChipButtons(listOf())
        homeService.loadChipType(listOf())
        homeService.reloadData()
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        callback.onEmpty("页面未实现") {}
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        callback.onEmpty("页面未实现")
    }

}
