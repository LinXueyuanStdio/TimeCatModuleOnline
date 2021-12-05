package com.timecat.module.user.app.online

import android.net.Uri
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_CONTAINER
import com.timecat.identity.data.block.type.BLOCK_SPACE
import com.timecat.identity.data.block.type.CONTAINER_BLOCK_UNIVERSAL
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.prettyTitle
import com.timecat.middle.block.service.DNS
import com.timecat.middle.block.service.RouteSchema
import com.timecat.module.user.ext.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/12
 * @description null
 * @usage null
 * 在线超空间（重定向到 ServiceImpl）
 * 1. world://timecat.online?db={space-uuid}
 * space-uuid 用于提供 IDatabase，加载根目录
 *
 * 2. world://timecat.online?recordId={record-uuid}
 * 不需要提供 space-uuid，因为block自带
 *
 * 3. 首页（重定向到 ServiceImpl）
 * 首页 world://timecat.online/home == world://timecat.online/home?tab=recommend
 * 首页-推荐 world://timecat.online/home?tab=recommend
 * 首页-热门 world://timecat.online/home?tab=hot
 * 首页-关注 world://timecat.online/home?tab=focus
 *
 * 4. 动态（重定向到 ServiceImpl）
 * 动态 world://timecat.online/moment == world://timecat.online/moment?tab=recommend
 * 动态-推荐 world://timecat.online/moment?tab=recommend
 * 动态-热门 world://timecat.online/moment?tab=hot
 * 动态-关注 world://timecat.online/moment?tab=focus
 */
object TimeCatOnline {
    const val SCHEMA = "world"

    /**
     * 在线地图的目录
     */
    const val TocHost = "toc.timecat.online"
    const val tocUrl = "$SCHEMA://${TocHost}"

    /**
     * 在线超空间内容
     */
    const val Host = "timecat.online"
    const val url = "$SCHEMA://${Host}"

    /**
     * 官方在线超空间目录列表
     */
    const val OfficialHost = "official.timecat.online"
    const val officialUrl = "$SCHEMA://${OfficialHost}"

    /**
     * 我的在线，即当前登录用户的在线超空间目录列表
     */
    const val MineHost = "mine.timecat.online"
    const val MineUrl = "$SCHEMA://${MineHost}"

    const val PATH_home = "home"
    const val PATH_moment = "moment"
    const val PATH_game = "game"

    const val QUERY_Tab = "tab"
    const val TAB_recommend = "recommend"
    const val TAB_hot = "hot"
    const val TAB_focus = "focus"

    val homeMapSubItems = listOf(
        MapSubItem(
            "推荐",
            "推荐",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_home, GLOBAL_OnlineHomeRecommendService, TAB_recommend)
        ),
        MapSubItem(
            "热门",
            "热门",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_home, GLOBAL_OnlineHomeHotService, TAB_hot)
        ),
        MapSubItem(
            "关注",
            "关注",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_home, GLOBAL_OnlineHomeFocusService, TAB_focus)
        ),
    )
    val momentMapSubItems = listOf(
        MapSubItem(
            "推荐",
            "推荐",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_moment, GLOBAL_OnlineMomentRecommendService, TAB_recommend)
        ),
        MapSubItem(
            "热门",
            "热门",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_moment, GLOBAL_OnlineMomentHotService, TAB_hot)
        ),
        MapSubItem(
            "关注",
            "关注",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_moment, GLOBAL_OnlineMomentFocusService, TAB_focus)
        ),
    )
    val gameMapSubItems = listOf(
        MapSubItem(
            "图鉴",
            "物品等",
            "R.drawable.every_drawer_note",
            tab2Url(PATH_game, GLOBAL_OnlineGameService, TAB_recommend)
        ),
    )
    fun rootUri() = DNS.buildUri().authority(Host)

    //region tab
    /**
     * world://timecat.online/{pathSeg}?redirect={redirectService}&tab={tab}
     */
    fun tab2Url(pathSeg: String, redirectService: String, tab: String): String {
        val url = rootUri()
            .path(pathSeg)
            .appendQueryParameter(DNS.QUERY_Redirect, redirectService)
            .appendQueryParameter(QUERY_Tab, tab)
            .build().toString()
        return url
    }

    fun parseTabPath(parentUuid: String): String {
        val uri = Uri.parse(parentUuid)
        val tab = uri.getQueryParameter(QUERY_Tab) ?: TAB_recommend
        return tab
    }
    //endregion

    //region block
    fun block2Url(block: Block): String {
        val spaceId = if (block.type == BLOCK_SPACE || block.space == null) {
            block.objectId
        } else {
            block.space?.objectId ?: DNS.DEFAULT_QUERY_SpaceId
        }
        // 如果space为空，说明block就是超空间
        return DNS.buildUri(encodeSpaceId(spaceId), block.objectId, RouterHub.GLOBAL_BlockDetailService)
            .authority(Host)
            .build().toString()
    }

    fun encodeSpaceId(spaceId:String) :String = "${RouteSchema.OnlineHost}/${spaceId}"
    fun decodeSpaceId(spaceId:String) :String = spaceId.substringAfter("${RouteSchema.OnlineHost}/")
    //endregion
}