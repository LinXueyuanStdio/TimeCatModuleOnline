package com.timecat.module.user.permission.identity

import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form.Form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.data.bmob.ext.*
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.net.*
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.IdentityBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.*
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 创建身份==创建方块
 * 主要是身份和一个或多个默认角色绑定
 * 身份还可以额外挂接多个临时角色
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddIdentityActivity)
class AddIdentityActivity : BaseBlockEditorActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var identity: Block? = null

    override fun title(): String = "创建方块"
    override fun routerInject() = NAV.inject(this)

    lateinit var linkContainer: ContainerItem
    lateinit var linkItem: NextItem
    lateinit var roles: List<Block2Block>

    private fun loadRoles(block: Block) {
        requestBlockRelation {
            query = block.findAllRole()
            onError = {
                mStatefulLayout?.showError {
                    loadRoles(block)
                }
            }
            onEmpty = {
                LogUtil.e("empty")
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                setRole(it.map { it.to })
                roles = it
                mStatefulLayout?.showContent()
            }
        }
    }

    private fun setRole(blocks: List<Block>) {
        LogUtil.e(blocks)
        linkContainer.removeAllViews()
        formData.blocks.clear()
        for (i in blocks) {
            simpleRole(linkContainer, i)
            formData.blocks.add(i)
        }
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.titleItem = OneLineInput("名称", "", autoAdd = false)
        val d1 = Divider(autoAdd = false)
        linkItem = Next("关联角色", autoAdd = false) {
            addRole()
        }
        linkContainer = VerticalContainer(autoAdd = false) {}
        val d2 = Divider(autoAdd = false)
        add(
            formData.titleItem to 0,
            d1 to 1,
            linkItem to 2,
            linkContainer to 3,
            d2 to 4,
        )
    }

    override fun validator(): Form.() -> Unit = {
        inputLayout(formData.titleItem.inputLayout) {
            isNotEmpty().description("权限角色名")
        }
    }

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        setTitle("编辑方块")
        formData.title = title
        val head = IdentityBlock.fromJson(structure)
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        mStatefulLayout?.showLoading()
        loadRoles(this)
    }

    override fun ok() {
        if (currentBlock() == null) {
            requestExistBlock {
                query = checkIdentityExistByTitle(formData.title)
                onError = errorCallback
                onSuccess = { exist ->
                    if (exist) {
                        unlockEverythingWhenPublish()
                        ToastUtil.w("已存在，请修改名称 ！")
                    } else {
                        save()
                    }
                }
            }
        } else {
            update()
        }
    }

    override fun currentBlock(): Block? = identity

    override fun subtype(): Int = 0

    override fun savableBlock(): Block = I() create Identity {
        title = formData.title
        content = formData.content
        headerBlock = getHeadBlock()
    }

    override fun onSaveSuccess(it: Block) {
        saveRoleOfIdentity(it)
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
        structure = getHeadBlock().toJson()
    }

    fun getHeadBlock():IdentityBlock {
        return IdentityBlock()
    }

    override fun onUpdateSuccess(it: Block) {
        if (::roles.isInitialized) {
            deleteBatch {
                target = roles
                onSuccess = { _ ->
                    saveRoleOfIdentity(it)
                }
                onError = errorCallback
            }
        } else {
            saveRoleOfIdentity(it)
        }
    }

    private fun saveRoleOfIdentity(identity: Block) {
        LogUtil.e(formData.blocks)
        saveBatch {
            target = formData.blocks.map { role ->
                I() let_Identity_has_role (identity to role)
            }
            onSuccess = {
                ToastUtil.ok("成功！")
                finish()
            }
            onError = errorCallback
        }
    }

    private fun addRole() {
        MaterialDialog(this).show {
            lifecycleOwner(this@AddIdentityActivity)
            message(R.string.load_dialog_wait)
            requestBlock {
                query = allRole()
                onError = {
                    dismiss()
                }
                onEmpty = {
                    ToastUtil.w("没有可分配的角色！")
                    dismiss()
                }
                onSuccess = { data ->
                    showAddRoleDialog(data)
                    dismiss()
                }
            }
        }
    }

    private fun showAddRoleDialog(roles: List<Block>) {
        MaterialDialog(this, BottomSheet()).show {
            lifecycleOwner(this@AddIdentityActivity)

            val initSelection = ArrayList<Int>()
            for (i in roles) {
                for (j in formData.blocks) {
                    if (i.eq(j)) {
                        initSelection.add(roles.indexOf(i))
                    }
                }
            }
            positiveButton(R.string.ok)
            listItemsMultiChoice(
                items = roles.map { "${it.title}\n描述: ${it.content}" },
                initialSelection = initSelection.toTypedArray().toIntArray()
            ) { dialog, indexs, titles ->
                formData.blocks.clear()
                linkContainer.removeAllViews()
                for (i in indexs) {
                    val p = roles[i]
                    formData.blocks.add(p)
                    simpleRole(linkContainer, p)
                }
            }
        }
    }

    private fun simpleRole(viewGroup: ViewGroup, role: Block) {
        viewGroup.Next(role.title, role.content) {
            NAV.go(this, RouterHub.USER_AddRoleActivity, "block", role)
        }
    }
}