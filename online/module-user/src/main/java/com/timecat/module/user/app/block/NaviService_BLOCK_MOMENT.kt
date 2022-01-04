package com.timecat.module.user.app.block

import android.net.Uri
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.prettyTitle
import com.timecat.middle.block.service.BlockTypeNaviService
import com.timecat.middle.block.service.DNS
import com.timecat.middle.block.service.FunctionNaviBuilderFactory
import com.timecat.middle.block.service.NaviBuilderFactory
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/11/21
 * @description null
 * @usage null
 */
@ServiceAnno(BlockTypeNaviService::class, name = [RouterHub.NAVI_NaviService_BLOCK_MOMENT])
class NaviService_BLOCK_MOMENT : BlockTypeNaviService {
    override fun forType(): Int = BLOCK_MOMENT
    override suspend fun buildFactory(): NaviBuilderFactory {
        return FunctionNaviBuilderFactory { context, parentPath, record ->
            val uri = Uri.parse(parentPath.uuid)
            val spaceId = DNS.getSpaceId(uri)
            val redirectUrl = RouterHub.GLOBAL_BlockDetailService
            val url = DNS.buildUri(uri.authority!!, spaceId, record.uuid, redirectUrl)
                .build().toString()
            Path(record.prettyTitle, url, DNS.type)
        }
    }

}