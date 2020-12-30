package com.timecat.module.user.manager.permission.role

import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.data.bmob.ext.Role
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.let_Role_has_permission
import com.timecat.data.bmob.ext.net.allHunPermission
import com.timecat.data.bmob.ext.net.checkRoleExistByTitle
import com.timecat.data.bmob.ext.net.findAllHunPermission
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 创建角色
 * 主要是创建角色的多个混权限
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddRoleActivity)
class AddRoleActivity : BaseBlockEditActivity() {

    @AttrValueAutowiredAnno("block")
    @JvmField
    var block: Block? = null

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建权限角色"

    data class FormData(
        var name: String = "权限角色名",
        var content: String = "",
        var permissions: MutableList<Block> = ArrayList()
    )

    val formData: FormData = FormData()
    lateinit var c: ContainerItem
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
                setPermission(listOf(it.to))
                permissions = listOf(it)
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                setPermission(it.map { it.to })
                permissions = it
                mStatefulLayout?.showContent()
            }
        }
    }

    private fun setPermission(blocks: List<Block>) {
        LogUtil.e(blocks)
        c.removeAllViews()
        formData.permissions.clear()
        for (i in blocks) {
            simplePermission(c, i)
            formData.permissions.add(i)
        }
    }

    override fun addSettingItems(container: ViewGroup) {
        block?.let {
            setTitle("编辑权限角色")
            formData.name = it.title
            formData.content = it.content
            mStatefulLayout?.showLoading()
            loadPermission(it)
        }
        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("权限角色名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("描述", formData.content) {
                formData.content = it ?: ""
            }

            Divider()
            Next("关联混权限") {
                addPermission()
            }
            c = simpleContainer(container)
            Divider()

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("权限角色名")
                }

                submitWith(R.id.ok) {
                    btnOk.isEnabled = false
                    ok()
                }
            }
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
                    showPermissionDialog(listOf(data))
                    dismiss()
                }
                onListSuccess = { data ->
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
                for (j in formData.permissions) {
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
        simpleNext(viewGroup, permission.title, permission.content, null) {
            val info = "${permission.title}\n由 ${permission.user.nick} 创建"
            ToastUtil.i(info)
        }
    }

    override fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
            if (block != null) {
                updateBlock {
                    target = block!!.apply {
                        title = formData.name
                        content = formData.content
                    }
                    onSuccess = { role ->
                        if (::permissions.isInitialized) {
                            deleteBatch {
                                target = permissions
                                onSuccess = {
                                    savePermissionsOfRole(role)
                                }
                                onError = {
                                    btnOk.isEnabled = true
                                    ToastUtil.e("创建失败！${it.msg}")
                                    LogUtil.e("创建失败！${it.msg}")
                                }
                            }
                        } else {
                            savePermissionsOfRole(role)
                        }
                    }
                    onError = {
                        btnOk.isEnabled = true
                        ToastUtil.e("创建失败！${it.msg}")
                        LogUtil.e("创建失败！${it.msg}")
                    }
                }
            } else {
                requestExist {
                    query = checkRoleExistByTitle(formData.name)
                    onError = {
                        btnOk.isEnabled = true
                        ToastUtil.e("创建失败！${it.msg}")
                        LogUtil.e("创建失败！${it.msg}")
                    }
                    onSuccess = { exist ->
                        if (exist) {
                            btnOk.isEnabled = true
                            ToastUtil.w("已存在，请修改权限角色名 ！")
                        } else {
                            save()
                        }
                    }
                }
            }
        }
    }

    private fun savePermissionsOfRole(role:Block) {
        LogUtil.e(formData.permissions)
        saveBatch {
            target = formData.permissions.map { permission ->
                I() let_Role_has_permission (role to permission)
            }
            onSuccess = {
                ToastUtil.ok("创建成功！")
                finish()
            }
            onError = {
                btnOk.isEnabled = true
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create Role {
                title = formData.name
                content = formData.content
            }
            onSuccess = {
                savePermissionsOfRole(it)
            }
            onError = {
                btnOk.isEnabled = true
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}