package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_SPACE
import com.timecat.identity.readonly.RouterHub
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
 * @description 在线超空间
 * @usage null
 */
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_SPACE])
class CreateBlockTypeService_BLOCK_SPACE : CreateBlockTypeService {
    override fun type(): Int = BLOCK_SPACE
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_SPACE, "空间符文", "空间符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_SPACE()
}

class CreateSubTypeService_BLOCK_SPACE : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLogin()) return listOf()
        return listOf(0)
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLogin()) return listOf()
        return listOf(
            SubItem(BLOCK_SPACE, 0, "超空间", "【需登录】在线超空间", IconLoader.randomAvatar(), "空间符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }
    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {

    }
}
