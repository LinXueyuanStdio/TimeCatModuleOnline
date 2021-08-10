package com.timecat.module.user.ext

import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.room.record.RoomRecord

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/3
 * @description null
 * @usage null
 */
fun Block.toRoomRecord(): RoomRecord {
    return copyFrom(fakeRecord())
}

fun Block.fakeRecord(): RoomRecord {
    return RoomRecord.forName("")
}

fun Block.copyFrom(record: RoomRecord): RoomRecord {
    //    record.name = name
    record.title = title
    record.content = content
    record.uuid = objectId
//    record.mtime = mtime
//    record.icon = icon
//    record.cover = coverImageUrl
//    record.isDummy = isDummy
    record.type = type
    record.subType = subtype
//    record.finishTime = finishTime
//    record.deleteTime = deleteTime
//    record.archiveTime = archiveTime
//    record.pinTime = pinTime
//    record.lockTime = lockTime
//    record.blockTime = blockTime
//    record.startTime = startTime
//    record.totalLength = totalLength
//    record.label = label
    record.status = status
//    record.theme = theme
//    record.color = color
//    record.miniShowType = miniShowType
//    record.render_type = render_type
    record.order = order
//    record.tags = tags
//    record.topics = topics
    record.parent = parent?.objectId ?: ""
//    record.ext = ext
//    record.attachmentItems = attachmentItems
    return record
}

fun RoomRecord.toBlock(user: User): Block {
    return copyFrom(fakeBlock(user))
}

fun RoomRecord.fakeBlock(user: User): Block {
    return Block.forName(user, type, title)
}

fun RoomRecord.copyFrom(record: Block): Block {
//    record.name = name
    record.title = title
    record.content = content
    record.objectId = uuid
//    record.mtime = mtime
//    record.icon = icon
//    record.cover = coverImageUrl
//    record.isDummy = isDummy
    record.type = type
    record.subtype = subType
//    record.finishTime = finishTime
//    record.deleteTime = deleteTime
//    record.archiveTime = archiveTime
//    record.pinTime = pinTime
//    record.lockTime = lockTime
//    record.blockTime = blockTime
//    record.startTime = startTime
//    record.totalLength = totalLength
//    record.label = label
    record.status = status
//    record.theme = theme
//    record.color = color
//    record.miniShowType = miniShowType
//    record.render_type = render_type
    record.order = order
//    record.tags = tags
//    record.topics = topics
    record.parent = Block().also { it.objectId = parent }
//    record.ext = ext
//    record.attachmentItems = attachmentItems
    return record
}