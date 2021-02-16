package com.timecat.module.user.game.cube.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/16
 * @description null
 * @usage null
 */
class CubeLevelBarLiveData(
    exp: MutableLiveData<Long>,
    maxLevel: MutableLiveData<Int>
) : MediatorLiveData<Pair<Int, Long>>() {
    init {
        addSource(exp) {
            val currentMaxLevel: Int = value?.first ?: 1
            val currentExp = exp.value ?: 0
            val v = currentMaxLevel to currentExp
            postValue(v)
        }
        addSource(maxLevel) {
            val currentMaxLevel: Int = maxLevel.value ?: 1
            val currentExp = value?.second ?: 0
            val v = currentMaxLevel to currentExp
            postValue(v)
        }
    }
}