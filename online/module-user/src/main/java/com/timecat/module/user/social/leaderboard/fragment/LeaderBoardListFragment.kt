package com.timecat.module.user.social.leaderboard.fragment

import androidx.lifecycle.ViewModelProvider
import com.timecat.data.bmob.ext.net.findAllItemsInLeaderBoard
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseEndlessBlock2BlockFragment
import com.timecat.module.user.social.leaderboard.vm.LeaderBoardViewModel

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