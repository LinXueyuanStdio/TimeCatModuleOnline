package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.block.type.BLOCK_DIALOG
import com.timecat.identity.data.block.type.BLOCK_MAIL
import com.timecat.identity.data.block.type.BLOCK_RECORD
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
 * @description null
 * @usage null
 */
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_DIALOG])
class CreateBlockTypeService_BLOCK_DIALOG : CreateBlockTypeService {
    override fun type(): Int = BLOCK_DIALOG
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_DIALOG, "Markdown -> ${parent?.title ?: "根目录"}", "Markdown 符文。", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_DIALOG()
}

class CreateSubTypeService_BLOCK_DIALOG : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLogin()) return listOf()
        return listOf(0)
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLogin()) return listOf()
        return listOf(
            SubItem(BLOCK_MAIL, 0, "邮件", "【需登录】邮件可附上其他符文，接收者可领取物品", IconLoader.randomAvatar(), "邮件符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
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