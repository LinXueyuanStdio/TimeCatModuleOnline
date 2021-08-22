package com.timecat.module.user.game.task.task

import android.text.InputType
import android.view.ViewGroup
import android.widget.LinearLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form.Form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.game.allTaskRules
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.TASK_Data
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.layout.ui.business.setting.OwnCountItem
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.ext.ImageAspectRatio
import com.timecat.module.user.ext.chooseAvatar
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receiveImage
import com.timecat.module.user.game.item.showItemDialog
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
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

    @AttrValueAutowiredAnno("block")
    var task: Block? = null
    override fun title(): String = "数据任务"
    override fun routerInject() = NAV.inject(this)
    var formRewardItems: MutableMap<String, Long> = mutableMapOf()
    var formRewardListItems: List<Reward>
        get() = formRewardItems.toList().map { Reward(it.first, it.second) }
        set(value) {
            formRewardItems = mutableMapOf(*value.map {
                it.uuid to it.count
            }.toTypedArray())
        }
    var formRuleItems: MutableMap<String, Long> = mutableMapOf()
    var formRuleListItems: List<TaskRule>
        get() = formRuleItems.toList().map { TaskRule(it.first, it.second) }
        set(value) {
            formRuleItems = mutableMapOf(*value.map {
                it.where to it.targetCount
            }.toTypedArray())
        }

    lateinit var rewardsItem: NextItem
    lateinit var rewardsContainer: LinearLayout
    lateinit var rulesItem: NextItem
    lateinit var rulesContainer: LinearLayout

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = TaskBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        formData.icon = head.header.avatar
        formRewardListItems = head.rewards
        val head2 = TaskDataBlock.fromJson(head.structure)
        formRuleListItems = head2.rules
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder", autoAdd = false) {
            chooseAvatar { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.coverItem = Image("背景图", "R.drawable.ic_folder", autoAdd = false) {
            chooseImage(ImageAspectRatio.Horizon) { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.cover = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新任务", autoAdd = false)
        rulesItem = Next("任务完成条件",
            hint = formRuleItems.toString(),
            initialText = "${formRuleItems.size}",
            autoAdd = false
        ) {
            selectRuleItems()
        }
        rulesContainer = ContainerItem(context)
        rewardsItem = Next("奖励",
            hint = formRewardListItems.toString(),
            initialText = "${formRewardListItems.size}",
            autoAdd = false
        ) {
            selectRewardItems()
        }
        rewardsContainer = ContainerItem(context)

        add(
            formData.iconItem to 0,
            formData.coverItem to 1,
            formData.titleItem to 2,
            rulesItem to 3,
            rulesContainer to 4,
            rewardsItem to 5,
            rewardsContainer to 6,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        setRulesItems()
        setRewardItems()
    }

    override fun currentBlock(): Block? = task

    //region task rule
    fun setRulesItems() {
        setTaskRulesItems(formRuleListItems)
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
                    text = "${formRuleItems[rule.where]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formRuleItems[rule.where] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onCloseIconClick {
                    formRuleItems.remove(rule.where)
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
                formRuleListItems = rules
                setTaskRulesItems(rules)
                rulesItem.hint = formRuleItems.toString()
                rulesItem.text = "${formRuleItems.size}"
            }
        }
    }
    //endregion

    //region reward
    fun setRewardItems() {
        requestBlock {
            query = allItem().whereContainedIn("objectId", formRewardItems.keys)
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
                    text = "${formRewardItems[block.objectId]}"
                    inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    onTextChange = {
                        val count = it?.toLongOrNull() ?: 0L
                        formRewardItems[block.objectId] = count
                    }
                }
                closeIcon = "R.drawable.ic_close"
                onIconClick {
                    showItemDialog(block)
                }
                onCloseIconClick {
                    formRewardItems.remove(block.objectId)
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
                formRewardListItems = blocks.map { Reward(it.objectId, 1) }
                setBlockItems(blocks)
                rewardsItem.hint = formRewardItems.toString()
                rewardsItem.text = "${formRewardItems.size}"
            }
        }
    }
    //endregion

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        var h = formData.iconItem.height + formData.coverItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
    }

    override fun subtype() = TASK_Data

    override fun getHeadBlock(): TaskBlock {
        return TaskBlock(
            type = TASK_Data,
            structure = TaskDataBlock(
                formRuleListItems
            ).toJsonObject(),
            rewards = formRewardListItems,
            topicScope = formData.topicScope,
            atScope = formData.atScope,
            mediaScope = formData.attachments,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.cover,
            )
        )
    }

}