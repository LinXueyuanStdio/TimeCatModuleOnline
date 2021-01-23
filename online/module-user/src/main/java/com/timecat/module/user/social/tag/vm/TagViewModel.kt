package com.timecat.module.user.social.tag.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.common.Block

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class TagViewModel : ViewModel() {
    val tag: MutableLiveData<Block?> = MutableLiveData()
}