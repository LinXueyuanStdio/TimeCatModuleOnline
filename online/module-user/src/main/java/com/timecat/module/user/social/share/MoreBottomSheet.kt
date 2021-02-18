package com.timecat.module.user.social.share

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.business.form.H1
import com.timecat.layout.ui.business.form.H2
import com.timecat.layout.ui.business.form.HorizontalContainer
import com.timecat.layout.ui.business.form.VerticalContainer
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.business.setting.RewardItem
import com.timecat.layout.ui.layout.HorizontalScrollView
import com.timecat.module.user.R
import com.timecat.module.user.view.dsl.IconText
import com.timecat.module.user.view.item.IconTextItem
import com.timecat.page.base.extension.simpleUIContainer
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.FragmentAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description null
 * @usage null
 */
@FragmentAnno(USER_MoreBottomSheet)
class MoreBottomSheet : BottomSheetDialogFragment() {
    @AttrValueAutowiredAnno("block")
    @JvmField
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
        val container = ContainerItem(context)
        container.apply {
            H2("分享")
            HorizontalScrollView {
                HorizontalContainer {
                    IconText(R.drawable.ic_logo_wechat, "微信")
                    IconText(R.drawable.ic_logo_moments, "朋友圈")
                    IconText(R.drawable.ic_logo_weibo, "微博")
                    IconText(R.drawable.ic_logo_qq, "QQ")
                    IconText(R.drawable.ic_logo_more, "更多")
                    IconText(R.drawable.ic_logo_link, "复制链接")
                }
            }
        }
        return container
    }
}