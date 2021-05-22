package com.timecat.module.user.adapter.game

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ActivityBlock
import com.timecat.identity.data.block.type.*
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseHeaderItem
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.adapter.detail.BaseDetailVH
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.ext.friendlyCreateTimeText
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/19
 * @description null
 * @usage null
 */
class ActivityItem(
    val activity: FragmentActivity,
    val item: Block,
    val onClick: ((View) -> Unit)? = null
) : BaseHeaderItem<ActivityItem.DetailVH>(item.objectId) {
    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val iv_avatar: ImageView = root.findViewById(R.id.iv_avatar)
        val tv_name: TextView = root.findViewById(R.id.tv_name)
    }

    override fun getLayoutRes(): Int = R.layout.user_item_item

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    var timeString: String = item.friendlyCreateTimeText()
    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        val title = item.title
        holder.tv_name.setText(title)

        val structure = item.structure
        val head = ActivityBlock.fromJson(structure)
        LOAD.image(head.header.avatar, holder.iv_avatar)
        holder.root.safeClick {
            //edit
            val path = when (item.subtype) {
                ACTIVITY_Url -> RouterHub.USER_UrlActivityEditorActivity
                ACTIVITY_Text_url -> RouterHub.USER_TextUrlActivityEditorActivity
                ACTIVITY_Dream -> RouterHub.USER_DreamActivityEditorActivity
                ACTIVITY_Double -> RouterHub.USER_DoubleActivityEditorActivity
                ACTIVITY_Card -> RouterHub.USER_CardActivityEditorActivity
                ACTIVITY_Price -> RouterHub.USER_PriceActivityEditorActivity
                ACTIVITY_Life -> RouterHub.USER_LifeActivityEditorActivity
                ACTIVITY_Achievement -> RouterHub.USER_AchievementActivityEditorActivity
                ACTIVITY_Get_back -> RouterHub.USER_GetBackActivityEditorActivity
                ACTIVITY_Seven_day_sign -> RouterHub.USER_SevenDaySignActivityEditorActivity
                ACTIVITY_Everyday_main -> RouterHub.USER_EveryDayMainActivityEditorActivity
                ACTIVITY_One_task -> RouterHub.USER_OneTaskActivityEditorActivity
                else -> RouterHub.USER_UrlActivityEditorActivity
            }
            NAV.go(path, "block", item)
        }
        holder.root.setOnLongClickListener {
            //distribute to user
            NAV.go(RouterHub.USER_SendActivityActivity, "block", item)
            true
        }

    }

    private fun View.safeClick(defaultClick: (View) -> Unit) {
        setShakelessClickListener(onClick = onClick ?: defaultClick)
    }
}