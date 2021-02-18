package com.timecat.module.user.game.cube.fragment

import android.view.View
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.findAllPost
import com.timecat.module.user.base.GO

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的帖子
 * @usage null
 */
class PostListFragment : BaseListFragment() {
    override fun name(): String = "帖子"
    override fun query() = viewModel.cube.value!!.findAllPost()
    override fun bindView(view: View) {
        super.bindView(view)
        write_response.setText("发帖子")
    }
    override fun addNew(block: Block) {
        GO.addPostFor(block)
    }
}