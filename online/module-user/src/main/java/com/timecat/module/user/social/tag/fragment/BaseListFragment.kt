package com.timecat.module.user.social.tag.fragment

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.timecat.module.user.social.tag.vm.TagViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的子块组成的列表
 * @usage null
 */
abstract class BaseListFragment : BaseEndlessBlockFragment() {

    override fun layout(): Int = R.layout.user_base_refresh_list
    lateinit var write_response: TextView

    lateinit var viewModel: TagViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(TagViewModel::class.java)
        viewModel.tag.observe(viewLifecycleOwner, {
            load()
        })
    }

    override fun bindView(view: View) {
        super.bindView(view)
        write_response = view.findViewById(R.id.write_response)
        write_response.setShakelessClickListener {
            viewModel.tag.value?.let {
                addNew(it)
            }
        }
    }

    override fun loadData() {}

    open fun addNew(block: Block) {}
}