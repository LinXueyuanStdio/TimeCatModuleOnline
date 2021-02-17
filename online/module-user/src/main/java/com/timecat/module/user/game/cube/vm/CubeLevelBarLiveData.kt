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
    val exp: MutableLiveData<Long>,
    val maxLevel: MutableLiveData<Int>
) : MediatorLiveData<Pair<Int, Long>>() {
    init {
        addSource(exp) { updateValue() }
        addSource(maxLevel) { updateValue() }
    }

    private fun updateValue() {
        val currentMaxLevel: Int = maxLevel.value ?: 1
        val currentExp: Long = exp.value ?: 0
        val v = currentMaxLevel to currentExp
        postValue(v)
    }
}