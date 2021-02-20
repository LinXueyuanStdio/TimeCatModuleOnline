package com.timecat.module.user.social.common


import android.view.View
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllMoment
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题讨论
 * @usage null
 */
class MomentListFragment : BaseBlockListFragment() {
    override fun name(): String = "动态"
    override fun query() = blockViewModel.block.value!!.findAllMoment()
    override fun bindView(view: View) {
        super.bindView(view)
        response.text = "转发到动态"
    }
    override fun addNew(block: Block) {
        GO.addMomentFor(block)
    }
}