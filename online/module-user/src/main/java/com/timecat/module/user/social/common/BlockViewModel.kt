package com.timecat.module.user.social.common

import androidx.lifecycle.MutableLiveData
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.ext.RxViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/17
 * @description null
 * @usage null
 */
class BlockViewModel : RxViewModel() {
    val block: MutableLiveData<Block?> = MutableLiveData()
}