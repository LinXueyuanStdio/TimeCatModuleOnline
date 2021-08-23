package com.timecat.module.user.app.online

import android.content.Context
import com.timecat.data.bmob.data.User
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.configAdapterEndlessLoad
import com.timecat.middle.block.service.*
import com.timecat.module.user.adapter.block.NotMoreItem
import com.xiaojinzi.component.anno.ServiceAnno
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/6
 * @description null
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = [RouterHub.GLOBAL_OfficialOnlineTocService])
class OfficialOnlineTocServiceImpl : ContainerService {

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
//        homeService.loadDatabase()
        homeService.loadContextRecord(null)
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        homeService.loadMenu(EmptyMenuContext())
        homeService.loadChipType(listOf())
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.loadCommand(EmptyCommandContext())
        homeService.loadHeader(listOf())
        homeService.loadChipButtons(listOf())
        homeService.reloadData()
    }

    private val pageSize: Int = 10
    private val notMoreItem: NotMoreItem = NotMoreItem()

    var disposable: Disposable? = null
    var dataloader: UserSpace? = null
    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        val listener = homeService.itemCommonListener()
        configAdapterEndlessLoad(listener.adapter(), false, pageSize, 4, notMoreItem) { lastPosition: Int, currentPage: Int ->
            listener.loadMore(lastPosition, currentPage)
        }
        callback.onLoading("正在连接服务器") {
            disposable?.dispose()
            callback.onVirtualLoadSuccess(listOf())
        }
        val officialVirtualUser = User()
        officialVirtualUser.objectId = "6123a628ed028f2ed88d0737"
        dataloader = UserSpace(officialVirtualUser, pageSize)
        disposable = dataloader?.loadForVirtualPath(context, parentUuid, homeService, callback)
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        dataloader?.loadMoreForVirtualPath(context, parentUuid, offset, homeService, callback)
    }

}
