package com.timecat.module.user.game.task.task

import android.text.InputType
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Task
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.TASK_Data
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_DataTaskEditorActivity)
class DataTaskEditorActivity : BaseTaskAddActivity() {

    override fun title(): String = "数据任务"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var cover: String = "R.drawable.ic_folder",
        var name: String = "新建任务",
        var content: String = "",
        var where: String = "",
        var targetNum: Long = 0,
        var rewards: List<Reward> = mutableListOf(),
        var attachments: AttachmentTail? = null
    )

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var coverItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var whereItem: InputItem
    lateinit var numItem: InputItem
    lateinit var rewardsItem: NextItem
    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        MaterialForm(this, container).apply {
            imageItem = ImageItem(windowContext).apply {
                title = "图标"
                setImage(formData.icon)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        receieveImage(I(), listOf(path), false) {
                            formData.icon = it.first()
                            imageItem.setImage(formData.icon)
                        }
                    }
                }

                container.addView(this, 0)
            }
            coverItem = ImageItem(windowContext).apply {
                title = "背景图"
                setImage(formData.cover)
                onClick {
                    chooseImage(isAvatar = true) { path ->
                        receieveImage(I(), listOf(path), false) {
                            formData.cover = it.first()
                            coverItem.setImage(formData.cover)
                        }
                    }
                }

                container.addView(this, 1)
            }
            titleItem = InputItem(windowContext).apply {
                hint = "名称"
                text = formData.name
                onTextChange = {
                    formData.name = it ?: ""
                }

                container.addView(this, 2)
            }
            whereItem = InputItem(windowContext).apply {
                hint = "字段名称"
                text = formData.where
                onTextChange = {
                    formData.where = it ?: ""
                }

                container.addView(this, 3)
            }
            numItem = InputItem(windowContext).apply {
                hint = "数量"
                text = "${formData.targetNum}"
                onTextChange = {
                    formData.targetNum = it?.toLong() ?: 0
                }
                inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED

                container.addView(this, 4)
            }
            rewardsItem = Next("奖励",
                hint = formData.rewards.toString(),
                initialText = "${formData.rewards.size}") {
                selectItems()
            }
            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }
                inputLayout(whereItem.inputLayout) {
                    isNotEmpty().description("请输入字段名称!")
                }
                inputLayout(numItem.inputLayout) {
                    isNotEmpty().description("请输入数量!")
                }

                submitWith(R.id.ok) { result ->
                    publish()
                }
            }
        }
    }

    fun selectItems() {
        mStatefulLayout?.showLoading()
        requestBlock {
            query = allItem()
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
            title(text = "选择物品放入礼包中")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsMultiChoice(items = texts) { _, intArr, _ ->
                val blocks = items.filterIndexed { index, block -> index in intArr }
                formData.rewards = blocks.map { Reward(it.objectId, 1) }
                rewardsItem.hint = formData.rewards.toString()
                rewardsItem.text = "${formData.rewards.size}"
            }
        }
    }

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            titleItem.inputEditText.hasFocus() -> imageItem.height + coverItem.height
            whereItem.inputEditText.hasFocus() -> imageItem.height + coverItem.height + titleItem.height
            numItem.inputEditText.hasFocus() -> imageItem.height + coverItem.height + titleItem.height + whereItem.height
            emojiEditText.hasFocus() -> imageItem.height + coverItem.height + titleItem.height + whereItem.height + numItem.height
            else -> 0
        }
    }

    override fun publish(content: String, attachments: AttachmentTail?) {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        save()
    }

    open fun save() {
        saveBlock {
            target = I() create Task {
                title = formData.name
                content = formData.content
                subtype = TASK_Data
                headerBlock = TaskBlock(
                    type = TASK_Data,
                    structure = TaskDataBlock(
                        listOf(TaskRule(formData.where,
                            formData.targetNum))
                    ).toJson(),
                    mediaScope = formData.attachments,
                    topicScope = TopicScope(emojiEditText.realTopicList.map {
                        TopicItem(it.topicName, it.topicId)
                    }.toMutableList()),
                    atScope = AtScope(emojiEditText.realUserList.map {
                        AtItem(it.user_name, it.user_id)
                    }.toMutableList()),
                    header = PageHeader(
                        icon = formData.icon,
                        avatar = formData.icon,
                        cover = formData.cover,
                    )
                )
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = errorCallback
        }
    }
}