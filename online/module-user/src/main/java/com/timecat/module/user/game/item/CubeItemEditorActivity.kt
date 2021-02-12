package com.timecat.module.user.game.item

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Item
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.allIdentity
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.ITEM_Cube
import com.timecat.identity.data.block.type.ITEM_Package
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-13
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeItemEditorActivity)
class CubeItemEditorActivity : BaseItemAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var item: Block? = null
    override fun title(): String = "方块"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var name: String = "新建方块",
        var content: String = "",
        var uuid: String = "",
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var cubeItem: NextItem
    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        item?.let {
            formData.name = it.title
            formData.content = it.content
            val head = ItemBlock.fromJson(it.structure)
            formData.attachments = head.mediaScope
            formData.icon = head.header.avatar
        }
        emojiEditText.setText(formData.content)
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "图标"
                setImage(formData.icon)
                container.addView(this, 0)
            }
            titleItem = InputItem(windowContext).apply {
                hint = "名称"
                text = formData.name
                inputEditText.isEnabled = false
                container.addView(this, 1)
            }

            cubeItem = Next("方块", hint = formData.uuid, initialText = formData.uuid) {
                chooseCube()
            }

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }

                submitWith(R.id.ok) { result ->
                    publish()
                }
            }
        }
    }

    fun chooseCube() {
        mStatefulLayout?.showLoading()
        requestBlock {
            query = allIdentity()
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e_long(it.msg)
            }
            onEmpty = {
                mStatefulLayout?.showContent()
                ToastUtil.w("空")
            }
            onComplete = {
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                showSelectDialog(it)
            }
        }
    }

    fun showSelectDialog(items: List<Block>) {
        MaterialDialog(this, BottomSheet()).show {
            title(text = "选择方块")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsSingleChoice(items = texts) { _, idx, _ ->
                val cube = items[idx]
                formData.name = cube.title
                formData.uuid = cube.objectId
                formData.content = cube.content
                titleItem.text = cube.title
                emojiEditText.setText(cube.content)
            }
        }
    }

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            titleItem.inputEditText.hasFocus() -> imageItem.height
            emojiEditText.hasFocus() -> imageItem.height + titleItem.height
            else -> 0
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        if (item == null) {
            save()
        } else {
            update()
        }
    }
    fun subtype() = ITEM_Cube
    fun getItemBlock(): ItemBlock {
        val topicScope = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
        val atScope = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }
        return ItemBlock(
            type = subtype(),
            structure =CubeItemBlock(formData.uuid).toJson(),
            mediaScope = formData.attachments,
            topicScope = topicScope,
            atScope = atScope,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            )
        )
    }

    fun update() {
        item?.let {
            updateBlock {
                target = it.apply {
                    title = formData.name
                    content = formData.content
                    subtype = subtype()
                    structure = getItemBlock().toJson()
                }
                onSuccess = {
                    ToastUtil.ok("更新成功！")
                    finish()
                }
                onError = errorCallback
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create Item {
                title = formData.name
                content = formData.content
                subtype = subtype()
                headerBlock = getItemBlock()
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = errorCallback
        }
    }
}