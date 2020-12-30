package com.timecat.module.user.social.cloud.channel

import com.timecat.identity.readonly.RouterHub

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/8
 * @description null
 * @usage null
 */
enum class UserChannel(
    val id: Long,
    val category: String,
    var title: String,
    var imagePath: String,
    var fragmentRouterPath: String,
    var actionRouterPath: String
) {
    //1.固定
    World(
        0x00000001,
        "固定", "世界", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_WorldFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),

    //2.私人
    Focus(
        0x00000002,
        "私人", "关注", "R.drawable.ic_home_white_24dp",
        RouterHub.USER_FocusFragment,
        RouterHub.SMALL_EDITOR_BlockSmallEditorActivity
    ),
    Recommend(
        0x00000003,
        "私人", "推荐", "R.drawable.ic_cloud_white_24dp",
        RouterHub.USER_RecommendFragment,
        RouterHub.USER_AddMomentActivity
    ),

    //3.可选
    Forum(
        0x00000004,
        "可选", "论坛", "R.drawable.ic_cloud_white_24dp",
        RouterHub.USER_ForumFragment,
        RouterHub.USER_AddMomentActivity
    ),

}