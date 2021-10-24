package com.timecat.module.user.app.create

import com.timecat.component.router.app.NAV
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_TASK
import com.timecat.identity.data.block.type.SHOP_Basic
import com.timecat.identity.data.block.type.TASK_Data
import com.timecat.identity.data.block.type.TASK_Story
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
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_TASK])
class CreateBlockTypeService_BLOCK_TASK : CreateBlockTypeService {
    override fun type(): Int = BLOCK_TASK
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_TASK, "任务符文", "任务符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_TASK()
}

class CreateSubTypeService_BLOCK_TASK : CreateBlockSubTypeService {
    override fun subtype(): List<Int> {
        return listOf(
            TASK_Data,
            TASK_Story,
        )
    }

    override fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        return listOf(
            SubItem(BLOCK_TASK, TASK_Data, "统计数据任务", "【需登录】如：登录0/1，战斗2／5，胜利3／7", IconLoader.randomAvatar(), "任务符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_TASK, TASK_Story, "剧情任务", "【需登录】剧情任务", IconLoader.randomAvatar(), "任务符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
        )
    }

    override fun create(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(subItem, parent, listener)
    }

    override fun createInActivity(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        val path = when (subItem.subType) {
            TASK_Data -> RouterHub.USER_DataTaskEditorActivity
            TASK_Story -> RouterHub.USER_DataTaskEditorActivity
            else -> RouterHub.USER_DataTaskEditorActivity
        }
        NAV.go(path)
    }

    override fun createInDialog(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}