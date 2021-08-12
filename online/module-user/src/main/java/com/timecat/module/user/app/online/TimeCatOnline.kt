package com.timecat.module.user.app.online

import android.net.Uri
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.room.record.RoomRecord
import com.timecat.data.room.space.Space
import com.timecat.identity.data.block.type.BLOCK_CONTAINER
import com.timecat.identity.data.block.type.CONTAINER_BLOCK_UNIVERSAL
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.middle.block.ext.prettyTitle

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/12
 * @description null
 * @usage null
 * 在线
 * 1. world://timecat.online?db={space-uuid}&recordId={record-uuid}
 * space-uuid 用于提供 IDatabase
 * record-uuid 交给下一步 Path 用于加载容器符文
 *
 * 2. world://timecat.online?recordId={record-uuid}
 * 不提供 space-uuid 则为默认主数据库
 *
 * 3. 首页
 * 首页 world://timecat.online/home == world://timecat.online/home?tab=recommend
 * 首页-推荐 world://timecat.online/home?tab=recommend
 * 首页-热门 world://timecat.online/home?tab=hot
 * 首页-关注 world://timecat.online/home?tab=focus
 *
 * 4. 动态
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

    const val QUERY_Space = "db"
    val DEFAULT_QUERY_Space: String
        get() = Space.default().dbPath
    const val QUERY_RecordId = "recordId"
    const val DEFAULT_QUERY_RecordId: String = ""

    fun getRecordId(url: String): String {
        if (url.startsWith(SCHEMA)) {
            val uri = Uri.parse(url)
            return uri.getQueryParameter(QUERY_RecordId) ?: ""
        } else {
            return url
        }
    }

    fun parseTabPath(parentUuid: String): String {
        val uri = Uri.parse(parentUuid)
        val tab = uri.getQueryParameter(QUERY_Tab) ?: TAB_recommend
        return tab
    }

    fun parsePath(parentUuid: String): Pair<String, String> {
        val uri = Uri.parse(parentUuid)
        val dbPath = uri.getQueryParameter(QUERY_Space) ?: DEFAULT_QUERY_Space
        val recordId = uri.getQueryParameter(QUERY_RecordId) ?: DEFAULT_QUERY_RecordId
        return dbPath to recordId
    }

    fun rootPath(): Path {
        return toPath(Space.default())
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

    fun toPath(space: Space): Path {
        val url = rootUri()
            .appendQueryParameter(QUERY_Space, space.dbPath)
            .appendQueryParameter(QUERY_RecordId, DEFAULT_QUERY_RecordId)
            .build().toString()
        return Path(space.title, url, CONTAINER_BLOCK_UNIVERSAL)
    }

    fun toNavigate(parentPath: Path, record: RoomRecord): Triple<String, String, Int> {
        val uri = Uri.parse(parentPath.uuid)
        val db = uri.getQueryParameter(QUERY_Space)
            ?: return Triple(parentPath.name, parentPath.uuid, parentPath.type)

        val url = Uri.EMPTY.buildUpon()
            .scheme(uri.scheme)
            .authority(uri.authority)
            .appendQueryParameter(QUERY_Space, db)
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