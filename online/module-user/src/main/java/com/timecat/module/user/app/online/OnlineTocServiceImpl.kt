package com.timecat.module.user.app.online

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.adapter.SubTypeCard
import com.timecat.middle.block.adapter.TypeCard
import com.timecat.middle.block.service.*
import com.timecat.module.master.adapter.item.virtual.SubItem
import com.timecat.module.master.adapter.item.virtual.TypeItem
import com.timecat.module.user.ext.GLOBAL_OnlineHomeService
import com.timecat.module.user.ext.GLOBAL_OnlineMomentService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/6
 * @description null
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = [RouterHub.GLOBAL_OnlineTocService])
class OnlineTocServiceImpl : ContainerService {

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        homeService.loadContextRecord(null)
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        homeService.loadMenu(EmptyMenuContext())
        homeService.loadHeader(listOf())
        homeService.loadChipButtons(listOf())
        homeService.loadChipType(listOf())
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.loadCommand(EmptyCommandContext())
        homeService.reloadData()
    }

    override fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ) {
        loadForType(context, parentUuid, homeService, callback)
    }

    override fun loadMoreForVirtualPath(
        context: Context,
        parentUuid: String,
        offset: Int,
        homeService: HomeService,
        callback: ContainerService.LoadMoreCallback
    ) {
        callback.onVirtualLoadSuccess(listOf())
    }

    fun mapSubItem2Card(context: Context, homeService: HomeService, subItem: MapSubItem): SubTypeCard {
        return SubTypeCard(subItem, context, object : SubTypeCard.Listener {
            override fun loadFor(subItem: SubItem) {
                val path = Path(subItem.title, subItem.uuid, -1000)
                homeService.resetTo(path)
            }

            override fun more(subItem: SubItem) {
                NAV.go(subItem.helpUrl)
            }
        })
    }

    private fun loadForType(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ) {
        val homeCard = Map("首页").let {
            val card = TypeCard(it)
            MapSubItems().forEach {
                it.uuid = TimeCatOnline.tab2Url(TimeCatOnline.PATH_home, GLOBAL_OnlineHomeService, it.uuid)
                val subCard = mapSubItem2Card(context, homeService, it)
                card.addSubItem(subCard)
            }
            card
        }
        val momentCard = Map("动态").let {
            val card = TypeCard(it)
            MapSubItems().forEach {
                it.uuid = TimeCatOnline.tab2Url(TimeCatOnline.PATH_moment, GLOBAL_OnlineMomentService, it.uuid)
                val subCard = mapSubItem2Card(context, homeService, it)
                card.addSubItem(subCard)
            }
            card
        }
        val allTypeCard = listOf(homeCard, momentCard)
        callback.onVirtualLoadSuccess(allTypeCard)
    }
}