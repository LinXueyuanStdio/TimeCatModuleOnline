package com.timecat.module.user.app.create

import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.block.type.BLOCK_MAIL
import com.timecat.identity.data.block.type.BLOCK_RECORD
import com.timecat.identity.data.block.type.BLOCK_ROLE
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.CreateBlockTypeService
import com.timecat.middle.block.service.ItemCommonListener
import com.xiaojinzi.component.anno.ServiceAnno

@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_ROLE])
class CreateBlockTypeService_BLOCK_ROLE : CreateBlockTypeService {
    override fun type(): Int = BLOCK_ROLE
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_ROLE, "Markdown", "Markdown 符文。", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_ROLE()
}

class CreateSubTypeService_BLOCK_ROLE : CreateBlockSubTypeService {
    override fun subtype(): List<Int> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf(0)
    }

    override fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf(
            SubItem(BLOCK_MAIL, 0, "邮件", "【需登录】邮件可附上其他符文，接收者可领取物品", IconLoader.randomAvatar(), "邮件符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun createInActivity(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInDialog(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
