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

    lateinit var blockViewModel: BlockViewModel
    override fun initViewAfterLogin() {
        blockViewModel = ViewModelProvider(requireActivity()).get(BlockViewModel::class.java)
        blockViewModel.block.observe(viewLifecycleOwner, {
            loadData()
        })
    }

    override fun bindView(view: View) {
        super.bindView(view)
        response = view.findViewById(R.id.write_response)
        response.setShakelessClickListener {
            blockViewModel.block.value?.let {
                addNew(it)
            }
        }
    }
    open fun addNew(block: Block) {}
}