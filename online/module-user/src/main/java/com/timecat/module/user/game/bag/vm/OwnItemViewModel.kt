package com.timecat.module.user.game.bag.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.module.user.game.cube.isExpItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class OwnItemViewModel : ViewModel() {
    /**
     * 所有物品
     */
    val ownItems: MutableLiveData<List<OwnItem>> = MutableLiveData()

    /**
     * 当前选中的物品
     */
    val ownItem: MutableLiveData<OwnItem> = MutableLiveData()

    /**
     * 所有经验
     */
    val ownExpItems: MutableLiveData<List<OwnItem>> = MutableLiveData()

    fun loadAllOwnItems(own: List<OwnItem>) {
        ownItems.postValue(own)
        val currentOwnItem = own[0]
        ownItem.postValue(currentOwnItem)
        val expItems = own.filter { isExpItem(it.item.objectId) }
        ownExpItems.postValue(expItems)
    }
}