package com.timecat.module.user.game.mail

import android.content.Intent
import android.view.ViewGroup
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.saveOwnMail
import com.timecat.data.bmob.ext.game.ownMail
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Next
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.module.user.base.login.BaseLoginEditActivity
import com.timecat.module.user.search.SelectActivity
import com.timecat.module.user.view.item.MaterialForm
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/15
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_SendMailActivity)
class SendMailActivity : BaseLoginEditActivity() {
    /**
     * 要发送的邮件
     */
    @AttrValueAutowiredAnno("block")
    @JvmField
    var mail: Block? = null

    override fun title(): String = "发送邮件"
    override fun routerInject() = NAV.inject(this)
    lateinit var formData: MaterialForm
    override fun addSettingItems(container: ViewGroup) {
        formData = MaterialForm()
        container.apply {
            formData.blockItem = Next("邮件") {
                selectBlock(it)
            }
            formData.userItem = Next("用户") {
                selectUser(it)
            }
        }
        formData.block = mail
    }

    override fun ok() {
        saveOwnMail {
            target = ownMail(formData.user!!, formData.block!!)
            onSuccess = {
                ToastUtil.ok("成功")
                finish()
            }
            onError = {
                ToastUtil.e_long("失败：$it")
            }
        }
    }

    private fun selectBlock(nextItem: NextItem) {
        val types = ArrayList<String>()
        types.add(SelectActivity.SelectType.Mail.title)
        NAV.raw(this, RouterHub.SEARCH_SelectActivity)
            .withStringArrayList("types", types)
            .requestCode(SEARCH_MAIL)
            .navigation()
    }

    private fun selectUser(nextItem: NextItem) {
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
                val user = data?.getParcelableExtra("data") as User?
                user?.let {
                    formData.user = it
                }
            } else if (requestCode == SEARCH_MAIL) {
                val block = data?.getParcelableExtra("data") as Block?
                block?.let {
                    formData.block = it
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val SEARCH_USER = 20
        const val SEARCH_MAIL = 21
    }
}