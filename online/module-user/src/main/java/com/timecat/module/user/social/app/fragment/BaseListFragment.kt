package com.timecat.module.user.social.app.fragment

import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.BaseEndlessBlockFragment
import com.timecat.module.user.social.app.vm.AppViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 论坛的子块组成的列表
 * @usage null
 */
abstract class BaseListFragment : BaseEndlessBlockFragment() {

    override fun layout(): Int = R.layout.user_base_refresh_list
    lateinit var write_response: TextView

    lateinit var viewModel: AppViewModel
    override fun initViewAfterLogin() {
        viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.app.observe(viewLifecycleOwner, {
            load()
        })
    }

    override fun bindView(view: View) {
        super.bindView(view)
        write_response = view.findViewById(R.id.write_response)
        write_response.setShakelessClickListener {
            viewModel.app.value?.let {
                addNew(it)
            }
        }
    }

    override fun loadData() {}

    open fun addNew(block: Block) {}
}