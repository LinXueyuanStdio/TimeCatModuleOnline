package com.timecat.module.user.game.task.task

import android.text.InputType
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Task
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.game.allTaskRules
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.*
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.TASK_Data
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.layout.ui.business.setting.InputItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.timecat.module.user.game.item.showItemDialog
import com.timecat.module.user.view.item.OwnCountItem
import com.timecat.page.base.extension.simpleUIContainer
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_DataTaskEditorActivity)
class DataTaskEditorActivity : BaseTaskAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var task: Block? = null
    override fun title(): String = "数据任务"
    override fun routerInject() = NAV.inject(this)
    data class FormData(
        var icon: String = "R.drawable.ic_folder",
        var cover: String = "R.drawable.ic_folder",
        var name: String = "新建任务",
        var content: String = "",
        var items: MutableMap<String, Long> = mutableMapOf(),
        var rules: MutableMap<String, Long> = mutableMapOf(),
        var attachments: AttachmentTail? = null
    ) {
        fun setRewardListItems(items: List<Reward>) {
            this.items = mutableMapOf(*items.map {
                it.uuid to it.count
            }.toTypedArray())
        }

        fun getRewardListItems(): MutableList<Reward> {
            return items.toList().map { Reward(it.first, it.second) }.toMutableList()
        }

        fun setRuleListItems(items: List<TaskRule>) {
            this.rules = mutableMapOf(*items.map {
                it.where to it.targetCount
            }.toTypedArray())
        }

        fun getRuleListItems(): MutableList<TaskRule> {
            return rules.toList().map { TaskRule(it.first, it.second) }.toMutableList()
        }
    }

    val formData: FormData = FormData()
    lateinit var imageItem: ImageItem
    lateinit var coverItem: ImageItem
    lateinit var titleItem: InputItem
    lateinit var rewardsItem: NextItem
    lateinit var rewardsContainer: LinearLayout
    lateinit var rulesItem: NextItem
    lateinit var rulesContainer: LinearLayout

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        task?.let {
            formData.name = it.title
            formData.content = it.content
            val head = TaskBlock.fromJson(it.structure)
            formData.attachments = head.mediaScope
            formData.icon = head.header.avatar
            formData.setRewardListItems(head.rewards)
            val head2 = TaskDataBlock.fromJson(head.structure)
            formData.setRuleListItems(head2.rules)
        }
        emojiEditText.setText(formData.content)
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

            rulesItem = Next("任务完成条件",
                hint = formData.rules.toString(),
                initialText = "${formData.rules.size}") {
                selectRuleItems()
            }
            rulesContainer = simpleUIContainer(windowContext)
            container.addView(rulesContainer)
            setRulesItems()

            rewardsItem = Next("奖励",
                hint = formData.items.toString(),
                initialText = "${formData.items.size}") {
                selectRewardItems()
            }
            rewardsContainer = simpleUIContainer(windowContext)
            container.addView(rewardsContainer)
            setRewardItems()

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

    //region task rule
    fun setRulesItems() {
        setTaskRulesItems(formData.getRuleListItems())
    }
    fun setTaskRulesItems(items: List<TaskRule>) {
        rulesContainer.removeAllViews()
        for (rule in items) {
            val itemView = OwnCountItem(this).apply {
                icon = IconLoader.randomAvatar(rule.where)
                left_field = {
                    hint = "条件"
                    text = rule.where
                    inputEditText.isEnabled = false
                }
                right_field = {
                    hint = "数量"
                    text = "${formData.rules[rule.where]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formData.rules[rule.where] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onCloseIconClick {
                    formData.rules.remove(rule.where)
                    rulesContainer.removeView(this)
                }
            }
            rulesContainer.addView(itemView)
        }
    }

    fun selectRuleItems() {
        MaterialDialog(this, BottomSheet()).show {
            title(text = "选择任务条件")
            positiveButton(R.string.ok)
            val texts = allTaskRules()
            listItemsMultiChoice(items = texts) { _, intArr, _ ->
                val blocks = texts.filterIndexed { index, _ -> index in intArr }
                val rules = blocks.map { TaskRule(it, 1) }
                formData.setRuleListItems(rules)
                setTaskRulesItems(rules)
                rulesItem.hint = formData.rules.toString()
                rulesItem.text = "${formData.rules.size}"
            }
        }
    }
    //endregion

    //region reward
    fun setRewardItems() {
        requestBlock {
            query = allItem().whereContainedIn("objectId", formData.items.keys)
            onSuccess = {
                setBlockItems(it)
            }
        }
    }

    fun setBlockItems(items: List<Block>) {
        LogUtil.e(items)
        rewardsContainer.removeAllViews()
        for (block in items) {
            val head = ItemBlock.fromJson(block.structure)
            val itemView = OwnCountItem(this).apply {
                icon = head.header.icon
                left_field = {
                    hint = "物品"
                    text = block.title
                    inputEditText.isEnabled = false
                }
                right_field = {
                    hint = "数量"
                    text = "${formData.items[block.objectId]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formData.items[block.objectId] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onIconClick {
                    showItemDialog(block)
                }
                onCloseIconClick {
                    formData.items.remove(block.objectId)
                    rewardsContainer.removeView(this)
                }
            }
            rewardsContainer.addView(itemView)
        }
    }

    fun selectRewardItems() {
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
            title(text = "选择物品")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsMultiChoice(items = texts) { _, intArr, _ ->
                val blocks = items.filterIndexed { index, block -> index in intArr }
                formData.setRewardListItems(blocks.map { Reward(it.objectId, 1) })
                setBlockItems(blocks)
                rewardsItem.hint = formData.items.toString()
                rewardsItem.text = "${formData.items.size}"
            }
        }
    }
    //endregion

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            titleItem.inputEditText.hasFocus() -> imageItem.height + coverItem.height
            emojiEditText.hasFocus() -> imageItem.height + coverItem.height + titleItem.height
            else -> 0
        }
    }

    //region save
    override fun publish(content: String, attachments: AttachmentTail?) {
        formData.content = content
        formData.attachments = attachments
        ok()
    }

    protected fun ok() {
        if (task == null) {
            save()
        } else {
            update()
        }
    }

    fun subtype() = TASK_Data
    fun getHeadBlock(): TaskBlock {
        val topicScope = emojiEditText.realTopicList.map {
            TopicItem(it.topicName, it.topicId)
        }.ifEmpty { null }?.let { TopicScope(it.toMutableList()) }
        val atScope = emojiEditText.realUserList.map {
            AtItem(it.user_name, it.user_id)
        }.ifEmpty { null }?.let { AtScope(it.toMutableList()) }
        return TaskBlock(
            type = TASK_Data,
            structure = TaskDataBlock(
                formData.getRuleListItems()
            ).toJsonObject(),
            rewards = formData.getRewardListItems(),
            topicScope = topicScope,
            atScope = atScope,
            mediaScope = formData.attachments,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.cover,
            )
        )
    }

    fun update() {
        task?.let {
            updateBlock {
                target = it.apply {
                    title = formData.name
                    content = formData.content
                    subtype = subtype()
                    structure = getHeadBlock().toJson()
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
            target = I() create Task {
                title = formData.name
                content = formData.content
                subtype = subtype()
                headerBlock = getHeadBlock()
            }
            onSuccess = {
                ToastUtil.ok("成功！")
                finish()
            }
            onError = errorCallback
        }
    }
    //endregion

}