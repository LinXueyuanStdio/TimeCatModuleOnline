package com.timecat.module.user.app.block

import android.content.Context
import com.same.lib.core.ActionBarMenuItem
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.service.*
import com.timecat.module.user.R
import com.timecat.module.user.app.online.TimeCatOnline
import com.xiaojinzi.component.anno.ServiceAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

//    private fun existSpace(I: User, space: Block, block: Block, path: Path, context: Context, parentUuid: String, homeService: HomeService) {
//        val remoteDb = OnlineBackendDb(context, I, space)
//        homeService.loadDatabase(TimeCatOnline.space2Url(space), remoteDb)
//        homeService.loadContextRecord(block.toRoomRecord())
//    }

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        context.launch(Dispatchers.IO) {
            recordContext?.init(context, homeService.getPermission(), homeService)
            loadContextRecordByDefault(path, context, parentUuid, homeService)
//            val I = UserDao.getCurrentUser()
//            if (I == null) {
//                homeService.loadContextRecord(null)
//                return@launch
//            }
////            val (spaceId, recordId) = TimeCatOnline.parsePath(parentUuid)
////            if (recordId != null) {
////
////            }
//            TimeCatOnline.parseBlockPath(parentUuid, onSpace = { uuid ->
//                requestOneBlockOrNull {
//                    query = oneBlockOf(uuid)
//                    onSuccess = {
//                        val space = it
//                        existSpace(I, space, it, path, context, parentUuid, homeService)
//                    }
//                    onError = {
//                        homeService.loadContextRecord(null)
//                    }
//                    onEmpty = {
//                        homeService.loadContextRecord(null)
//                    }
//                }
//            }, onBlock = { uuid ->
//                requestOneBlockOrNull {
//                    query = oneBlockOf(uuid)
//                    onSuccess = {
//                        val space = it.space
//                        if (space == null) {
//                            homeService.loadContextRecord(null)
//                        } else {
//                            existSpace(I, space, it, path, context, parentUuid, homeService)
//                        }
//                    }
//                    onError = {
//                        homeService.loadContextRecord(null)
//                    }
//                    onEmpty = {
//                        homeService.loadContextRecord(null)
//                    }
//                }
//            }, onFail = {
//                homeService.loadContextRecord(null)
//            })
        }
    }

    override fun loadContext(path: Path, context: Context, parentUuid: String, record: RoomRecord?, homeService: HomeService) {
        context.launch(Dispatchers.IO) {
            val menuContext = recordContext?.getMenu(path, context, parentUuid, record, homeService) ?: EmptyMenuContext()
            val headers = recordContext?.getHeader(path, context, parentUuid, record, homeService) ?: listOf()
            val inputContext = recordContext?.getInputSend(path, context, parentUuid, record, homeService) ?: EmptyInputContext()
            val commandContext = recordContext?.getCommand(path, context, parentUuid, record, homeService) ?: EmptyCommandContext()
            val types = recordContext?.getChipType(path, context, parentUuid, record, homeService) ?: listOf()
            val panel = recordContext?.getPanel(path, context, parentUuid, record, homeService) ?: EmptyPanelContext()

            val menuContext2 = object : MenuContext by menuContext {
                override fun configStatusMenu(view: ActionBarMenuItem) {
                    view.setIcon(R.drawable.ic_apps_white_24dp)
                }
            }

            val buttons = recordContext?.getChipButtons(path, context, parentUuid, record, homeService) ?: listOf()

            withContext(Dispatchers.Main) {
                homeService.loadMenu(menuContext2)
                homeService.loadHeader(headers)
                homeService.loadInputSend(inputContext)
                homeService.loadCommand(commandContext)
                homeService.loadChipType(types)
                homeService.loadPanel(panel)
                homeService.loadChipButtons(buttons)

                //只能手动排序
                if (record == null) {
                    recordContext?.setInitSortTypeAndSortAsc(homeService, 0, true)
                } else {
                    recordContext?.setInitSortTypeAndSortAsc(homeService, record.sortType, record.sortAsc)
                }
            }
        }
    }

    override fun loadForVirtualPath(context: Context, parentUuid: String, homeService: HomeService, callback: ContainerService.LoadCallback) {
        val recordId = DNS.getRecordId(parentUuid)
        recordContext?.loadForVirtualPath(context, parentUuid, recordId, homeService, callback)
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        val recordId = DNS.getRecordId(parentUuid)
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
