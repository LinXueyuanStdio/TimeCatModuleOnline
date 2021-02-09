package com.timecat.module.user.game.task.channal

import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.standard.navi.TabBlockItem
import com.timecat.module.user.social.cloud.channel.TabChannel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/8
 * @description null
 * @usage null
 */
enum class TaskChannel(
    val id: Long,
    val category: String,
    var title: String,
    var imagePath: String,
    var fragmentRouterPath: String,
    var actionRouterPath: String
) {
    //1.主要活动
    Home(
        0x00000001,
        "主要活动", "活动", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityMainFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Custom(
        0x00000002,
        "主要活动", "自定义", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityCustomFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Double(
        0x00000004,
        "主要活动", "大道", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityDoubleFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Dream(
        0x00000008,
        "主要活动", "梦境", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityDreamFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Card(
        0x00000010,
        "主要活动", "流星", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityCardFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Price(
        0x00000020,
        "主要活动", "悬赏", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityPriceFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Life(
        0x00000040,
        "主要活动", "生涯", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityLifeFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Achievement(
        0x00000080,
        "主要活动", "成就", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityAchieveFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Get_back(
        0x00000100,
        "主要活动", "回归", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_ActivityGetBackFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    );

    fun asTabBlockItem(): TabBlockItem {
        return TabBlockItem(
            0, title,
            imagePath,
            fragmentRouterPath,
            actionRouterPath
        )
    }

    fun getKey(): String = asTabBlockItem().key
    fun asTabChannel(): TabChannel = TabChannel(title, asTabBlockItem())
}