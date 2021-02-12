package com.timecat.module.user.game.mail

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import cn.leancloud.AVCloud
import com.alibaba.fastjson.JSON
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.mail.OwnMail
import com.timecat.data.bmob.ext.bmob.deleteBlockRelation
import com.timecat.data.bmob.ext.bmob.deleteOwnMail
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.MailBlock
import com.timecat.identity.data.block.PackageItemBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.setting.MaterialForm
import com.timecat.module.user.game.item.BigTitle
import com.timecat.module.user.game.item.RewardList
import com.timecat.module.user.game.item.buildRewardListItem
import com.timecat.page.base.extension.simpleUIContainer
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
    lateinit var ownMail: OwnMail

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    fun buildView(context: Context): View {
        val container = simpleUIContainer(context)
        MaterialForm(context, container).apply {
            val mail = ownMail.mail
            BigTitle(mail.title)
            Body(mail.content)

            val head = MailBlock.fromJson(mail.structure)
            if (head.rewards.isEmpty()) {
                val button = MaterialButton(windowContext)
                button.setText("删除")
                button.setShakelessClickListener {
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
                container.addView(button)
            } else {
                val items = head.rewards
                RewardList(requireActivity(), items)

                val button = MaterialButton(windowContext)
                button.setText("使用")
                button.setShakelessClickListener {
                    val params = mutableMapOf<String, Any>()
                    params["ownMailId"] = ownMail.objectId
                    AVCloud.callFunctionInBackground<Any?>("readMail", params).subscribe({

                    }, {

                    })
                }
                container.addView(button)
            }
        }
        return container
    }

}