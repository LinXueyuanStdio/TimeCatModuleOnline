package com.timecat.module.user.app.create

import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_TAG
import com.timecat.identity.readonly.RouterHub
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.CreateBlockTypeService
import com.timecat.middle.block.service.ItemCommonListener
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/23
 * @description null
 * @usage null
 */
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_TAG])
class CreateBlockTypeService_BLOCK_TAG : CreateBlockTypeService {
    override fun type(): Int = BLOCK_TAG
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_TAG, "标签符文", "标签符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_TAG()
}

class CreateSubTypeService_BLOCK_TAG : CreateBlockSubTypeService {
    override fun subtype(): List<Int> {
        return listOf(0)
    }

    override fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        return listOf(
            SubItem(BLOCK_TAG, 0, "标签", "给其他符文打标签，按标签过滤", "R.drawable.every_drawer_note", "标签符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun createInActivity(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInDialog(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}