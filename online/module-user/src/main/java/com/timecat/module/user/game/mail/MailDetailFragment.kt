package com.timecat.module.user.game.mail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import cn.leancloud.AVCloud
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.data.bmob.ext.bmob.deleteOwnMail
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.MailBlock
import com.timecat.identity.data.block.Reward
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.Body
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.layout.dp
import com.timecat.module.user.R
import com.timecat.module.user.game.item.BigTitle
import com.timecat.module.user.game.item.RewardList
import com.timecat.module.user.game.item.buildRewardListItem
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_MailDetailFragment)
class MailDetailFragment : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("ownMail")
    var _ownMail: OwnMail? = null
    lateinit var ownMail: OwnMail

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        ownMail = _ownMail!!
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    fun buildView(context: Context): View {
        val container = ContainerItem(context)
        container.apply {
            val mail = ownMail.mail
            BigTitle(mail.title)
            Body(mail.content)

            val head = MailBlock.fromJson(mail.structure)
            if (head.rewards.isEmpty()) {
                DeleteMail()
            } else {
                val items = head.rewards
                RewardList(requireActivity(), items)

                if (ownMail.receive) {
                    DeleteMail()
                } else {
                    ReceiveMail(items)
                }
            }
        }
        return container
    }

    private fun ViewGroup.DeleteMail() {
        MaterialButton("删除") { button ->
            deleteOwnMail {
                target = ownMail
                onSuccess = {
                    dismiss()
                }
                onError = {
                    ToastUtil.e_long("删除失败 ${it.msg}")
                }
            }
        }
    }

    private fun ViewGroup.ReceiveMail(rewards: List<Reward>) {
        MaterialButton("领取") { button ->
            button.isEnabled = false
            val params = mutableMapOf<String, Any>()
            params["ownMailId"] = ownMail.objectId
            AVCloud.callFunctionInBackground<Any?>("readMail", params).subscribe({
                MaterialDialog(requireActivity()).show {
                    title(text = "获得了")
                    val view = buildRewardListItem(requireActivity(), rewards)
                    view.setPadding(10.dp)
                    customView(view = view)
                    positiveButton(R.string.ok)
                    this@MailDetailFragment.dismiss()
                }
            }, {
                button.isEnabled = true
                errUsingItem(it)
            })
        }
    }

    private fun errUsingItem(err: Throwable) {
        ToastUtil.e_long("出现错误：$err")
    }
}