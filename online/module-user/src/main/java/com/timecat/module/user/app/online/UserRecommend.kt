package com.timecat.module.user.app.online

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
abstract class UserRecommend(
    val I: User,
    val pageSize: Int = 10,
) {
    abstract fun transform(context: Context, block: Block, homeService: HomeService): BaseItem<*>
    fun loadForVirtualPath(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ): Disposable {
        return loadFirst(context, parentUuid, homeService, callback)
    }

    var current: Disposable? = null
    private fun loadFirst(
        context: Context,
        parentUuid: String,
        homeService: HomeService,
        callback: ContainerService.LoadCallback
    ): Disposable {
        current?.dispose()
        return requestBlock {
            query = query().apply {
                setLimit(pageSize)
                setSkip(0)
                order("-createdAt")
                cachePolicy = AVQuery.CachePolicy.NETWORK_ONLY
            }
            onSuccess = {
                val items = it.map {
                    transform(context, it, homeService)
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
        }.also {
            current = it
        }
    }

    abstract fun query(): AVQuery<Block>

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
                    transform(context, it, homeService)
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