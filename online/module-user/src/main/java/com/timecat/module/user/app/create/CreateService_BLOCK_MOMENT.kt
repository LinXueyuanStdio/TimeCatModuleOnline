package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_MOMENT
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
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_MOMENT])
class CreateService_BLOCK_MOMENT : CreateBlockTypeService {
    override fun type(): Int = BLOCK_MOMENT
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_MOMENT, "动态符文 -> ${parent?.title ?: "根目录"}", "动态符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_MOMENT()
}

class CreateSubTypeService_BLOCK_MOMENT : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_MOMENT)) return listOf()
        return listOf(0)
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_MOMENT)) return listOf()
        return listOf(
            SubItem(BLOCK_MOMENT, 0, "动态", "【需登录】发布动态", IconLoader.randomAvatar(), "动态符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        NAV.go(RouterHub.USER_AddMomentActivity)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}