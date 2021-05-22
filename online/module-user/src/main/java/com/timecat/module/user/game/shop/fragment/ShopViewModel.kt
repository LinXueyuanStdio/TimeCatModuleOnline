package com.timecat.module.user.game.shop.fragment

import androidx.lifecycle.MutableLiveData
import com.timecat.data.bmob.data.common.Block
import com.timecat.module.user.ext.RxViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/22
 * @description null
 * @usage null
 */
class ShopViewModel : RxViewModel() {
    val money: MutableLiveData<Block> = MutableLiveData()
    val haveMoney: MutableLiveData<Int> = MutableLiveData()
}