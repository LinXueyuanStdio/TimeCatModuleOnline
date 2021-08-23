package com.timecat.module.user.app.online

import android.content.Context
import cn.leancloud.AVQuery
import com.google.android.material.chip.Chip
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.configAdapterEndlessLoad
import com.timecat.middle.block.service.*
import com.timecat.module.user.adapter.block.MomentItem
import com.timecat.module.user.adapter.block.NotMoreItem
import com.timecat.module.user.ext.GLOBAL_OnlineHomeRecommendService
import com.xiaojinzi.component.anno.ServiceAnno
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/16
 * @description world://timecat.online/{pathSeg}?redirect={redirectService}&tab={tab}
 * pathSeg = home
 * redirectService = GLOBAL_OnlineMomentHotService
 * tab = ?
 * @usage 根据推荐
 * 目前数据量比较少，先根据更新时间排序
 */
@ServiceAnno(ContainerService::class, name = [GLOBAL_OnlineHomeRecommendService])
class OnlineHomeRecommendServiceImpl : ContainerService {
    val pageSize: Int = 10
    private val notMoreItem: NotMoreItem = NotMoreItem()

    fun I(): User {
        return UserDao.getCurrentUser() ?: throw Exception()
    }

    override fun loadContextRecord(path: Path, context: Context, parentUuid: String, homeService: HomeService) {
        homeService.loadContextRecord(null)
    }

    var focus_ids: List<User> = mutableListOf()
    var disposable: Disposable? = null
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
        homeService.loadChipType(listOf())
        homeService.loadPanel(EmptyPanelContext())
        homeService.loadChipButtons(listOf(Chip(context).apply {
            text = "进入社区"
            setShakelessClickListener {
                NAV.go(RouterHub.USER_CloudActivity)
            }
        }))
        homeService.loadCommand(EmptyCommandContext())
        homeService.loadInputSend(EmptyInputContext())
        homeService.reloadData()
    }

    override fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ) {

        if (UserDao.getCurrentUser() == null) {
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
            disposable = requestUserRelation {
                query = I().allFollow()
                onError = {
                    callback.onError("加载失败") {
                        homeService.reloadData()
                    }
                    focus_ids = mutableListOf()
                    LogUtil.se(it)
                }
                onEmpty = {
                    focus_ids = mutableListOf()
                    loadFirst(context, parentUuid, homeService, callback)
                }
                onSuccess = {
                    focus_ids = it.map { it.target }
                    loadFirst(context, parentUuid, homeService, callback)
                }
            }
        }
    }

    var current: Disposable? = null
    fun loadFirst(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ) {
        current?.dispose()
        current = requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(0)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onSuccess = {
                val items = it.map {
                    MomentItem(context, it)
                }
                callback.onVirtualLoadSuccess(items)
            }
            onEmpty = {
                callback.onEmpty("空") {
                    loadFirst(context, parentUuid, homeService, callback)
                }
            }
            onError = {
                callback.onError(it.localizedMessage ?: "") {
                    loadFirst(context, parentUuid, homeService, callback)
                }
            }
        }
    }

    fun query(): AVQuery<Block> {
        // 合并两个条件，进行"或"查询
        // 查询 我关注的人的动态 和 自己的动态
        val queries: MutableList<AVQuery<Block>> = ArrayList()
        for (user in focus_ids) {
            queries.add(user.findAllMoment())
        }
        queries.add(I().findAllMoment())
        return AVQuery.or(queries)
            .include("user")
            .include("parent")
            .order("-createdAt")
    }

    override fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
        requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(offset)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onSuccess = {
                val items = it.map {
                    MomentItem(context, it)
                }
                callback.onVirtualLoadSuccess(items)
            }
            onEmpty = {
                callback.onEmpty("空")
            }
            onError = {
                callback.onError(it.localizedMessage ?: "")
            }
        }
    }
}