package com.timecat.module.user.game.cube.vm

import androidx.lifecycle.MutableLiveData
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneOwnCube
import com.timecat.data.bmob.ext.net.oneOwnCubeOf
import com.timecat.identity.data.block.IdentityBlock
import com.timecat.module.user.ext.RxViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description null
 * @usage null
 */
class IdentityViewModel : RxViewModel() {
    /**
     * 所有方块
     */
    val ownCubes: MutableLiveData<List<Block>> = MutableLiveData()

    /**
     * 当前选中的方块
     */
    val cube: MutableLiveData<Block> = MutableLiveData()

    fun loadAllCube(own: List<Block>) {
        if (own.isEmpty()) {
            ownCubes.postValue(own)
            return
        }
        ownCubes.postValue(own)
        val currentCube = own[0]
        loadCube(currentCube)
    }

    fun loadCube(currentCube: Block) {
        cube.postValue(currentCube)
    }

}