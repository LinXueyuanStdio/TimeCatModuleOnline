package com.timecat.module.user.game.cube.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.bmob.requestOneOwnCube
import com.timecat.data.bmob.ext.net.oneOwnCubeOf

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
    val cube: MutableLiveData<OwnCube> = MutableLiveData()

    val block: MutableLiveData<Block> = MutableLiveData()

    fun reloadCube() {
        block.value?.let {
            requestOneOwnCube {
                query = oneOwnCubeOf(it.objectId)
                onSuccess = {
                    cube.postValue(it)
                    block.postValue(it.cube)
                }
            }
        }
    }
}