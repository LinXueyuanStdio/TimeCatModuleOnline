package com.timecat.module.user.ext

import com.alibaba.fastjson.JSON
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.identity.data.base.Json

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
    record.name = name
    record.title = title
    record.content = content
    record.uuid = objectId
//    record.mtime = mtime
    record.icon = icon
    record.coverImageUrl = cover
//    record.isDummy = isDummy
    record.type = type
    record.subType = subtype
//    record.finishTime = finishTime
//    record.deleteTime = deleteTime
//    record.archiveTime = archiveTime
//    record.pinTime = pinTime
//    record.lockTime = lockTime
//    record.blockTime = blockTime
    record.startTime = startTime
    record.totalLength = totalLength
    record.label = label
    record.status = status
//    record.theme = theme
    record.color = color
    record.miniShowType = miniShowType
    record.render_type = render_type
    record.order = order
    record.tags = tags
    record.topics = topics
    record.parent = parentId
    record.ext = ext
    record.attachmentItems = attachmentItems
    return record
}

fun RoomRecord.toBlock(user: User): Block {
    return copyFrom(fakeBlock(user))
}

fun RoomRecord.fakeBlock(user: User): Block {
    return Block.forName(user, type, title)
}

fun RoomRecord.copyFrom(record: Block): Block {
    record.name = name
    record.title = title
    record.content = content
    record.objectId = uuid
//    record.mtime = mtime
    record.icon = icon
    record.cover = coverImageUrl
//    record.isDummy = isDummy
    record.type = type
    record.subtype = subType
//    record.finishTime = finishTime
//    record.deleteTime = deleteTime
//    record.archiveTime = archiveTime
//    record.pinTime = pinTime
//    record.lockTime = lockTime
//    record.blockTime = blockTime
    record.startTime = startTime
    record.totalLength = totalLength
    record.label = label
    record.status = status
//    record.theme = theme
    record.color = color
    record.miniShowType = miniShowType
    record.render_type = render_type
    record.order = order
    record.tags = tags
    record.topics = topics
    record.parentId = parent
    record.ext = ext
    record.attachmentItems = attachmentItems
    return record
}

var Block.ext: Json
    get() = Json(JSON.parseObject(structure))
    set(value) {
        structure = value.toJson()
    }
var Block.parentId: String
    get() = parent?.objectId ?: ""
    set(value) {
        Block().also { it.objectId = value }
    }
var Block.icon: String
    get() = simpleAvatar()
    set(value) {

    }
var Block.cover: String?
    get() = simpleAvatar()
    set(value) {
    }

var Block.name: String
    get() = struct.getString("name") ?: ""
    set(value) {
        struct.put("name", value)
    }
var Block.startTime: Long
    get() = struct.getLong("startTime") ?: 0
    set(value) {
        struct.put("startTime", value)
    }

var Block.totalLength: Long
    get() = struct.getLong("totalLength") ?: 0
    set(value) {
        struct.put("totalLength", value)
    }

var Block.miniShowType: Int
    get() = struct.getInteger("miniShowType") ?: 0
    set(value) {
        struct.put("miniShowType", value)
    }

var Block.render_type: Int
    get() = struct.getInteger("render_type") ?: 0
    set(value) {
        struct.put("render_type", value)
    }

var Block.label: Int
    get() = struct.getInteger("label") ?: 0
    set(value) {
        struct.put("label", value)
    }

var Block.color: Int
    get() = struct.getInteger("color") ?: 0
    set(value) {
        struct.put("color", value)
    }

var Block.tags: String
    get() = struct.getString("tags") ?: ""
    set(value) {
        struct.put("tags", value)
    }
var Block.topics: String
    get() = struct.getString("topics") ?: ""
    set(value) {
        struct.put("topics", value)
    }
var Block.attachmentItems: AttachmentTail
    get() {
        val jsonStr = struct.getString("attachmentItems") ?: ""
        return AttachmentTail.fromJson(jsonStr)
    }
    set(value) {
        struct.put("attachmentItems", value.toJson())
    }
