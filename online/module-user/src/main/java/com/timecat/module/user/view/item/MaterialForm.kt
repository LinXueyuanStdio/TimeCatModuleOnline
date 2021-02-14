package com.timecat.module.user.view.item

import com.shuyu.textutillib.RichEditText
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.base.*
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem

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

    //region blockItem -> uuid
    var uuid: String
        get() = blockItem.text
        set(value) {
            blockItem.text = value
        }
    lateinit var blockItem: NextItem
    //endregion

    //region whereItem -> where, numItem -> num
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
    var content: String
        get() = emojiEditText.realText
        set(value) {
            emojiEditText.setText(value)
        }
    val topicScope: TopicScope?
        get() = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
    val atScope: AtScope?
        get() = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }
    lateinit var emojiEditText: RichEditText
    //endregion

    var attachments: AttachmentTail? = null
    var blocks: MutableList<Block> = ArrayList()

}