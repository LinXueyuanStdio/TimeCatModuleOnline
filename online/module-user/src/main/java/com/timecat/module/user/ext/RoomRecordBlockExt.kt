package com.timecat.module.user.ext

import cn.leancloud.AVACL
import cn.leancloud.AVRole
import cn.leancloud.AVUser
import cn.leancloud.Transformer
import com.alibaba.fastjson.JSONObject
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.identity.data.base.Json
import com.timecat.identity.data.block.type.BLOCK_DATABASE
import com.timecat.identity.data.block.type.BLOCK_SPACE
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.app.online.TimeCatOnline

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
    return RoomRecord.forName(title)
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
    record.bag.setUser(user)
    record.bag.setSpace(space)
    return record
}

fun RoomRecord.toBlock(user: User, space: Block?, newRecord: Boolean = false): Block {
    return copyFrom(fakeBlock(user, space), newRecord)
}

fun RoomRecord.fakeBlock(user: User, space: Block? = null): Block {
    return Block.forName(user, type, title).apply {
        this.space = space
    }
}

fun RoomRecord.copyFrom(record: Block, newRecord: Boolean = false): Block {
    record.name = name
    record.title = title
    record.content = content
    if (!newRecord) {
        record.objectId = uuid
    }
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
    record.user = bag.getUser() ?: UserDao.getCurrentUser()!! //TODO 离线情况下，可能因为没有登录，导致崩溃
    record.space = bag.getSpace()
    return record
}

fun JSONObject.getUser(): User? {
    val userObj = getString("user") ?: return null
    return User.transform(userObj)
}

fun JSONObject.setUser(user: User) {
    put("user", user.toJSONObject().toJSONString())
}

fun JSONObject.getSpace(): Block? {
    val jsonString = getString("space") ?: return null
    val rawObject = AVUser.parseAVObject(jsonString)
    return Transformer.transform(rawObject, Block::class.java)
}

fun JSONObject.setSpace(space: Block?) {
    if (space == null) return
    put("user", space.toJSONObject().toJSONString())
}

fun Block.withStruct(func: (cn.leancloud.json.JSONObject) -> Unit) {
    val s = struct
    func(s)
    struct = s
}

var Block.ext: Json
    get() = Json.fromJson(extObj.toJSONString())
    set(value) {
        val jsonString = value.toJson()
        extObj = cn.leancloud.json.JSON.parseObject(jsonString)
    }
var Block.extObj: cn.leancloud.json.JSONObject
    get() = struct.getJSONObject("ext") ?: cn.leancloud.json.JSONObject.Builder.create(null)
    set(value) {
        withStruct {
            it.put("ext", value)
        }
    }

var Block.parentId: String
    get() = parent?.objectId ?: ""
    set(value) {
        if (value == objectId) return
        if (value.isEmpty()) {
            if (type != BLOCK_SPACE) {
                parent = space
            }
        } else {
            parent = Block().also { it.objectId = value }
        }
        //会优先保存新的block，即新建的parent
        //但是当parent.objectId==""时，保存失败，会导致接下来主体的保存也失败
        //所以不能用""表示父节点，必须用space.objectId
    }
var Block.icon: String
    get() = struct.getString("icon") ?: IconLoader.randomAvatar(uuid)
    set(value) {
        withStruct {
            it.put("icon", value)
        }
    }
var Block.cover: String?
    get() = struct.getString("cover") ?: IconLoader.randomCover(uuid)
    set(value) {
        withStruct {
            it.put("cover", value)
        }
    }

var Block.name: String
    get() {
        if (type == BLOCK_DATABASE) {
            return TimeCatOnline.block2Url(this)
        }
        return struct.getString("name") ?: ""
    }
    set(value) {
        withStruct {
            it.put("name", value)
        }
    }
var Block.startTime: Long
    get() = struct.getLong("startTime") ?: 0
    set(value) {
        withStruct {
            it.put("startTime", value)
        }
    }

var Block.totalLength: Long
    get() = struct.getLong("totalLength") ?: 0
    set(value) {
        withStruct {
            it.put("totalLength", value)
        }
    }

var Block.miniShowType: Int
    get() = struct.getInteger("miniShowType") ?: 0
    set(value) {
        withStruct {
            it.put("miniShowType", value)
        }
    }

var Block.render_type: Int
    get() = struct.getInteger("render_type") ?: 0
    set(value) {
        withStruct {
            it.put("render_type", value)
        }
    }

var Block.label: Int
    get() = struct.getInteger("label") ?: 0
    set(value) {
        withStruct {
            it.put("label", value)
        }
    }

var Block.color: Int
    get() = struct.getInteger("color") ?: 0
    set(value) {
        withStruct {
            it.put("color", value)
        }
    }

var Block.tags: String
    get() = struct.getString("tags") ?: ""
    set(value) {
        withStruct {
            it.put("tags", value)
        }
    }

var Block.topics: String
    get() = struct.getString("topics") ?: ""
    set(value) {
        withStruct {
            it.put("topics", value)
        }
    }

var Block.attachmentItems: AttachmentTail
    get() {
        val jsonStr = struct.getJSONObject("attachmentItems") ?: return AttachmentTail(mutableListOf())
        return AttachmentTail.fromJson(jsonStr.toJSONString())
    }
    set(value) {
        withStruct {
            it.put("attachmentItems", value.toJsonObject())
        }
    }

fun Block.privateAcl(I: User) {
    val ownerAcl = AVACL()
    ownerAcl.publicReadAccess = false
    ownerAcl.publicWriteAccess = false
    ownerAcl.setReadAccess(I, true)
    ownerAcl.setWriteAccess(I, true)
    acl = ownerAcl
}

fun Block.publicViewAcl(I: User) {
    val ownerAcl = AVACL()
    ownerAcl.publicReadAccess = true
    ownerAcl.publicWriteAccess = false
    ownerAcl.setReadAccess(I, true)
    ownerAcl.setWriteAccess(I, true)
    acl = ownerAcl
}

fun Block.publicCommentAcl(I: User) {
    val ownerAcl = AVACL()
    ownerAcl.publicReadAccess = true
    ownerAcl.publicWriteAccess = false
    ownerAcl.setReadAccess(I, true)
    ownerAcl.setWriteAccess(I, true)
    acl = ownerAcl
    AVRole()
}
