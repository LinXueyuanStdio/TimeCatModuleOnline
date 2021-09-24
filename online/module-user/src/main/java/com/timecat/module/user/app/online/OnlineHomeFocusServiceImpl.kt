package com.timecat.module.user.app.online

import android.content.Context
import com.google.android.material.chip.Chip
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.configAdapterEndlessLoad
import com.timecat.middle.block.service.*
import com.timecat.module.user.adapter.block.NotMoreItem
import com.timecat.module.user.ext.GLOBAL_OnlineHomeFocusService
import com.xiaojinzi.component.anno.ServiceAnno
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/6
 * @description world://timecat.online/{pathSeg}?redirect={redirectService}&tab={tab}
 * pathSeg = moment
 * redirectService = GLOBAL_OnlineHomeFocusService
 * tab = ?
 * @usage 根据关注列表
 */
@ServiceAnno(ContainerService::class, name = [GLOBAL_OnlineHomeFocusService])
class OnlineHomeFocusServiceImpl : ContainerService {
    private val pageSize: Int = 10
    private val notMoreItem: NotMoreItem = NotMoreItem()

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
//        homeService.loadDatabase()
        homeService.loadContextRecord(null)
    }

    var disposable: Disposable? = null
    var dataloader: UserHomeFocus? = null
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
        homeService.loadChipButtons(listOf(Chip(context).apply {
            text = "进入社区"
            setShakelessClickListener {
                NAV.go(RouterHub.USER_CloudActivity)
            }
        }))
        homeService.loadChipType(listOf())
        homeService.reloadData()
    }

    override fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ) {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            callback.onError("请登录") {
                NAV.go(RouterHub.LOGIN_LoginActivity)
            }
        } else {
            val listener = homeService.itemCommonListener()
            configAdapterEndlessLoad(listener.adapter(), false, pageSize, 4, notMoreItem) { lastPosition: Int, currentPage: Int ->
                listener.loadMore(lastPosition, currentPage)
            }
            callback.onLoading("正在连接服务器") {
                disposable?.dispose()
                callback.onVirtualLoadSuccess(listOf())
            }
            dataloader = UserHomeFocus(I, pageSize)
            disposable = dataloader?.loadForVirtualPath(context, parentUuid, homeService, callback)
        }
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        dataloader?.loadMoreForVirtualPath(context, parentUuid, offset, homeService, callback)
    }

}
