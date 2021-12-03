package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.block.type.BLOCK_IDENTITY
import com.timecat.identity.data.block.type.BLOCK_MAIL
import com.timecat.identity.data.block.type.BLOCK_RECORD
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.readonly.UiHub
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.CreateBlockTypeService
import com.timecat.middle.block.service.ItemCommonListener
import com.xiaojinzi.component.anno.ServiceAnno

@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_IDENTITY])
class CreateService_BLOCK_IDENTITY : CreateBlockTypeService {
    override fun type(): Int = BLOCK_IDENTITY
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_IDENTITY, "方块符文 -> ${parent?.title ?: "根目录"}", "方块符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_IDENTITY()
}

class CreateSubTypeService_BLOCK_IDENTITY : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_IDENTITY)) return listOf()
        return listOf(0)
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_IDENTITY)) return listOf()
        return listOf(
            SubItem(BLOCK_IDENTITY, 0, "身份／方块", "【需登录】身份带有角色，因而有权限，可访问指定的路径", IconLoader.randomAvatar(), "方块符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        NAV.go(RouterHub.USER_AddIdentityActivity)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
