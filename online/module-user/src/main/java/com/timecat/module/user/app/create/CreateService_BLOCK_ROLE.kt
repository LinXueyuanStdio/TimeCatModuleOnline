package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_ROLE
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.readonly.UiHub
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.CreateBlockTypeService
import com.timecat.middle.block.service.ItemCommonListener
import com.xiaojinzi.component.anno.ServiceAnno

@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_ROLE])
class CreateService_BLOCK_ROLE : CreateBlockTypeService {
    override fun type(): Int = BLOCK_ROLE
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_ROLE, "角色符文 -> ${parent?.title ?: "根目录"}", "角色符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_ROLE()
}

class CreateSubTypeService_BLOCK_ROLE : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_ROLE)) return listOf()
        return listOf(0)
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_ROLE)) return listOf()
        return listOf(
            SubItem(BLOCK_ROLE, 0, "角色", "【需登录】权限角色，批量管理权限", IconLoader.randomAvatar(), "角色符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        NAV.go(RouterHub.USER_AddRoleActivity)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
