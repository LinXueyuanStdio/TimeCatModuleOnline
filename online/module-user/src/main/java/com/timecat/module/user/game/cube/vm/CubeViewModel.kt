package com.timecat.module.user.game.cube.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class CubeViewModel : ViewModel() {
    /**
     * 所有方块
     */
    val cubes: MutableLiveData<List<OwnCube>> = MutableLiveData()

    /**
     * 当前选中的方块
     */
    val cube: MutableLiveData<OwnCube?> = MutableLiveData()

    val block: LiveData<Block?> = Transformations.map(cube) { it?.cube }
}