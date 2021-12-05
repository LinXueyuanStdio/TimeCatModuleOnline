package com.timecat.module.user.adapter.virtual

import android.content.Context
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.materialdialogs.customview.customView
import com.timecat.component.commonsdk.extension.beVisibleIf
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.business.form.Switch
import com.timecat.layout.ui.business.form.VerticalContainer
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.ext.showDialog
import com.timecat.middle.block.service.ItemCommonListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailItem
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.ext.allowPublicEdit
import com.timecat.module.user.ext.allowPublicInteract
import com.timecat.module.user.ext.shareToPublic
import com.timecat.module.user.ext.toRoomRecord
import com.timecat.page.base.extension.simpleUIContainer
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/12/5
 * @description null
 * @usage null
 */
class PermissionHeaderCard(
    var context: Context,
    var block: Block,
    var listener: ItemCommonListener
) : BaseDetailItem<PermissionHeaderCard.DetailVH>(block.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val share: AppCompatButton by lazy { root.findViewById<AppCompatButton>(R.id.share) }
    }

    override fun getLayoutRes(): Int = R.layout.user_card_page_permission

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>): DetailVH {
        return DetailVH(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<IFlexible<*>>, holder: DetailVH, position: Int, payloads: MutableList<Any>?) {
        super.bindViewHolder(adapter, holder, position, payloads)
        holder.share.setShakelessClickListener {
            context.showDialog {
                val view = simpleUIContainer(context).apply {
                    val publicAccessUi = VerticalContainer(autoAdd = false) {
                        Switch("允许公开交互", "如果允许，则每个人都能评论、使用", getInitialCheck = { block.allowPublicInteract }) {
                            block.allowPublicInteract = it
                            listener.primaryDb().updateRecord(block.toRoomRecord())
                        }
                        Switch("允许公开编辑", "如果允许，则每个人都能参与编辑", getInitialCheck = { block.allowPublicEdit }) {
                            block.allowPublicEdit = it
                            listener.primaryDb().updateRecord(block.toRoomRecord())
                        }
                    }
                    Switch("分享到公共空间", "如果分享，则每个人都能看到", getInitialCheck = { block.shareToPublic }) {
                        block.shareToPublic = it
                        publicAccessUi.beVisibleIf(it)
                        listener.primaryDb().updateRecord(block.toRoomRecord())
                    }
                    publicAccessUi.beVisibleIf(block.shareToPublic)
                    addView(publicAccessUi)
                }
                customView(view = view)
            }
        }
    }
}