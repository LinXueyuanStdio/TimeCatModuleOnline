package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_ABOUT
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
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_ABOUT])
class CreateBlockTypeService_BLOCK_ABOUT : CreateBlockTypeService {
    override fun type(): Int = BLOCK_ABOUT
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_ABOUT, "公告符文", "公告符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_ABOUT()
}

class CreateSubTypeService_BLOCK_ABOUT : CreateBlockSubTypeService {
    override suspend fun subtype(): List<Int> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf()
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf(
//            SubItem(BLOCK_ABOUT, 0, "公告", "", "R.drawable.every_drawer_note", "公告符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
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
