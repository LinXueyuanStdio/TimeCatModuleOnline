package com.timecat.module.user.app.online

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.service.*
import com.timecat.module.user.backend.OnlineBackend
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
@ServiceAnno(BlockTypeBackendService::class, name=[RouterHub.BACKEND_BackendService_OnlineHost])
class BlockTypeBackendServiceImpl : BlockTypeBackendService {
    override fun forType(): String = RouteSchema.OnlineHost
    override suspend fun buildFactory(): BackendBuilderFactory {
        return FunctionBackendBuilderFactory(::buildBackend)
    }
    private fun buildBackend(context: Context, type: String): IBackend {
        return OnlineBackend(context, UserDao.getCurrentUser())
    }
}