package com.timecat.module.user.record

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlockOrNull
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.middle.block.service.CardPermission
import com.timecat.middle.block.service.EmptyDatabase
import com.timecat.middle.block.service.IBackend
import com.timecat.middle.block.service.IDatabase
import com.timecat.module.user.app.online.TimeCatOnline
import com.timecat.module.user.ext.allowPublicEdit
import com.timecat.module.user.ext.allowPublicInteract
import com.timecat.module.user.ext.allowPublicRead
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/12/4
 * @description null
 * @usage null
 */
class OnlineBackend(val context: Context, val I: User?) : IBackend {

//    private fun existSpace(I: User, space: Block, block: Block, path: Path, context: Context, parentUuid: String, homeService: HomeService) {
//        val remoteDb = OnlineBackendDb(context, I, space)
//        homeService.loadDatabase(TimeCatOnline.space2Url(space), remoteDb)
//        homeService.loadContextRecord(block.toRoomRecord())
//    }

    suspend fun getBlock(context: Context, q: AVQuery<Block>): Block? {
        return suspendCoroutine { con ->
            requestOneBlockOrNull {
                query = q
                onSuccess = {
                    con.resume(it)
                }
                onError = {
                    con.resume(null)
                }
                onEmpty = {
                    con.resume(null)
                }
            }
        }
    }

    override suspend fun getDatabase(context: Context, spaceId: String): IDatabase {
        if (I == null) return EmptyDatabase()
        val realSpaceId = TimeCatOnline.decodeSpaceId(spaceId)
        val space = getBlock(context, oneBlockOf(realSpaceId)) ?: return EmptyDatabase()
        return OnlineBackendDb(context, I, space)

//        requestOneBlockOrNull {
//            query = oneBlockOf(uuid)
//            onSuccess = {
//                val space = it.space
//                if (space == null) {
//                    callback.onFail("在线数据库不存在") {
//
//                    }
//                } else {
//                    val db = OnlineBackendDb(context, I, space)
//                    callback.onSuccess(db)
//                }
//            }
//        }
    }

    override suspend fun getPermission(context: Context, spaceId: String): CardPermission {
        if (I == null) return CardPermission.NoAccess
        val realSpaceId = TimeCatOnline.decodeSpaceId(spaceId)
        val query = oneBlockOf(realSpaceId).includeACL(true)
        val space = getBlock(context, query) ?: return CardPermission.NoAccess
        val canRead = space.acl.getReadAccess(I)
        val canEdit = space.acl.getWriteAccess(I)
        val permission = when {
            //高权限 优先判断
            space.user.objectId == I.objectId -> CardPermission.FullAccess
            space.allowPublicEdit -> CardPermission.Editable
            canEdit -> CardPermission.Editable
            space.allowPublicInteract -> CardPermission.Interactive
            space.allowPublicRead -> CardPermission.ReadOnly
            canRead -> CardPermission.ReadOnly
            else -> CardPermission.NoAccess
        }
        return permission
    }

    override suspend fun getDatabaseAndPermission(context: Context, spaceId: String): Pair<CardPermission, IDatabase> {
        if (I == null) return CardPermission.NoAccess to EmptyDatabase()
        val realSpaceId = TimeCatOnline.decodeSpaceId(spaceId)
        val query = oneBlockOf(realSpaceId).includeACL(true)
        val space = getBlock(context, query) ?: return CardPermission.NoAccess to EmptyDatabase()
        val db = OnlineBackendDb(context, I, space)
        val canRead = space.acl.getReadAccess(I)
        val canEdit = space.acl.getWriteAccess(I)
        val permission = when {
            //高权限 优先判断
            space.user.objectId == I.objectId -> CardPermission.FullAccess
            space.allowPublicEdit -> CardPermission.Editable
            canEdit -> CardPermission.Editable
            space.allowPublicInteract -> CardPermission.Interactive
            space.allowPublicRead -> CardPermission.ReadOnly
            canRead -> CardPermission.ReadOnly
            else -> CardPermission.NoAccess
        }
        return permission to db
    }
}