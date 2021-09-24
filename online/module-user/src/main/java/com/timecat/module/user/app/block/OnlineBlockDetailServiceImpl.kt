package com.timecat.module.user.app.block

import android.content.Context
import com.same.lib.core.ActionBarMenuItem
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlockOrNull
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.service.*
import com.timecat.module.user.R
import com.timecat.module.user.app.online.TimeCatOnline
import com.timecat.module.user.ext.toRoomRecord
import com.timecat.module.user.record.OnlineBackendDb
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
    val recordContext: RecordContext? by lazy { NAV.service(RecordContext::class.java) }

    override fun loadFor(parentPath: Path, record: RoomRecord, onParse: ParseCallback) {
        val (title, url, type) = TimeCatOnline.blockNavigate(parentPath, record)
        onParse.onParse(title, url, type)
    }

    private fun existSpace(I: User, space: Block, block: Block, path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        val remoteDb = OnlineBackendDb(context, I, space)
        homeService.loadDatabase(TimeCatOnline.space2Url(space), remoteDb)
        homeService.loadContextRecord(block.toRoomRecord())
    }

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        recordContext?.init(context)
        val I = UserDao.getCurrentUser()
        if (I == null) {
            homeService.loadContextRecord(null)
            return
        }
        TimeCatOnline.parseBlockPath(parentUuid, onSpace = { uuid ->
            requestOneBlockOrNull {
                query = oneBlockOf(uuid)
                onSuccess = {
                    val space = it
                    existSpace(I, space, it, path, context, parentUuid, homeService)
                }
                onError = {
                    homeService.loadContextRecord(null)
                }
                onEmpty = {
                    homeService.loadContextRecord(null)
                }
            }
        }, onBlock = { uuid ->
            requestOneBlockOrNull {
                query = oneBlockOf(uuid)
                onSuccess = {
                    val space = it.space
                    if (space == null) {
                        homeService.loadContextRecord(null)
                    } else {
                        existSpace(I, space, it, path, context, parentUuid, homeService)
                    }
                }
                onError = {
                    homeService.loadContextRecord(null)
                }
                onEmpty = {
                    homeService.loadContextRecord(null)
                }
            }
        }, onFail = {
            homeService.loadContextRecord(null)
        })
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        val commonListener = homeService.itemCommonListener()
        recordContext?.getMenu(path, context, parentUuid, record, homeService) {
            homeService.loadMenu(object : MenuContext by it {
                override fun configStatusMenu(view: ActionBarMenuItem) {
                    view.setIcon(R.drawable.ic_apps_white_24dp)
                }
            })

        } ?: homeService.loadMenu(EmptyMenuContext())
        recordContext?.getChipType(path, context, parentUuid, record, homeService) {
            val types = it
            homeService.loadChipType(types)
        } ?: homeService.loadChipType(listOf())

        recordContext?.getPanel(path, context, parentUuid, record, homeService) {
            val panel = it
            homeService.loadPanel(panel)
        } ?: homeService.loadPanel(EmptyPanelContext())

        recordContext?.getInputSend(path, context, parentUuid, record, homeService) {
            homeService.loadInputSend(object : InputContext by it {

            })

        } ?: homeService.loadInputSend(EmptyInputContext())


        recordContext?.getCommand(path, context, parentUuid, record, homeService) {
            homeService.loadCommand(object : CommandContext by it {

            })
        } ?: homeService.loadCommand(EmptyCommandContext())

        recordContext?.getHeader(path, context, parentUuid, record, homeService) {
            val headers = mutableListOf<BaseItem<*>>()
            headers.addAll(it)
            homeService.loadHeader(headers)
        } ?: homeService.loadHeader(listOf())

        //chip button
        recordContext?.getChipButtons(path, context, parentUuid, record, homeService) {
            homeService.loadChipButtons(it)
        } ?: homeService.loadChipButtons(listOf())

        if (record == null) {
            commonListener.setInitSortTypeAndSortAsc(0, true)
        } else {
            commonListener.setInitSortTypeAndSortAsc(record.sortType, record.sortAsc)
        }
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        val recordId = TimeCatOnline.getRecordId(parentUuid)
        recordContext?.loadForVirtualPath(context, parentUuid, recordId, homeService, callback)
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        val recordId = TimeCatOnline.getRecordId(parentUuid)
        recordContext?.loadMoreForVirtualPath(context, parentUuid, recordId, offset, homeService, callback)
    }

    var RoomRecord.sortType: Int
        get() = extension.getInteger("sortType") ?: 0
        set(value) {
            extension["sortType"] = value
        }
    var RoomRecord.sortAsc: Boolean
        get() = extension.getBoolean("sortAsc") ?: true
        set(value) {
            extension["sortAsc"] = value
        }
}
