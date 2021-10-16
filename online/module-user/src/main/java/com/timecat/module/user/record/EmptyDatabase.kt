package com.timecat.module.user.record

import com.timecat.data.room.record.RoomRecord
import com.timecat.middle.block.service.IDatabase
import com.timecat.middle.block.service.RequestListCallback
import com.timecat.middle.block.service.RequestSingleOrNullCallback

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
class EmptyDatabase : IDatabase {
    override fun updateRecord(record: RoomRecord) {
    }

    override fun insertRecord(
        record: RoomRecord,
        callback: RequestSingleOrNullCallback<RoomRecord>.() -> Unit
    ) {
        val cb = RequestSingleOrNullCallback<RoomRecord>().apply(callback)
        cb.onEmpty()
    }

    override fun deleteRecord(record: RoomRecord) {
    }

    override fun replaceRecord(record: RoomRecord) {
    }

    override fun hardDeleteBatch(record: List<RoomRecord>) {
    }

    override fun updateRoomRecords(records: List<RoomRecord>) {
    }

    override fun getByUuid(
        uuid: String,
        callback: RequestSingleOrNullCallback<RoomRecord>.() -> Unit
    ) {
        val cb = RequestSingleOrNullCallback<RoomRecord>().apply(callback)
        cb.onEmpty()
    }

    fun runSql(callback: RequestListCallback<RoomRecord>.() -> Unit) {
        val cb = RequestListCallback<RoomRecord>().apply(callback)
        cb.onEmpty()
    }

    override fun getByUuids(uuid: List<String>, callback: RequestListCallback<RoomRecord>.() -> Unit) {
        runSql(callback)
    }

    override fun getAllLiveChildren(
        uuid: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        runSql(callback)
    }

    override fun getAllLiveMessage(
        uuid: String,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        runSql(callback)
    }

    override fun getAllRecords(
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        runSql(callback)
    }

    override fun getAllTimeRecords(
        fromTs: Long, toTs: Long,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        runSql(callback)
    }

    override fun getAllByTypeAndSubtype(
        type: Int, subType: Int,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        runSql(callback)
    }

    override fun getAllByTypeAndSubtype(type: Int, subTypes: List<Int>, order: Int, asc: Boolean, offset: Int, pageSize: Int, callback: RequestListCallback<RoomRecord>.() -> Unit) {
        runSql(callback)
        runSql(callback)
    }

    override fun searchAll(
        query: String,
        order: Int, asc: Boolean,
        offset: Int, pageSize: Int,
        callback: RequestListCallback<RoomRecord>.() -> Unit
    ) {
        runSql(callback)
    }
}