package com.timecat.module.user.app

import android.content.Context
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.service.ContainerService
import com.timecat.middle.block.service.HomeService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/16
 * @description null
 * @usage null
 */
@ServiceAnno(ContainerService::class, name = arrayOf(RouterHub.GLOBAL_OnlineContainerService))
class OnlineContainerServiceImpl : ContainerService {
    override fun loadForVirtualPath(context: Context,
                                    parentUuid: String,
                                    homeService: HomeService,
                                    callback: ContainerService.LoadCallback) {
        TODO("Not yet implemented")
    }
}