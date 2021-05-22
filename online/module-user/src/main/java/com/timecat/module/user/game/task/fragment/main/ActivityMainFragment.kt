package com.timecat.module.user.game.task.fragment.main

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.identity.data.block.ActivityBlock
import com.timecat.identity.data.block.ActivityOneTaskBlock
import com.timecat.identity.data.block.ActivityUrlBlock
import com.timecat.identity.data.block.type.ACTIVITY_Everyday_main
import com.timecat.identity.data.block.type.ACTIVITY_One_task
import com.timecat.identity.data.block.type.ACTIVITY_Seven_day_sign
import com.timecat.identity.data.block.type.ACTIVITY_Url
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.*
import com.timecat.module.user.R
import com.timecat.module.user.game.task.fragment.BaseActivityFragment
import com.timecat.module.user.game.task.rule.ActivityContext
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_ActivityMainFragment)
class ActivityMainFragment : BaseActivityFragment() {
    override fun layout(): Int = R.layout.user_fragment_game_activity_main

    private lateinit var sv: ScrollView
    private lateinit var container: FrameLayout
    private lateinit var linear: LinearLayout
    override fun bindView(view: View) {
        super.bindView(view)
        sv = view.findViewById(R.id.sv)
        linear = view.findViewById(R.id.linear)
        container = view.findViewById(R.id.container)
    }

    private val activityViewMap: MutableMap<String, View> = mutableMapOf()

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel.mainActivities.observe(this) { owns ->
            if (owns.isEmpty()) {
                mStatefulLayout?.showEmpty("没有正在进行的活动喵～")
                return@observe
            }
            for (own in owns) {
                linear.addView(buildText(own))
            }
        }
    }

    private fun buildText(ownActivity: OwnActivity): View {
        return TextView(_mActivity).apply {
            layout_width = match_parent
            layout_height = wrap_content
            layout_gravity = gravity_center_horizontal
            setTextColor(Attr.getPrimaryTextColor(_mActivity))
            text_size = 18
            text = ownActivity.activity.title
            setShakelessClickListener {
                showActivityDetail(ownActivity)
            }
        }
    }

    private fun showActivityDetail(ownActivity: OwnActivity) {
        val id = ownActivity.objectId
        var view = activityViewMap[id]
        if (view == null) {
            view = loadActivityView(ownActivity)
        }
        container.removeAllViews()
        container.addView(view)
    }

    private fun loadActivityView(ownActivity: OwnActivity): View {
        val view = buildActivityView(ownActivity)
        activityViewMap[ownActivity.objectId] = view
        return view
    }

    private fun buildActivityView(ownActivity: OwnActivity): View {
        val activity = ownActivity.activity
        val head = ActivityBlock.fromJson(activity.structure)
        val header = head.header
        val cover = header.cover
        when (activity.subtype) {
            ACTIVITY_Url -> {
                val block = ActivityUrlBlock.fromJson(head.structure)
                val url = block.url
                return ActivityUrlView(_mActivity).apply {
                    layout_width = match_parent
                    layout_height = match_parent
                    this.cover = cover
                    onClick = {
                        NAV.go(url)
                    }
                }
            }
            ACTIVITY_Seven_day_sign -> {
                return ActivityUrlView(_mActivity).apply {
                    layout_width = match_parent
                    layout_height = match_parent
                    this.cover = cover
                }
            }
            ACTIVITY_Everyday_main -> {
                return ActivityUrlView(_mActivity).apply {
                    layout_width = match_parent
                    layout_height = match_parent
                    this.cover = cover
                }
            }
            ACTIVITY_One_task -> {
                val block = ActivityOneTaskBlock.fromJson(head.structure)
                val taskId = block.taskId
//                val receive = ActivityContext.taskRewardProgress[taskId]
                return ActivityOneTaskView(_mActivity).apply {
                    layout_width = match_parent
                    layout_height = match_parent
                    this.cover = cover
//                    taskCard.rewards = block.taskId
                }
            }
            else -> return ActivityUrlView(_mActivity).apply {
                layout_width = match_parent
                layout_height = match_parent
                this.cover = cover
            }
        }
    }
}