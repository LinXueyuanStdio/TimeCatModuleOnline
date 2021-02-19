package com.timecat.module.user.social.topic

import android.view.ViewGroup
import com.afollestad.vvalidator.form.Form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.Topic
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkTopicExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.NoteBody
import com.timecat.identity.data.base.PageHeader
import com.timecat.identity.data.block.TopicBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Image
import com.timecat.layout.ui.business.form.OneLineInput
import com.timecat.layout.ui.business.form.add
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.receieveImage
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description 创建话题
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddTopicActivity)
open class AddTopicActivity : BaseBlockEditorActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var item: Block? = null

    override fun title(): String = "创建话题"
    override fun routerInject() = NAV.inject(this)
    override fun loadFromExistingBlock(): Block.() -> Unit = {
        formData.title = title
        val head = TopicBlock.fromJson(structure)
        formData.icon = head.header?.icon ?: "R.drawable.ic_folder"
        formData.setContentScope(context, content, head.atScope, head.topicScope)
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.iconItem = Image("图标", "R.drawable.ic_folder", autoAdd = false) {
            chooseImage(isAvatar = true) { path ->
                receieveImage(I(), listOf(path), false) {
                    formData.icon = it.first()
                }
            }
        }
        formData.titleItem = OneLineInput("标题", "新建话题", autoAdd = false)

        add(
            formData.iconItem to 0,
            formData.titleItem to 1,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("请输入名称!")
        }
    }

    override fun currentBlock(): Block? = item

    override fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        var h = formData.iconItem.height
        if (formData.titleItem.inputEditText.hasFocus()) return h
        h += formData.titleItem.height
        if (emojiEditText.hasFocus()) return h
        return 0
    }

    override fun savableBlock(): Block = I() create Topic {
        title = formData.title
        content = formData.content
        headerBlock = getHeadBlock()
    }

    fun getHeadBlock(): TopicBlock {
        return TopicBlock(
            content = NoteBody(),
            atScope = formData.atScope,
            topicScope = formData.topicScope,
            header = PageHeader(
                icon = formData.icon,
                avatar = formData.icon,
                cover = formData.icon,
            )
        )
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
        structure = getHeadBlock().toJson()
    }

    override fun subtype() = 0

    override fun ok() {
        if (currentBlock() == null) {
            requestExistBlock {
                query = checkTopicExistByTitle(formData.title)
                onError = {
                    ToastUtil.e("创建失败！${it.msg}")
                    LogUtil.e("创建失败！${it.msg}")
                }
                onSuccess = { exist ->
                    if (exist) {
                        ToastUtil.w("已存在，请修改名称！")
                    } else {
                        save()
                    }
                }
            }
        } else {
            update()
        }
    }
}