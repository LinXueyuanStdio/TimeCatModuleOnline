package com.timecat.module.user.game.cube.fragment


import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 转发的动态
 * @usage null
 */
class MomentListFragment : BaseListFragment() {
    override fun name(): String = "转发的动态"
    override fun query() = viewModel.block.value!!.findAllMoment()
    override fun addNew(block: Block) {
        GO.addMomentFor(block)
    }
}