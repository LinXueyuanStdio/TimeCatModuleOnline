package com.timecat.module.user.record

import android.content.Context
import cn.leancloud.AVQuery
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.net.allBlock
import com.timecat.data.bmob.ext.net.allBlockByIds
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.data.room.record.RoomRecord
import com.timecat.middle.block.service.IDatabase
import com.timecat.middle.block.service.RequestListCallback
import com.timecat.middle.block.service.RequestSingleOrNullCallback
import com.timecat.module.user.ext.toBlock
import com.timecat.module.user.ext.toRoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/7/4
 * @description 符文生态
 * 接入符文生态，只需要提供后端实现即可
 * @usage null
 */
class OnlineBackendDb(val context: Context, val owner: User) : IDatabase {
    override fun updateRecord(record: RoomRecord) {
        saveBlock {
            target = record.toBlock(owner)
        }
    }

    override fun insertRecord(record: RoomRecord) {
        saveBlock {
            target = record.toBlock(owner)
        }
    }

    override fun deleteRecord(record: RoomRecord) {
        deleteBlock {
            target = record.toBlock(owner)
        }
    }

    override fun replaceRecord(record: RoomRecord) {
        saveBlock {
            target = record.toBlock(owner)
        }
    }

    override fun hardDeleteBatch(record: List<RoomRecord>) {
        com.timecat.data.bmob.ext.bmob.deleteBatch {
            target = record.map { it.toBlock(owner) }
        }
    }

    override fun updateRoomRecords(records: List<RoomRecord>) {
        saveBatch {
            target = records.map { it.toBlock(owner) }
        }
    }

    override fun getByUuid(
        uuid: String,
        callback: RequestSingleOrNullCallback<RoomRecord>.() -> Unit
    ) {
        val cb = RequestSingleOrNullCallback<RoomRecord>().apply(callback)
        requestOneBlockOrNull {
            query = oneBlockOf(uuid)
            onSuccess = { cb.onSuccess(it.toRoomRecord()) }
            onError = { cb.onError(it) }
            onEmpty = { cb.onEmpty() }
        }
    }

    fun runSql(q: AVQuery<Block>, callback: RequestListCallback<RoomRecord>.() -> Unit) {
        val cb = RequestListCallback<RoomRecord>().apply(callback)
        requestBlock {
            query = q
            onSuccess = { cb.onSuccess(it.map { it.toRoomRecord() }.toMutableList()) }
            onError = { cb.onError(it) }
            onEmpty = { cb.onEmpty() }
        }
    }

    override fun getByUuids(uuid: List<String>, callback: RequestListCallback<RoomRecord>.() -> Unit) {
        runSql(allBlockByIds(uuid), callback)
    }

    override fun getAllLiveChildren(
        uuid: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        val q = allBlock().apply {
            skip(offset)
            setLimit(pageSize)
        }
        runSql(q, callback)
    }

    override fun getAllLiveMessage(
        uuid: String,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        val q = allBlock().apply {
            skip(offset)
            setLimit(pageSize)
        }
        runSql(q, callback)
    }

    override fun getAllRecords(
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        val q = allBlock().apply {
            skip(offset)
            setLimit(pageSize)
        }
        runSql(q, callback)
    }

    override fun getAllTimeRecords(
        fromTs: Long, toTs: Long,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        val q = allBlock().apply {
            skip(offset)
            setLimit(pageSize)
        }
        runSql(q, callback)
    }

    override fun getAllByTypeAndSubtype(
        type: Int, subType: Int,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        val q = allBlock().apply {
            skip(offset)
            setLimit(pageSize)
        }
        runSql(q, callback)
    }

    override fun searchAll(
        query: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        val q = allBlock().apply {
            skip(offset)
            setLimit(pageSize)
        }
        runSql(q, callback)
    }
}