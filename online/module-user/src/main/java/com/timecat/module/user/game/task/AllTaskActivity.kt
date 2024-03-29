package com.timecat.module.user.game.task

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allTask
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.game.TaskItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 活动、任务
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllTaskActivity)
class AllTaskActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "任务"
    override fun query() = allTask()
    override fun block2Item(block: Block) = TaskItem(this, block)
    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context)
    }
}