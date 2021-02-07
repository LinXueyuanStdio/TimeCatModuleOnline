package com.timecat.module.user.game.task.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnActivity

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class TaskViewModel : ViewModel() {
    /**
     * 所有活动
     */
    val activities: MutableLiveData<List<OwnActivity>> = MutableLiveData()

    /**
     * 当前选中的活动
     */
    val ownActivity: MutableLiveData<OwnActivity?> = MutableLiveData()

    val block: MutableLiveData<Block?> = MutableLiveData()
}