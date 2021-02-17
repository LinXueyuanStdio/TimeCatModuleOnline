package com.timecat.module.user.game.bag.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.module.user.ext.RxViewModel
import com.timecat.module.user.game.cube.isExpItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class OwnItemViewModel : RxViewModel() {
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

    val itemMap: MutableMap<String, MutableLiveData<OwnItem>> = mutableMapOf()
    fun loadPagedOwnItems(key: String, own: List<OwnItem>) {
        for (i in own) {
            setOwnItem(i)
        }
    }

    fun setOwnItem(item: OwnItem): MutableLiveData<OwnItem> {
        var i = itemMap[item.objectId]
        if (i == null) {
            i = MutableLiveData(item)
            itemMap[item.objectId] = i
        }
        return i
    }

    fun getOwnItem(uuid: String): MutableLiveData<OwnItem>? {
        return itemMap[uuid]
    }

    fun loadAllOwnItems(own: List<OwnItem>) {
        ownItems.postValue(own)
        val currentOwnItem = own[0]
        ownItem.postValue(currentOwnItem)
        val expItems = own.filter { isExpItem(it.item.objectId) }
        ownExpItems.postValue(expItems)
    }
}