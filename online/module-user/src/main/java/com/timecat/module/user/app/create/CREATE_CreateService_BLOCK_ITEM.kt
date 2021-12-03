package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.readonly.UiHub
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.CreateBlockTypeService
import com.timecat.middle.block.service.ItemCommonListener
import com.xiaojinzi.component.anno.ServiceAnno

@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_ITEM])
class CREATE_CreateService_BLOCK_ITEM : CreateBlockTypeService {
    override fun type(): Int = BLOCK_ITEM
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_ITEM, "物品符文 -> ${parent?.title ?: "根目录"}", "物品符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_ITEM()
}

class CreateSubTypeService_BLOCK_ITEM : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_ITEM)) return listOf()
        return listOf(
            ITEM_Thing,
            ITEM_Package,
            ITEM_Data,
            ITEM_Equip,
            ITEM_Buff,
            ITEM_Cube,
        )
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_ITEM)) return listOf()
        return listOf(
            SubItem(BLOCK_ITEM, ITEM_Thing, "物产", "【需登录】物产，用于合成。如碎片，如装备碎片、方块碎片；如突破材料", IconLoader.randomAvatar(), "物品符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ITEM, ITEM_Package, "礼包", "【需登录】礼包，指定数量个物品的组合", IconLoader.randomAvatar(), "物品符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ITEM, ITEM_Data, "数值", "【需登录】数值，一个字段的加或减，如用户经验值、用户体力、方块经验值、方块突破等级", IconLoader.randomAvatar(), "物品符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ITEM, ITEM_Equip, "装备", "【需登录】装备，给角色的数值加或减", IconLoader.randomAvatar(), "物品符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ITEM, ITEM_Buff, "药剂", "【需登录】药剂，有时效性，增益效果，状态效果", IconLoader.randomAvatar(), "物品符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ITEM, ITEM_Cube, "方块", "【需登录】方块。使用后获得一个方块", IconLoader.randomAvatar(), "物品符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        val path = when (subItem.subType) {
            ITEM_Thing -> RouterHub.USER_ThingItemEditorActivity
            ITEM_Package -> RouterHub.USER_PackageItemEditorActivity
            ITEM_Data -> RouterHub.USER_DataItemEditorActivity
            ITEM_Equip -> RouterHub.USER_EquipItemEditorActivity
            ITEM_Buff -> RouterHub.USER_BuffItemEditorActivity
            ITEM_Cube -> RouterHub.USER_CubeItemEditorActivity
            else -> RouterHub.USER_ThingItemEditorActivity
        }
        NAV.go(path)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
