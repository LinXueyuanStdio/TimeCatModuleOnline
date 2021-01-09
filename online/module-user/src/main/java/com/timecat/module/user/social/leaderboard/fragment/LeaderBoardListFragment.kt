package com.timecat.module.user.social.leaderboard.fragment

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.*
import com.timecat.data.bmob.ext.net.allBlock
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.module.user.adapter.DetailAdapter
import com.timecat.module.user.adapter.detail.ActionItem
import com.timecat.module.user.adapter.detail.NinePhotoItem
import com.timecat.module.user.adapter.detail.SimpleContentItem
import com.timecat.module.user.base.login.BaseLoginListFragment
import com.timecat.module.user.social.forum.vm.ForumViewModel
import com.timecat.identity.data.block.ForumBlock
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseEndlessBlock2BlockFragment
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.timecat.module.user.social.leaderboard.vm.LeaderBoardViewModel
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 排行榜的列表项
 * @usage null
 */
class LeaderBoardListFragment : BaseEndlessBlock2BlockFragment() {
    override fun layout(): Int = R.layout.user_base_refresh_list
    override fun name(): String = "讨论"
    override fun query() = viewModel.board.value!!.findAllItemsInLeaderBoard()

    lateinit var viewModel: LeaderBoardViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(LeaderBoardViewModel::class.java)
        viewModel.board.observe(viewLifecycleOwner, {
            load()
        })
    }
    override fun loadData() {}

}