package com.timecat.module.user.permission.auth

import android.content.Intent
import android.view.ViewGroup
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.datetime.dateTimePicker
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.afollestad.vvalidator.form
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.auth_Identity
import com.timecat.data.bmob.ext.auth_Permission
import com.timecat.data.bmob.ext.auth_Role
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.bmob.saveInterAction
import com.timecat.data.bmob.ext.net.allHunPermission
import com.timecat.data.bmob.ext.net.allIdentity
import com.timecat.data.bmob.ext.net.allRole
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.form.Spinner
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseBlockEditActivity
import com.timecat.module.user.base.GO
import com.timecat.module.user.base.login.BaseLoginEditActivity
import com.timecat.module.user.permission.UserContext
import com.timecat.module.user.search.SelectActivity
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 授权
 * 当前登录用户给target用户
 * 1. 授权
 * 2. 授身份
 * 3. 授角色
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AddAuthActivity)
class AddAuthEditActivity : BaseLoginEditActivity() {
    /**
     * 给 target 授权
     */
    @AttrValueAutowiredAnno("target")
    @JvmField
    var target: User? = null

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "授权"

    data class FormData(
        var authType: AuthType = AuthType.AuthIdentity,
        var authSource: String = "",
        var activeTime: DateTime = DateTime.now(),
        var expireTime: DateTime = DateTime.now(),
    ) {
        lateinit var authBlock: Block
        lateinit var target: User
    }

    /**
     * 授权类型
     */
    enum class AuthType(var idx: Int, var type: String, var id: String) {
        AuthPermission(0, "权限", "授予权限"),
        AuthRole(1, "角色", "授予角色"),
        AuthIdentity(2, "身份", "授予身份");

        override fun toString(): String = type
    }

    val formData: FormData = FormData()
    lateinit var authThing: NextItem
    lateinit var authTarget: NextItem

    override fun addSettingItems(container: ViewGroup) {
        val I = I()
        target?.let { formData.target = it }
        container.apply {
            Next("我是", I.nickName) {
                ToastUtil.i("当前授权的发起者为 ${I.nickName}")
                GO.userDetail(I.objectId)
            }
            Next("我以", formData.authSource) {
                changeAuthSource(it)
            }
            authTarget = Next("授予", target?.nickName ?: "") {
                changeAuthTarget(it)
            }
            val spinnerData = listOf(AuthType.AuthIdentity, AuthType.AuthRole, AuthType.AuthPermission)
            val spinner = Spinner("以下", spinnerData) { data, index ->
                formData.authType = data
                authThing.title = data.id
            }
            authThing = Next(formData.authType.id, "") {
                changeAuthThing(it)
            }
            Next("生效时间", formData.activeTime.toString("yyyy-MM-dd")) {
                changeActiveTime(it)
            }
            Next("失效时间", formData.expireTime.toString("yyyy-MM-dd")) {
                changeExpireTime(it)
            }


            form {
                useRealTimeValidation(disableSubmit = true)

                submitWith(R.id.ok) {
                    ok()
                }
            }
        }
    }

    /**
     * 当前授权发起者拥有的可用于授权的权限，有 3 类
     * 1. 权限
     * 2. 角色-权限
     * 3. 身份-角色-权限
     */
    private fun changeAuthSource(nextItem: NextItem) {
        MaterialDialog(this, BottomSheet()).show {
            lifecycleOwner(this@AddAuthEditActivity)
            positiveButton(R.string.ok)
            val choice = UserContext.hunPermission.map { it.title }
            listItemsSingleChoice(items = choice, initialSelection = 0) { _, idx, title ->
                formData.authSource = title.toString()
                nextItem.text = title.toString()
            }
        }
    }

    /**
     * 更改被授权人，即权限的接受者
     */
    private fun changeAuthTarget(nextItem: NextItem) {
        val types = ArrayList<String>()
        types.add(SelectActivity.SelectType.USER.title)
        NAV.raw(this, RouterHub.SEARCH_SelectActivity)
            .withStringArrayList("types", types)
            .requestCode(SEARCH_USER)
            .navigation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SEARCH_USER) {
                val user = data?.getSerializableExtra("data") as User?
                user?.let {
                    formData.target = it
                    authTarget.text = it.nickName ?: ""
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    /**
     * 选择授予哪些权限
     * 这些权限的生命周期 不能大于 用于本次授权的权限
     * 只能选择直接创建的
     */
    private fun changeAuthThing(nextItem: NextItem) {
        val I = I()
        requestBlock {
            query = when (formData.authType) {
                AuthType.AuthPermission -> I.allHunPermission()
                AuthType.AuthRole -> I.allRole()
                AuthType.AuthIdentity -> I.allIdentity()
            }
            onError = {
                ToastUtil.e("出错")
            }
            onEmpty = {
                ToastUtil.e("没有可授权的身份")
            }
            onSuccess = {
                showAuthThing(nextItem, it)
            }
        }
    }

    /**
     * 可授权的身份/权限/角色
     */
    private fun showAuthThing(nextItem: NextItem, blocks: List<Block>) {
        MaterialDialog(this, BottomSheet()).show {
            lifecycleOwner(this@AddAuthEditActivity)
            positiveButton(R.string.ok)
            val choice = when (formData.authType) {
                AuthType.AuthPermission -> blocks.map { it.title }
                AuthType.AuthRole -> blocks.map { it.title }
                AuthType.AuthIdentity -> blocks.map { it.title }
            }
            listItemsSingleChoice(items = choice, initialSelection = 0) { _, idx, title ->
                formData.authBlock = blocks[idx]
                nextItem.text = title.toString()
            }
        }
    }

    /**
     * 激活时间
     */
    private fun changeActiveTime(nextItem: NextItem) {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            lifecycleOwner(this@AddAuthEditActivity)
            title(text = "生效时间")
            dateTimePicker(
                minDateTime = DateTime.now().toCalendar(Locale.CANADA),
                currentDateTime = formData.activeTime.toCalendar(Locale.CANADA),
                requireFutureDateTime = true,
                show24HoursView = true
            ) { _, dateTime ->
                formData.activeTime = DateTime(dateTime)
                nextItem.text = formData.activeTime.toString("yyyy-MM-dd")
            }
        }
    }

    /**
     * 过期时间
     */
    private fun changeExpireTime(nextItem: NextItem) {
        MaterialDialog(this, BottomSheet(LayoutMode.WRAP_CONTENT)).show {
            lifecycleOwner(this@AddAuthEditActivity)
            title(text = "失效时间")
            dateTimePicker(
                minDateTime = DateTime.now().toCalendar(Locale.CANADA),
                currentDateTime = formData.expireTime.toCalendar(Locale.CANADA),
                requireFutureDateTime = true,
                show24HoursView = true
            ) { _, dateTime ->
                formData.expireTime = DateTime(dateTime)
                nextItem.text = formData.expireTime.toString("yyyy-MM-dd")
            }
        }
    }

    override fun ok() {
        GlobalScope.launch(Dispatchers.IO) {
//            requestInterActionExist {
//                query = checkTopicExistByTitle(formData.title)
//                onError = {
//                    ToastUtil.e("创建失败！${it.msg}")
//                    LogUtil.e("创建失败！${it.msg}")
//                }
//                onSuccess = { exist ->
//                    if (exist) {
//                        ToastUtil.w("已存在，请修改身份名 ！")
//                    } else {
//            save()
//                    }
//                }
//            }
            save()
        }
    }

    open fun save() {
        val I = I()
        saveInterAction {
            target = when (formData.authType) {
                AuthType.AuthIdentity -> I auth_Identity (formData.authBlock to formData.target)
                AuthType.AuthRole -> I auth_Role (formData.authBlock to formData.target)
                AuthType.AuthPermission -> I auth_Permission (formData.authBlock to formData.target)
            }.apply {
                activeDateTime = formData.activeTime
                expireDateTime = formData.expireTime
            }
            onSuccess = {
                ToastUtil.ok("成功！")
                finish()
            }
            onError = {
                ToastUtil.e("创建失败！${it.msg}")
                LogUtil.e("创建失败！${it.msg}")
            }
        }
    }

    companion object {
        const val SEARCH_USER = 20
    }
}