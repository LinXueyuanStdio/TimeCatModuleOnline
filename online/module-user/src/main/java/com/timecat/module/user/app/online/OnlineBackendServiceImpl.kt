package com.timecat.module.user.app.online

import android.content.Context
import com.timecat.middle.block.service.IDatabase
import com.timecat.middle.block.service.OnlineBackendService
import com.timecat.module.user.record.EmptyDatabase
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
@ServiceAnno(OnlineBackendService::class)
class OnlineBackendServiceImpl : OnlineBackendService {
    override fun buildBackend(context: Context, url: String): IDatabase {
        return EmptyDatabase()
    }
}