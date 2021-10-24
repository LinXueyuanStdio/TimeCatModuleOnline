package com.timecat.module.user.app.create

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.block.type.*
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
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_LEADER_BOARD])
class CreateBlockTypeService_BLOCK_LEADER_BOARD : CreateBlockTypeService {
    override fun type(): Int = BLOCK_LEADER_BOARD
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_LEADER_BOARD, "排名符文", "排名符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_LEADER_BOARD()
}

class CreateSubTypeService_BLOCK_LEADER_BOARD : CreateBlockSubTypeService {
    override fun subtype(): List<Int> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf(0)
    }

    override fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf(
            SubItem(BLOCK_LEADER_BOARD, 0, "排行榜", "【需登录】发布一个排行榜", IconLoader.randomAvatar(), "排名符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(subItem, parent, listener)
    }

    override fun createInActivity(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        NAV.go(RouterHub.USER_AddLeaderBoardActivity)
    }

    override fun createInDialog(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}