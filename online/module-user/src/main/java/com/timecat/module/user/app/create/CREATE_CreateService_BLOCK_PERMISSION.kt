package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_PERMISSION
import com.timecat.identity.data.block.type.PERMISSION_Hun
import com.timecat.identity.data.block.type.PERMISSION_Meta
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.readonly.UiHub
import com.timecat.layout.ui.utils.IconLoader
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
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_PERMISSION])
class CREATE_CreateService_BLOCK_PERMISSION : CreateBlockTypeService {
    override fun type(): Int = BLOCK_PERMISSION
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_PERMISSION, "权限符文 -> ${parent?.title ?: "根目录"}", "权限符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_PERMISSION()
}

class CreateSubTypeService_BLOCK_PERMISSION : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_PERMISSION)) return listOf()
        return listOf(
            PERMISSION_Meta,
            PERMISSION_Hun,
        )
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_PERMISSION)) return listOf()
        return listOf(
            SubItem(BLOCK_PERMISSION, PERMISSION_Meta, "元权限", "【需登录】一个权限", IconLoader.randomAvatar(), "权限符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_PERMISSION, PERMISSION_Hun, "混权限", "【需登录】正则表达式匹配多个权限", IconLoader.randomAvatar(), "权限符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        val path = when (subItem.subType) {
            PERMISSION_Meta -> RouterHub.USER_AddMetaPermissionActivity
            PERMISSION_Hun -> RouterHub.USER_AddHunPermissionActivity
            else -> RouterHub.USER_AddMetaPermissionActivity
        }
        NAV.go(path)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}