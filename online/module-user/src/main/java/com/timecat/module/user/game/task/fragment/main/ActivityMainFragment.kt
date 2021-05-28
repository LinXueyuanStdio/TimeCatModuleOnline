package com.timecat.module.user.game.task.fragment.main

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import com.google.android.material.chip.Chip
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.game.OwnActivity
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.*
import com.timecat.identity.data.block.type.ACTIVITY_Everyday_main
import com.timecat.identity.data.block.type.ACTIVITY_One_task
import com.timecat.identity.data.block.type.ACTIVITY_Seven_day_sign
import com.timecat.identity.data.block.type.ACTIVITY_Url
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.*
import com.timecat.module.user.R
import com.timecat.module.user.game.task.fragment.BaseActivityFragment
import com.timecat.module.user.view.TaskCard
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/7
 * @description 主要活动
 * @usage null
 */
@FragmentAnno(RouterHub.USER_ActivityMainFragment)
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
            showActivityDetail(owns[0])
            onContentLoaded()
        }
    }

    private fun buildText(ownActivity: OwnActivity): View {
        return Chip(_mActivity).apply {
            layout_width = match_parent
            layout_height = wrap_content
            layout_gravity = gravity_center_horizontal
            margin = 2
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

    private fun buildDefaultActivityView(cover: String): View {
        return ActivityUrlView(_mActivity).apply {
            layout_width = match_parent
            layout_height = match_parent
            this.cover = cover
        }
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
//                        NAV.go(RouterHub.ARTIFACT_MainActivity, "mUrl", url)
                        LogUtil.sd(url)
                    }
                }
            }
            ACTIVITY_Seven_day_sign -> {
                return buildDefaultActivityView(cover)
            }
            ACTIVITY_Everyday_main -> {
                return buildDefaultActivityView(cover)
            }
            ACTIVITY_One_task -> {
                val block = ActivityOneTaskBlock.fromJson(head.structure)
                val taskId = block.taskId
                viewModel.tasks.value?.let {
                    it.find { it.objectId == taskId }?.let {
                        val taskHeader = TaskBlock.fromJson(it.structure)
                        return ActivityOneTaskView(_mActivity, TaskCard.Task(taskHeader.rewards)).apply {
                            layout_width = match_parent
                            layout_height = match_parent
                            this.cover = cover
                            val recieve = viewModel.taskRewardProgress.value?.let { it[taskId] }
                            if (recieve == false) {
                                taskCard.buttonText = "领取"
                                taskCard.buttonClick = {
                                    ToastUtil.w_long("领取")
                                    taskCard.buttonText = "已领取"
                                }
                            } else {
                                taskCard.buttonText = "已领取"
                            }
                        }
                    }
                } ?: return buildDefaultActivityView(cover)
            }
            else -> return buildDefaultActivityView(cover)
        }
    }
}