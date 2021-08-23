package com.timecat.module.user.app.online

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.requestUserRelation
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import com.timecat.module.user.adapter.block.MomentItem
import io.reactivex.disposables.Disposable
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
class UserMomentFocus(
    val I: User,
    val pageSize: Int = 10,
    var focus_ids: List<User> = mutableListOf()
) {
    fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ): Disposable {
        return requestUserRelation {
            query = I.allFollow()
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

    var current: Disposable? = null
    private fun loadFirst(
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

    private fun query(): AVQuery<Block> {
        // 合并两个条件，进行"或"查询
        // 查询 我关注的人的动态 和 自己的动态
        val queries: MutableList<AVQuery<Block>> = ArrayList()
        for (user in focus_ids) {
            queries.add(user.findAllMoment())
        }
        queries.add(I.findAllMoment())
        return AVQuery.or(queries)
            .include("user")
            .include("parent")
            .order("-createdAt")
    }

    fun loadMoreForVirtualPath(context: Context, parentUuid: String, offset: Int, homeService: HomeService, callback: ContainerService.LoadMoreCallback) {
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