package com.timecat.module.user.game.cube.vm

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.bmob.requestOneOwnCube
import com.timecat.data.bmob.ext.net.oneOwnCubeOf
import com.timecat.identity.data.block.IdentityBlock

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
    val ownCubes: MutableLiveData<List<OwnCube>> = MutableLiveData()

    /**
     * 当前选中的方块
     */
    val ownCube: MutableLiveData<OwnCube> = MutableLiveData()
    val objectId: MutableLiveData<String> = MutableLiveData()
    val cube: MutableLiveData<Block> = MutableLiveData()
    val exp: MutableLiveData<Long> = MutableLiveData()
    val maxLevel: MutableLiveData<Int> = MutableLiveData()
    val star: MutableLiveData<Int> = MutableLiveData()
    val skill_1: MutableLiveData<Int> = MutableLiveData()
    val skill_2: MutableLiveData<Int> = MutableLiveData()
    val skill_3: MutableLiveData<Int> = MutableLiveData()
    val skill_4: MutableLiveData<Int> = MutableLiveData()
    val skill_5: MutableLiveData<Int> = MutableLiveData()
    val skill_6: MutableLiveData<Int> = MutableLiveData()
    val equipment_1: MutableLiveData<Block?> = MutableLiveData()
    val equipment_2: MutableLiveData<Block?> = MutableLiveData()
    val equipment_3: MutableLiveData<Block?> = MutableLiveData()
    val equipment_4: MutableLiveData<Block?> = MutableLiveData()
    val equipment_5: MutableLiveData<Block?> = MutableLiveData()
    val equipment_6: MutableLiveData<Block?> = MutableLiveData()
    val head: MutableLiveData<IdentityBlock> = MutableLiveData()

    fun loadAllCube(own: List<OwnCube>) {
        if (own.isEmpty()) {
            ownCubes.postValue(own)
            return
        }
        ownCubes.postValue(own)
        val currentOwnCube = own[0]
        loadCube(currentOwnCube)
    }

    fun loadCube(own: OwnCube) {
        ownCube.postValue(own)
        cube.postValue(own.cube)
        objectId.postValue(own.objectId)
        exp.postValue(own.exp)
        maxLevel.postValue(own.maxLevel)
        star.postValue(own.star)
        skill_1.postValue(own.skill_1)
        skill_2.postValue(own.skill_2)
        skill_3.postValue(own.skill_3)
        skill_4.postValue(own.skill_4)
        skill_5.postValue(own.skill_5)
        skill_6.postValue(own.skill_6)
        equipment_1.postValue(own.equipment_1)
        equipment_2.postValue(own.equipment_2)
        equipment_3.postValue(own.equipment_3)
        equipment_4.postValue(own.equipment_4)
        equipment_5.postValue(own.equipment_5)
        equipment_6.postValue(own.equipment_6)
        head.postValue(IdentityBlock.fromJson(own.cube.structure))
    }

    fun reloadCurrentCube() {
        cube.value?.let {
            requestOneOwnCube {
                query = oneOwnCubeOf(it.objectId)
                onSuccess = {
                    loadCube(it)
                }
            }
        }
    }

    val cubeLevelBar: CubeLevelBarLiveData = CubeLevelBarLiveData(exp, maxLevel)

}