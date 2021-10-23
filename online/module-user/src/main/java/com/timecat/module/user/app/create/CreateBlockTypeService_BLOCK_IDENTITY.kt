package com.timecat.module.user.app.create

import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.block.type.BLOCK_IDENTITY
import com.timecat.identity.data.block.type.BLOCK_RECORD
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.CreateBlockTypeService
import com.timecat.middle.block.service.ItemCommonListener
import com.xiaojinzi.component.anno.ServiceAnno

@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_IDENTITY])
class CreateBlockTypeService_BLOCK_IDENTITY : CreateBlockTypeService {
    override fun type(): Int = BLOCK_IDENTITY
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_IDENTITY, "Markdown", "Markdown 符文。", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_IDENTITY()
}

class CreateSubTypeService_BLOCK_IDENTITY : CreateBlockSubTypeService {
    override fun subtype(): List<Int> {
        return listOf(NOTE)
    }

    override fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        return listOf(
            SubItem(BLOCK_RECORD, NOTE, "时间笔记", "笔记+时间 = 笔记+提醒+习惯+目标+...", "R.drawable.every_drawer_note", "记录符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun createInActivity(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInDialog(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
