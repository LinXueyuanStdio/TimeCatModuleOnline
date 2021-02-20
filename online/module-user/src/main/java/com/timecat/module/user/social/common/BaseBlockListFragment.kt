package com.timecat.module.user.social.common

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseEndlessBlockFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 论坛的子块组成的列表
 * @usage null
 */
abstract class BaseBlockListFragment : BaseEndlessBlockFragment() {

    override fun layout(): Int = R.layout.user_base_refresh_list
    lateinit var response: TextView

    lateinit var viewModel: BlockViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        viewModel.block.observe(viewLifecycleOwner, {
            load()
        })
    }

    override fun bindView(view: View) {
        super.bindView(view)
        response = view.findViewById(R.id.write_response)
        response.setShakelessClickListener {
            viewModel.block.value?.let {
                addNew(it)
            }
        }
    }
    open fun addNew(block: Block) {}
}