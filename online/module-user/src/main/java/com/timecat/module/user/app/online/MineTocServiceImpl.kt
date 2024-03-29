package com.timecat.module.user.app.online

import android.content.Context
import android.content.res.ColorStateList
import com.google.android.material.chip.Chip
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.configAdapterEndlessLoad
import com.timecat.middle.block.service.*
import com.timecat.module.user.R
import com.timecat.module.user.adapter.block.NotMoreItem
import com.timecat.module.user.app.showCreateOnlineSpaceDialog
import com.xiaojinzi.component.anno.ServiceAnno
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/6
 * @description null
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = [RouterHub.GLOBAL_MineTocService])
class MineTocServiceImpl : ContainerService {

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
//        homeService.loadDatabase()
        homeService.loadContextRecord(null)
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            homeService.loadMenu(EmptyMenuContext())
            homeService.loadChipType(listOf())
            homeService.loadPanel(EmptyPanelContext())
            homeService.loadInputSend(EmptyInputContext())
            homeService.loadCommand(EmptyCommandContext())
            homeService.loadHeader(listOf())
            homeService.loadChipButtons(listOf())
            homeService.reloadData()
            return
        }
        val listener = homeService.itemCommonListener()
        homeService.loadMenu(EmptyMenuContext())
        homeService.loadChipType(listOf())
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.loadCommand(EmptyCommandContext())
        homeService.loadHeader(listOf())
        homeService.loadChipButtons(listOf(Chip(context).apply {
            text = "创建超空间"
            setChipIconResource(R.drawable.ic_add)
            chipIconTint = ColorStateList.valueOf(Attr.getIconColor(context))
            setShakelessClickListener {
                showCreateOnlineSpaceDialog(context) { block: Block? ->
                    homeService.reloadData()
                }
            }
        }))
        homeService.reloadData()
    }

    private val pageSize: Int = 10
    private val notMoreItem: NotMoreItem = NotMoreItem()

    var disposable: Disposable? = null
    var dataloader: UserSpace? = null
    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
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
            dataloader = UserSpace(I, pageSize)
            disposable = dataloader?.loadForVirtualPath(context, parentUuid, homeService, callback)
        }
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        dataloader?.loadMoreForVirtualPath(context, parentUuid, offset, homeService, callback)
    }

}
