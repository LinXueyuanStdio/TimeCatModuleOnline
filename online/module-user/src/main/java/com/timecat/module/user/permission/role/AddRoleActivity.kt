package com.timecat.module.user.permission.role

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
import com.timecat.data.bmob.ext.Role
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.let_Role_has_permission
import com.timecat.data.bmob.ext.net.allHunPermission
import com.timecat.data.bmob.ext.net.checkRoleExistByTitle
import com.timecat.data.bmob.ext.net.findAllHunPermission
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.RoleBlock
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
 * @description 创建角色
 * 主要是创建角色的多个混权限
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddRoleActivity)
class AddRoleActivity : BaseBlockEditorActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var role: Block? = null

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建权限角色"

    data class FormData(
        var name: String = "权限角色名",
        var content: String = "",
        var permissions: MutableList<Block> = ArrayList()
    )

    lateinit var linkContainer: ContainerItem
    lateinit var linkItem: NextItem
    lateinit var permissions: List<Block2Block>

    private fun loadPermission(block: Block) {
        requestBlockRelation {
            query = block.findAllHunPermission()
            onError = {
                mStatefulLayout?.showError {
                    loadPermission(block)
                }
            }
            onEmpty = {
                LogUtil.e("empty")
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                setPermission(it.map { it.to })
                permissions = it
                mStatefulLayout?.showContent()
            }
        }
    }

    private fun setPermission(blocks: List<Block>) {
        LogUtil.e(blocks)
        linkContainer.removeAllViews()
        formData.blocks.clear()
        for (i in blocks) {
            simplePermission(linkContainer, i)
            formData.blocks.add(i)
        }
    }

    override fun initFormView(): ViewGroup.() -> Unit = {
        formData.titleItem = OneLineInput("权限角色名", "", autoAdd = false)
        val d1 = Divider(autoAdd = false)
        linkItem = Next("关联混权限", autoAdd = false) {
            addPermission()
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

    private fun addPermission() {
        MaterialDialog(this).show {
            message(R.string.load_dialog_wait)
            lifecycleOwner(this@AddRoleActivity)
            requestBlock {
                query = allHunPermission()
                onError = {
                    dismiss()
                }
                onEmpty = {
                    ToastUtil.w("没有可分配的权限！")
                    dismiss()
                }
                onSuccess = { data ->
                    showPermissionDialog(data)
                    dismiss()
                }
            }
        }
    }

    private fun showPermissionDialog(permissions: List<Block>) {
        MaterialDialog(this, BottomSheet()).show {
            lifecycleOwner(this@AddRoleActivity)

            val initSelection = ArrayList<Int>()
            for (i in permissions) {
                for (j in formData.blocks) {
                    if (i.eq(j)) {
                        initSelection.add(permissions.indexOf(i))
                    }
                }
            }
            positiveButton(R.string.ok)
            listItemsMultiChoice(
                items = permissions.map { "${it.title}\n描述: ${it.content}" },
                initialSelection = initSelection.toTypedArray().toIntArray()
            ) { dialog, indexs, titles ->
                setPermission(permissions.filterIndexed { index, _ -> indexs.contains(index) })
            }
        }
    }

    private fun simplePermission(viewGroup: ViewGroup, permission: Block) {
        viewGroup.Next(permission.title, permission.content, null) {
            val info = "${permission.title}\n由 ${permission.user.nickName} 创建"
            ToastUtil.i(info)
        }
    }

    override fun loadFromExistingBlock(): Block.() -> Unit = {
        setTitle("编辑权限角色")
        formData.title = title
        val head = RoleBlock.fromJson(structure)
        formData.setContentScope(context, content, head.atScope, head.topicScope)
        mStatefulLayout?.showLoading()
        loadPermission(this)
    }

    override fun ok() {
        if (currentBlock() == null) {
            requestExistBlock {
                query = checkRoleExistByTitle(formData.title)
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

    override fun currentBlock(): Block? = role

    override fun subtype(): Int = 0

    override fun savableBlock(): Block = I() create Role {
        title = formData.title
        content = formData.content
    }

    override fun onSaveSuccess(it: Block) {
        savePermissionsOfRole(it)
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
    }

    override fun onUpdateSuccess(it: Block) {
        if (::permissions.isInitialized) {
            deleteBatch {
                target = permissions
                onSuccess = { _ ->
                    savePermissionsOfRole(it)
                }
                onError = errorCallback
            }
        } else {
            savePermissionsOfRole(it)
        }
    }

    private fun savePermissionsOfRole(role: Block) {
        LogUtil.e(formData.blocks)
        saveBatch {
            target = formData.blocks.map { permission ->
                I() let_Role_has_permission (role to permission)
            }
            onSuccess = {
                ToastUtil.ok("成功！")
                finish()
            }
            onError = errorCallback
        }
    }

}