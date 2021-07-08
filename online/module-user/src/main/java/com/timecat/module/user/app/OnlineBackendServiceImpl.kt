package com.timecat.module.user.app

//import android.content.Context
//import com.timecat.data.bmob.data.User
//import com.timecat.data.room.habit.Habit
//import com.timecat.data.room.record.RecordDao
//import com.timecat.data.room.record.RoomRecord
//import com.timecat.data.room.tag.Tag
//import com.timecat.middle.block.service.IDatabase
//import com.timecat.middle.block.service.ONLINE_SCHEMA
//import com.timecat.middle.block.service.OnlineBackendService
//import com.xiaojinzi.component.anno.ServiceAnno
//
///**
// * @author 林学渊
// * @email linxy59@mail2.sysu.edu.cn
// * @date 2021/7/4
// * @description null
// * @usage null
// */
//@ServiceAnno(OnlineBackendService::class)
//class OnlineBackendServiceImpl : OnlineBackendService {
//    override fun buildBackend(context: Context, url: String): IDatabase? {
//        val objId = url.substringAfter(ONLINE_SCHEMA)
//        return null
//    }
//}
//
//class PermissionController(owner: User) {
//
//}
//
//const val BLOCK_PERM_no_access = 0
//const val BLOCK_PERM_can_view = 1
//const val BLOCK_PERM_can_comment = 2
//const val BLOCK_PERM_can_edit = 3
//const val BLOCK_PERM_full_access = 4
//
//class OnlineBackendDb() : IDatabase {
//    override fun updateRecord(record: RoomRecord) {}
//    override fun insertRecord(record: RoomRecord) {}
//    override fun deleteRecord(record: RoomRecord) {}
//    override fun replaceRecord(record: RoomRecord) {}
//    override fun hardDeleteBatch(uuids: List<String>) {}
//    override fun deleteBatch(uuids: List<String>) {}
//    override fun archiveBatch(uuids: List<String>) {}
//    override fun updateRoomRecords(vararg record: RoomRecord) {}
//    override fun getByUuid(uuid: String): RoomRecord? {
//
//    }
//
//    override fun getAllLiveChildren(uuid: String, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//
//    }
//
//    override fun getAllLiveMessage(uuid: String, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//
//    }
//
//    override fun getAllRecords(order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//
//    }
//
//    override fun getAllTimeRecords(fromTs: Long, toTs: Long, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//
//    }
//
//    override fun getAllByTypeAndSubtype(type: Int, subType: Int, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//
//    }
//
//    override fun searchAll(query: String, order: Int, asc: Boolean, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//
//    }
//
//    override fun getAllRecordData(all: List<RoomRecord>, listener: RecordDao.OnRecordDataLoaded) {}
//    override fun getAllData(all: List<RoomRecord>, listener: RecordDao.OnDataLoaded) {}
//
//    override fun getHabit(id: Long): Habit? {
//
//    }
//
//    override fun getAllTags(uuid: List<String>): List<Tag> {
//
//    }
//
//    override fun getAllTags(): List<Tag> {
//
//    }
//
//    override fun insertTag(tag: Tag) {}
//}
