package com.timecat.module.user.social.comment

import android.app.Dialog
import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.page.base.extension.simpleUIContainer

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/9
 * @description null
 * @usage null
 */
class CommentDetailBottomSheet : BottomSheetDialogFragment() {

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = buildView(dialog.context)
        dialog.setContentView(view)
    }

    fun buildView(context: Context): View {
        val container = simpleUIContainer(context)
        return container
    }
}