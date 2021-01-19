package com.timecat.module.user.game.mail

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
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
    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.base_rv, null)
        setupViews(view)
        dialog.setContentView(view)
    }

    private fun setupViews(view: View) {}
}