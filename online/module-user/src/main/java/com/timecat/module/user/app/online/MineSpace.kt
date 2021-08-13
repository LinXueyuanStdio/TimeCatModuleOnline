package com.timecat.module.user.app.online

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import com.timecat.module.user.adapter.block.MomentItem
import com.timecat.module.user.ext.findAllSpace
import com.timecat.module.user.ext.icon
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
class MineSpace(
    val I: User,
    val pageSize: Int = 10,
) {
    var current: Disposable? = null
    fun loadForVirtualPath(context: Context,
                           parentUuid: String,
                           homeService: HomeService,
                           callback: ContainerService.LoadCallback): Disposable {
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
                    val item = SubItem(it.type, it.subtype, it.title, it.content, it.icon, "", RouterHub.ABOUT_HelpActivity, uuid = TimeCatOnline.toUrl(it))
                    val subCard = mapSubItem2Card(context, homeService, item)
                    subCard
                }
                callback.onVirtualLoadSuccess(items)
            }
            onEmpty = {
                callback.onEmpty("空") {
                    loadForVirtualPath(context, parentUuid, homeService, callback)
                }
            }
            onError = {
                callback.onError(it.localizedMessage ?: "") {
                    loadForVirtualPath(context, parentUuid, homeService, callback)
                }
            }
        }.also {
            current = it
        }
    }

    private fun query(): AVQuery<Block> {
        return I.findAllSpace()
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
                    val item = SubItem(it.type, it.subtype, it.title, it.content, it.icon, "", RouterHub.ABOUT_HelpActivity, uuid = TimeCatOnline.toUrl(it))
                    val subCard = mapSubItem2Card(context, homeService, item)
                    subCard
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