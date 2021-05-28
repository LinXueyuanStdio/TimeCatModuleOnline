package com.timecat.module.user.view.item

import android.content.Context
import com.shuyu.textutillib.RichEditText
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.base.*
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem
import org.joda.time.DateTime

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/14
 * @description null
 * @usage null
 */
class MaterialForm {
    //region titleItem -> title
    var title: String
        get() = titleItem.text
        set(value) {
            titleItem.text = value
        }
    lateinit var titleItem: InputItem

    //endregion

    //region urlItem -> url
    var url: String
        get() = urlItem.text
        set(value) {
            urlItem.text = value
        }
    lateinit var urlItem: InputItem
    //endregion

    //region packageNameItem -> packageName
    var packageName: String
        get() = packageNameItem.text
        set(value) {
            packageNameItem.text = value
        }
    lateinit var packageNameItem: InputItem
    //endregion

    //region blockItem -> blockId
    var block: Block? = null
        set(value) {
            value?.let {
                blockId = it.objectId
                blockName = it.title
            }
            field = value
        }
    var blockId: String
        get() = blockItem.hint ?: ""
        set(value) {
            blockItem.hint = value
        }
    var blockName: String
        get() = blockItem.text
        set(value) {
            blockItem.text = value
        }
    lateinit var blockItem: NextItem
    //endregion

    //region userItem -> userId, user
    var user: User? = null
        set(value) {
            value?.let { userId = it.objectId }
            field = value
        }
    var userId: String
        get() = userItem.text
        set(value) {
            userItem.text = value
        }
    lateinit var userItem: NextItem
    //endregion

    //region whereItem -> where, numItem -> num
    var tableName: String
        get() = tableNameItem.text
        set(value) {
            tableNameItem.text = value
        }
    lateinit var tableNameItem: InputItem
    var where: String
        get() = whereItem.text
        set(value) {
            whereItem.text = value
        }
    lateinit var whereItem: InputItem
    var num: Long
        get() = numItem.text.toLongOrNull() ?: 0
        set(value) {
            numItem.text = "$value"
        }
    lateinit var numItem: InputItem
    //endregion

    //region iconItem -> icon
    var icon: String
        get() = iconItem.icon
        set(value) {
            iconItem.icon = value
        }
    lateinit var iconItem: ImageItem
    //endregion

    //region coverItem -> cover
    var cover: String
        get() = coverItem.icon
        set(value) {
            coverItem.icon = value
        }
    lateinit var coverItem: ImageItem
    //endregion

    //region emojiEditText -> content, topicScope, atScope, attachments
    val content: String
        get() = emojiEditText.realText
    val topicScope: TopicScope?
        get() = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
    val atScope: AtScope?
        get() = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }

    fun setContentScope(context: Context, content: String, atScope: AtScope?, topicScope: TopicScope?) {
        emojiEditText.resolveInsertText(context, content,
            atScope?.let {
                it.ats.map { UserModel(it.name, it.objectId) }
            },
            topicScope?.let {
                it.topics.map { TopicModel(it.name, it.objectId) }
            })
    }

    fun setContent(context: Context, content: String, userModels: List<UserModel>?, topicModels: List<TopicModel>?) {
        emojiEditText.resolveInsertText(context, content, userModels, topicModels)
    }

    fun setScope(atScope: AtScope?, topicScope: TopicScope?) {
        atScope?.let {
            emojiEditText.setRichEditNameList(it.ats.map { UserModel(it.name, it.objectId) })
        }
        topicScope?.let {
            emojiEditText.setRichEditTopicList(it.topics.map { TopicModel(it.name, it.objectId) })
        }
    }

    lateinit var emojiEditText: RichEditText
    //endregion

    //region activeDateTimeItem, expireDateTimeItem -> activeDateTime, expireDateTime
    var activeDateTime: DateTime = DateTime()
        set(value) {
            activeDateTimeItem.text = value.toString("yyyy-MM-dd")
            field = value
        }
    var expireDateTime: DateTime = DateTime()
        set(value) {
            expireDateTimeItem.text = value.toString("yyyy-MM-dd")
            field = value
        }
    lateinit var activeDateTimeItem: NextItem
    lateinit var expireDateTimeItem: NextItem
    //endregion

    var attachments: AttachmentTail? = null
    var blocks: MutableList<Block> = ArrayList()

}