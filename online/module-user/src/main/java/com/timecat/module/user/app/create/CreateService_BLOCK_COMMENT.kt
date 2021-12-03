package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_COMMENT
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
 * @description
 * !!!评论需要指定一个符文，必须针对某个符文才可以评论。
 * 因此不是可以挂到根目录的。所以subitem返回空，表示不允许从0创建（*）
 * @usage null
 */
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_CreateService_BLOCK_COMMENT])
class CreateService_BLOCK_COMMENT : CreateBlockTypeService {
    override fun type(): Int = BLOCK_COMMENT
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_COMMENT, "评论符文 -> ${parent?.title ?: "根目录"}", "评论符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_COMMENT()
}

class CreateSubTypeService_BLOCK_COMMENT : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLogin()) return listOf()
        return listOf()
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLogin()) return listOf()
        return listOf(
//            SubItem(BLOCK_COMMENT, 0, "评论", "【需登录】", IconLoader.randomAvatar(), "评论符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: "")
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
//        NAV.go(RouterHub.USER_AddIdentityActivity)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}