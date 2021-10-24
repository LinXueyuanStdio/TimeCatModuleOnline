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

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/23
 * @description null
 * @usage null
 */
@ServiceAnno(CreateBlockTypeService::class, name = [RouterHub.CREATE_FACTORY_MainCreateBlockTypeService_BLOCK_ACTIVITY])
class CreateBlockTypeService_BLOCK_ACTIVITY : CreateBlockTypeService {
    override fun type(): Int = BLOCK_ACTIVITY
    override fun typeItem(parent: RoomRecord?): TypeItem = TypeItem(BLOCK_ACTIVITY, "活动符文 -> ${parent?.title ?: "根目录"}", "活动符文", true)
    override suspend fun buildFactory(): CreateBlockSubTypeService = CreateSubTypeService_BLOCK_ACTIVITY()
}

class CreateSubTypeService_BLOCK_ACTIVITY : BaseCreateSubTypeService() {
    override suspend fun subtype(): List<Int> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_ACTIVITY)) return listOf()
        return listOf(
            ACTIVITY_Url,
            ACTIVITY_Text_url,
            ACTIVITY_Custom,
            ACTIVITY_Dream,
            ACTIVITY_Double,
            ACTIVITY_Card,
            ACTIVITY_Price,
            ACTIVITY_Life,
            ACTIVITY_Achievement,
            ACTIVITY_Get_back,
            ACTIVITY_Seven_day_sign,
            ACTIVITY_Everyday_main,
            ACTIVITY_One_task,
        )
    }

    override suspend fun subItems(parent: RoomRecord?, listener: ItemCommonListener): List<SubItem> {
        if (checkNotLoginOrNotPermission(UiHub.MASTER_MainActivity_create_block_BLOCK_ACTIVITY)) return listOf()
        return listOf(
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Url, "外链", "【需登录】外链", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Text_url, "公告", "【需登录】公告", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Custom, "自定义", "【需登录】玩家自定义活动和任务", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Dream, "梦境", "【需登录】梦境，高价值目标，时间较长", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Double, "双线", "【需登录】双线，月任务，时间较长", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Card, "卡池", "【需登录】卡池", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Price, "悬赏", "【需登录】悬赏，由第三方或官方虚拟发起", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Life, "主线", "【需登录】成长、生涯、主线剧情", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Achievement, "成就", "【需登录】成就", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Get_back, "回归", "【需登录】回归", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Seven_day_sign, "7日签到", "【需登录】7日签到", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_Everyday_main, "每日", "【需登录】每日系列任务", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
            SubItem(BLOCK_ACTIVITY, ACTIVITY_One_task, "一期一会", "【需登录】一张图片，一个任务", IconLoader.randomAvatar(), "活动符文", RouterHub.ABOUT_HelpActivity, parent?.uuid ?: ""),
        )
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }

    override fun createInActivity(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        val path = when (subItem.subType) {
            ACTIVITY_Url -> RouterHub.USER_UrlActivityEditorActivity
            ACTIVITY_Text_url -> RouterHub.USER_TextUrlActivityEditorActivity
            ACTIVITY_Dream -> RouterHub.USER_DreamActivityEditorActivity
            ACTIVITY_Double -> RouterHub.USER_DoubleActivityEditorActivity
            ACTIVITY_Card -> RouterHub.USER_CardActivityEditorActivity
            ACTIVITY_Price -> RouterHub.USER_PriceActivityEditorActivity
            ACTIVITY_Life -> RouterHub.USER_LifeActivityEditorActivity
            ACTIVITY_Achievement -> RouterHub.USER_AchievementActivityEditorActivity
            ACTIVITY_Get_back -> RouterHub.USER_GetBackActivityEditorActivity
            ACTIVITY_Seven_day_sign -> RouterHub.USER_SevenDaySignActivityEditorActivity
            ACTIVITY_Everyday_main -> RouterHub.USER_EveryDayMainActivityEditorActivity
            ACTIVITY_One_task -> RouterHub.USER_OneTaskActivityEditorActivity
            else -> RouterHub.USER_UrlActivityEditorActivity
        }
        NAV.go(path)
    }

    override fun createInDialog(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }

    override fun createInPage(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
    }
}
