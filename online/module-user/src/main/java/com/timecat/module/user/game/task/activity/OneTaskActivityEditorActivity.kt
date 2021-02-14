package com.timecat.module.user.game.task.activity

import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.afollestad.vvalidator.form.Form
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allTask
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.ActivityBlock
import com.timecat.identity.data.block.ActivityOneTaskBlock
import com.timecat.identity.data.block.ItemBlock
import com.timecat.identity.data.block.type.ACTIVITY_One_task
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.*
import com.timecat.module.user.R
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_OneTaskActivityEditorActivity)
class OneTaskActivityEditorActivity : BaseActivityAddActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var task: Block? = null
    override fun title(): String = "一个任务活动"
    override fun routerInject() = NAV.inject(this)

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        formData.content = content
        val head = ItemBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.icon = head.header.avatar
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder") {
            chooseImage(isAvatar = true) { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.coverItem = Image("背景图", "R.drawable.ic_folder") {
            chooseImage(isAvatar = false) { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.cover = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建活动")
        formData.blockItem = Next("任务") {
            chooseTask()
        }

        add(
            formData.iconItem to 0,
            formData.coverItem to 1,
            formData.titleItem to 2,
            formData.blockItem to 3,
        )
    }

    fun chooseTask() {
        mStatefulLayout?.showLoading()
        requestBlock {
            query = allTask()
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
                formData.title = cube.title
                formData.uuid = cube.objectId
                formData.content = cube.content
            }
        }
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
        next(formData.blockItem) {
            isNotEmpty().description("必须选择一个任务!")
        }
    }

    override fun currentBlock(): Block? = task

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return when {
            formData.titleItem.inputEditText.hasFocus() -> formData.iconItem.height + formData.coverItem.height
            formData.urlItem.inputEditText.hasFocus() -> formData.iconItem.height + formData.coverItem.height + formData.titleItem.height
            emojiEditText.hasFocus() -> formData.iconItem.height + formData.coverItem.height + formData.titleItem.height + formData.urlItem.height
            else -> 0
        }
    }

    override fun subtype(): Int = ACTIVITY_One_task
    override fun getItemBlock(): ActivityBlock {
        return ActivityBlock(
            type = subtype(),
            structure = ActivityOneTaskBlock(formData.uuid).toJsonObject(),
            mediaScope = formData.attachments,
            topicScope = formData.topicScope,
            atScope = formData.atScope,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.cover,
            )
        )
    }
}