package com.timecat.module.user.social.share

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.page.base.extension.simpleUIContainer
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
class ShareBottomSheet : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("block")
    var block: Block? = null
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
        return container
    }
}