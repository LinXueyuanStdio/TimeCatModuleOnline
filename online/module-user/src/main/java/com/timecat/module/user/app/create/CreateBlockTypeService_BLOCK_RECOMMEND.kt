package com.timecat.module.user.app.create

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.NOTE
import com.timecat.identity.data.block.type.BLOCK_LEADER_BOARD
import com.timecat.identity.data.block.type.BLOCK_MAIL
import com.timecat.identity.data.block.type.BLOCK_RECOMMEND
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
 * @description 推荐
 * !!!推荐需要指定一个排行榜，必须针对某个排行榜才可以推荐。
 * 因此不是可以挂到根目录的。所以subitem返回空，表示不允许从0创建（*）
 * @usage null
 */
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_RECOMMEND])
class CreateBlockTypeService_BLOCK_RECOMMEND : CreateBlockTypeService {
    override fun type(): Int = BLOCK_RECOMMEND
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_RECOMMEND, "推荐符文", "推荐符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_RECOMMEND()
}

class CreateSubTypeService_BLOCK_RECOMMEND : CreateBlockSubTypeService {
    override fun subtype(): List<Int> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf()
    }

    override fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (UserDao.getCurrentUser() == null) return listOf()
        return listOf(
//            SubItem(BLOCK_RECOMMEND, 0, "推荐入榜", "【需登录】", IconLoader.randomAvatar(), "推荐符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(subItem, parent, listener)
    }

    override fun createInActivity(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
//        NAV.go(RouterHub.USER_AddRecommendActivity)
    }

    override fun createInDialog(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}