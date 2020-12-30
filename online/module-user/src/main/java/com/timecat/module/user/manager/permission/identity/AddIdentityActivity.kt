package com.timecat.module.user.manager.permission.identity

import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.afollestad.vvalidator.form
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.data.bmob.ext.Identity
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.let_Identity_has_role
import com.timecat.data.bmob.ext.net.allRole
import com.timecat.data.bmob.ext.net.checkIdentityExistByTitle
import com.timecat.data.bmob.ext.net.findAllRole
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.middle.setting.BaseNewActivity
import com.timecat.module.user.R
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.base.BaseBlockEditActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 创建身份
 * 主要是身份和一个或多个默认角色绑定
 * 身份还可以额外挂接多个临时角色
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddIdentityActivity)
class AddIdentityActivity : BaseBlockEditActivity() {
    @AttrValueAutowiredAnno("block")
    @JvmField
    var block: Block? = null

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "创建身份"

    data class FormData(
        var name: String = "身份名",
        var content: String = "",
        var roles: MutableList<Block> = ArrayList()
    )

    val formData: FormData = FormData()
    lateinit var c: ContainerItem
    var roles: List<Block2Block> = ArrayList()
        get() = field
        set(value) {
            c.removeAllViews()
            formData.roles.clear()
            for (i in value) {
                simpleRole(c, i.to)
                formData.roles.add(i.to)
            }
            field = value
        }

    private fun loadRole(block: Block) {
        requestBlockRelation {
            query = block.findAllRole()
            onError = {
                mStatefulLayout?.showError {
                    loadRole(block)
                }
            }
            onEmpty = {
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                roles = listOf(it)
                mStatefulLayout?.showContent()
            }
            onListSuccess = {
                roles = it
                mStatefulLayout?.showContent()
            }
        }
    }

    override fun addSettingItems(container: ViewGroup) {
        block?.let {
            formData.name = it.title
            formData.content = it.content
            mStatefulLayout?.showLoading()
            loadRole(it)
        }
        MaterialForm(this, container).apply {
            val titleItem = OneLineInput("身份名", formData.name) {
                formData.name = it ?: ""
            }
            MultiLineInput("身份描述", formData.content) {
                formData.content = it ?: ""
            }

            Divider()
            Next("关联角色") {
                addRole()
            }
            c = simpleContainer(container)
            Divider()

            form {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("身份名")
                }

                submitWith(R.id.ok) { result ->
                    btnOk.isEnabled = false
                    ok()
                }
            }
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
                    showAddRoleDialog(listOf(data))
                    dismiss()
                }
                onListSuccess = { data ->
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
                for (j in formData.roles) {
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
                formData.roles.clear()
                c.removeAllViews()
                for (i in indexs) {
                    val p = roles[i]
                    formData.roles.add(p)
                    simpleRole(c, p)
                }
            }
        }
    }

    private fun simpleRole(viewGroup: ViewGroup, role: Block) {
        simpleNext(viewGroup, role.title, role.content, null) {
            NAV.go(this, RouterHub.USER_AddRoleActivity, "block", role)
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
                    onSuccess = { block ->
                        LogUtil.e(roles)
                        deleteThenInsertBatch {
                            delete = roles.map { role->
                                role.copy().also { it.objectId = block.objectId }
                            }
                            insert = formData.roles.map { role ->
                                I() let_Identity_has_role (block to role)
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
                    onError = {
                        btnOk.isEnabled = true
                        ToastUtil.e("创建失败！${it.msg}")
                        LogUtil.e("创建失败！${it.msg}")
                    }
                }
            } else {
                requestExist {
                    query = checkIdentityExistByTitle(formData.name)
                    onError = {
                        btnOk.isEnabled = true
                        ToastUtil.e("创建失败！${it.msg}")
                        LogUtil.e("创建失败！${it.msg}")
                    }
                    onSuccess = { exist ->
                        if (exist) {
                            btnOk.isEnabled = true
                            ToastUtil.w("已存在，请修改身份名 ！")
                        } else {
                            save()
                        }
                    }
                }
            }
        }
    }

    open fun save() {
        saveBlock {
            target = I() create Identity {
                title = formData.name
                content = formData.content
            }
            onSuccess = {
                saveBatch {
                    target = formData.roles.map { role ->
                        I() let_Identity_has_role (it to role)
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
            onError = {
                btnOk.isEnabled = true
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }
}