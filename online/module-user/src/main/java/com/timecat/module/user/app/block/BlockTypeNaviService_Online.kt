package com.timecat.module.user.app.block

import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.service.BlockTypeNaviService
import com.timecat.middle.block.service.FunctionNaviBuilderFactory
import com.timecat.middle.block.service.NaviBuilderFactory
import com.timecat.module.user.app.online.TimeCatOnline
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/11/21
 * @description null
 * @usage null
 */
@ServiceAnno(BlockTypeNaviService::class, name = [RouterHub.NAVI_BlockTypeNaviService_BLOCK_MOMENT])
class BlockTypeNaviService_Online : BlockTypeNaviService {
    override fun forType(): Int = BLOCK_MOMENT
    override suspend fun buildFactory(): NaviBuilderFactory {
        return FunctionNaviBuilderFactory { parentPath, record ->
            TimeCatOnline.blockNavigatePath(parentPath, record)
        }
    }

}