package com.timecat.module.user.game.task.task

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.component.commonsdk.utils.ShareUtils
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.business.form.CenterText
import com.timecat.layout.ui.business.form.Divider
import com.timecat.layout.ui.business.form.HorizontalContainer
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.layout.HorizontalScrollView
import com.timecat.layout.ui.layout.padding
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.social.comment.showSubComments
import com.timecat.module.user.view.dsl.IconText
import com.timecat.module.user.view.dsl.RoundIconText
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@FragmentAnno(USER_ShowFinishTasksBottomSheet)
class ShowFinishTasksBottomSheet : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("block")
    var _block: Block?=null
    lateinit var block: Block
    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        block = _block!!
        super.onCreate(savedInstanceState)
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    fun buildView(context: Context): View {
        val container = ContainerItem(context)
        container.apply {
            Divider().apply {
                alpha = 0f
            }
            CenterText("取消").apply {
                setBackgroundColor(Attr.getBackgroundDarkColor(context))
                padding = 15
                setShakelessClickListener {
                    dismiss()
                }
            }
        }
        return container
    }
}