package com.timecat.module.user.app.online

import android.net.Uri
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.block.type.BLOCK_CONTAINER
import com.timecat.identity.data.block.type.CONTAINER_BLOCK_UNIVERSAL
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.prettyTitle
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
    const val Host = "timecat.online"
    const val url = "$SCHEMA://$Host"

    const val PATH_home = "home"
    const val PATH_moment = "moment"
    const val QUERY_Tab = "tab"
    const val TAB_recommend = "recommend"
    const val TAB_hot = "hot"
    const val TAB_focus = "focus"

    const val QUERY_Redirect = "redirect"

    const val QUERY_SpaceId = "spaceId"
    const val QUERY_RecordId = "recordId"

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

    fun getRecordId(url: String): String {
        if (url.startsWith(SCHEMA)) {
            val uri = Uri.parse(url)
            val dbPath = uri.getQueryParameter(QUERY_SpaceId)
            val recordId = uri.getQueryParameter(QUERY_RecordId)
            return when {
                dbPath != null -> dbPath
                recordId != null -> recordId
                else -> ""
            }
        } else {
            return url
        }
    }

    fun parseTabPath(parentUuid: String): String {
        val uri = Uri.parse(parentUuid)
        val tab = uri.getQueryParameter(QUERY_Tab) ?: TAB_recommend
        return tab
    }

    fun parsePath(
        parentUuid: String,
        onSpace: (String) -> Unit,
        onBlock: (String) -> Unit,
        onFail: () -> Unit,
    ) {
        val uri = Uri.parse(parentUuid)
        val dbPath = uri.getQueryParameter(QUERY_SpaceId)
        val recordId = uri.getQueryParameter(QUERY_RecordId)
        when {
            dbPath != null -> onSpace(dbPath)
            recordId != null -> onBlock(recordId)
            else -> onFail()
        }
    }

    fun rootUri() = Uri.EMPTY.buildUpon()
        .scheme(SCHEMA)
        .authority(Host)

    fun tab2Url(pathSeg: String, redirectService: String, tab: String): String {
        val url = rootUri()
            .path(pathSeg)
            .appendQueryParameter(QUERY_Redirect, redirectService)
            .appendQueryParameter(QUERY_Tab, tab)
            .build().toString()
        return url
    }

    fun toPath(block: Block): Path {
        val key = if (block.space == null) QUERY_SpaceId else QUERY_RecordId
        val url = rootUri()
            .appendQueryParameter(QUERY_Redirect, RouterHub.GLOBAL_BlockDetailService)
            .appendQueryParameter(key, block.objectId)
            .build().toString()
        return Path(block.title, url, CONTAINER_BLOCK_UNIVERSAL)
    }

    fun toUrl(block: Block): String {
        val key = if (block.space == null) QUERY_SpaceId else QUERY_RecordId
        return rootUri()
            .appendQueryParameter(QUERY_Redirect, RouterHub.GLOBAL_BlockDetailService)
            .appendQueryParameter(key, block.objectId)
            .build().toString()
    }

    fun toNavigate(parentPath: Path, record: RoomRecord): Triple<String, String, Int> {
        val uri = Uri.parse(parentPath.uuid)
        val db = uri.getQueryParameter(QUERY_SpaceId)
            ?: return Triple(parentPath.name, parentPath.uuid, parentPath.type)

        val url = Uri.EMPTY.buildUpon()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .appendQueryParameter(QUERY_SpaceId, db)
            .appendQueryParameter(QUERY_RecordId, record.uuid)
            .build().toString()
        LogUtil.e(url)
        //TODO 支持包括文件夹的其他类型
        //type
        //-1 :为本space下的记录。本space下的BLOCK_DATABASE仍然是-1
        //>0 :为嵌入应用的记录
        //-2 :为某个database里的记录，要处理当前TimeCatDatabase。路径在uuid里
        val type = if (record.type == BLOCK_CONTAINER) {
            record.subType
        } else CONTAINER_BLOCK_UNIVERSAL
        return Triple(record.prettyTitle, url, type)
    }

    fun toPath(parentPath: Path, record: RoomRecord): Path {
        val (title, url, type) = toNavigate(parentPath, record)
        return Path(title, url, type, parent = parentPath)
    }
}