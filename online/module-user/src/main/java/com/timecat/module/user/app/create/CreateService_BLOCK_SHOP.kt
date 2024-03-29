package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_SHOP
import com.timecat.identity.data.block.type.SHOP_Basic
import com.timecat.identity.data.block.type.SHOP_User_Basic
import com.timecat.identity.data.block.type.SHOP_User_Rend
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
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_SHOP])
class CreateService_BLOCK_SHOP : CreateBlockTypeService {
    override fun type(): Int = BLOCK_SHOP
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_SHOP, "商店符文 -> ${parent?.title ?: "根目录"}", "商店符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_SHOP()
}

class CreateSubTypeService_BLOCK_SHOP : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_SHOP)) return listOf()
        return listOf(
            SHOP_Basic,
            SHOP_User_Basic,
            SHOP_User_Rend,
        )
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_SHOP)) return listOf()
        return listOf(
            SubItem(BLOCK_SHOP, SHOP_Basic, "官方物品商店", "【需登录】基本版，游戏化的永久商店，用于提供基本的物质交换", IconLoader.randomAvatar(), "商店符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_SHOP, SHOP_User_Basic, "用户自己开店", "【需登录】用户自己开店，卖游戏数值", IconLoader.randomAvatar(), "商店符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_SHOP, SHOP_User_Rend, "用户自己开店", "【需登录】用户自己开店，出租头像、出租他们的造物", IconLoader.randomAvatar(), "商店符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        val path = when (subItem.subType) {
            SHOP_Basic -> RouterHub.USER_ShopEditorActivity
            else -> RouterHub.USER_ShopEditorActivity
        }
        NAV.go(path)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
