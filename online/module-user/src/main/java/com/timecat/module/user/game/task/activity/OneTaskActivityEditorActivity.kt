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
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.ACTIVITY_One_task
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.*
import com.timecat.module.user.R
import com.timecat.module.user.ext.ImageAspectRatio
import com.timecat.module.user.ext.chooseAvatar
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receiveImage
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
    var task: Block? = null
    override fun title(): String = "一个任务活动"
    override fun routerInject() = NAV.inject(this)

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = ItemBlock.fromJson(structure)
        formData.attachments = head.mediaScope
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        formData.icon = head.header.avatar
        formData.cover = head.header.cover
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
            chooseImage(ImageAspectRatio.Wallpaper) { path ->
                receiveImage(I(), listOf(path), false) {
                    formData.cover = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建活动", autoAdd = false)
        formData.blockItem = Next("任务", autoAdd = false) {
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
            title(text = "选择任务")
            positiveButton(R.string.ok)
            val texts = items.map { it.title }
            listItemsSingleChoice(items = texts) { _, idx, _ ->
                val task = items[idx]
                formData.block = task
            }
        }
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
        next(formData.blockItem, "任务") {
            isNotEmpty().description("必须选择一个任务!")
        }
    }

    override fun currentBlock(): Block? = task

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        var h = formData.iconItem.height + formData.coverItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
    }

    override fun subtype(): Int = ACTIVITY_One_task
    override fun getItemBlock(): ActivityBlock {
        return ActivityBlock(
            type = subtype(),
            structure = ActivityOneTaskBlock(formData.blockId).toJsonObject(),
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