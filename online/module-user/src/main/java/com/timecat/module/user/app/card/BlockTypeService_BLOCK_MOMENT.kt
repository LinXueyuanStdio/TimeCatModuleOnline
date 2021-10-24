package com.timecat.module.user.app.card

import android.content.Context
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_MOMENT
import com.timecat.middle.block.item.BaseRecordItem
import com.timecat.middle.block.service.BlockTypeService
import com.timecat.middle.block.service.CardBuilderFactory
import com.timecat.middle.block.service.FunctionCardBuilderFactory
import com.timecat.middle.block.service.ItemCommonListener
import com.timecat.module.user.adapter.readonly.moment.MomentCard

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/24
 * @description null
 * @usage null
 */
class BlockTypeService_BLOCK_MOMENT : BlockTypeService {
    override fun forType(): Int = BLOCK_MOMENT
    override suspend fun buildFactory(): CardBuilderFactory {
        return FunctionCardBuilderFactory(::buildCard)
    }

    suspend fun buildCard(
        record: RoomRecord,
        context: Context,
        commonListener: ItemCommonListener
    ): BaseRecordItem<*> {
        return MomentCard(context, record)
    }
}