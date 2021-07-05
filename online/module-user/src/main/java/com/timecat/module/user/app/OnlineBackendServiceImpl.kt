package com.timecat.module.user.app
//
//import android.content.Context
//import com.timecat.data.bmob.data.User
//import com.timecat.data.room.habit.Habit
//import com.timecat.data.room.record.RecordDao
//import com.timecat.data.room.record.RoomRecord
//import com.timecat.data.room.tag.Tag
//import com.timecat.middle.block.service.IDatabase
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
//class OnlineBackendServiceImpl: OnlineBackendService {
//    override fun buildBackend(context: Context, url: String): IDatabase {
//
//    }
//}
//class PermissionController(owner: User) {
//
//}
//class OnlineBackendDb(): IDatabase {
//    override fun archiveBatch(uuids: List<String>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteBatch(uuids: List<String>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun deleteRecord(record: RoomRecord) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllByTypeAndSubtype(type: Int, subType: Int): MutableList<RoomRecord> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllData(all: List<RoomRecord>, listener: RecordDao.OnDataLoaded) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllLiveChildren(uuid: String): MutableList<RoomRecord> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllRecordData(all: List<RoomRecord>, listener: RecordDao.OnRecordDataLoaded) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllRecords(): MutableList<RoomRecord> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllTags(): List<Tag> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllTags(uuid: List<String>): List<Tag> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getAllTimeRecords(fromTs: Long, toTs: Long): MutableList<RoomRecord> {
//        TODO("Not yet implemented")
//    }
//
//    override fun getByUuid(uuid: String): RoomRecord? {
//        TODO("Not yet implemented")
//    }
//
//    override fun getHabit(id: Long): Habit? {
//        TODO("Not yet implemented")
//    }
//
//    override fun hardDeleteBatch(uuids: List<String>) {
//        TODO("Not yet implemented")
//    }
//
//    override fun insertRecord(record: RoomRecord) {
//        TODO("Not yet implemented")
//    }
//
//    override fun insertTag(tag: Tag) {
//        TODO("Not yet implemented")
//    }
//
//    override fun replaceRecord(record: RoomRecord) {
//        TODO("Not yet implemented")
//    }
//
//    override fun searchAll(query: String, offset: Int, pageSize: Int): MutableList<RoomRecord> {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateRecord(record: RoomRecord) {
//        TODO("Not yet implemented")
//    }
//
//    override fun updateRoomRecords(vararg record: RoomRecord) {
//        TODO("Not yet implemented")
//    }
//
//}
