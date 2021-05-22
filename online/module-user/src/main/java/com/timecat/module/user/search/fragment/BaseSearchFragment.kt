package com.timecat.module.user.search.fragment

import androidx.lifecycle.ViewModelProvider
import com.timecat.module.user.base.BaseEndlessListFragment
import com.timecat.module.user.search.vm.SearchViewModel
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description 搜索 用户名 或 uid
 * @usage null
 */
abstract class BaseSearchFragment : BaseEndlessListFragment() {

    lateinit var searchViewModel: SearchViewModel
    var disposable: Disposable? = null

    override fun initViewAfterLogin() {
        loadData()
    }

    override fun loadData() {
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        searchViewModel.searchText.observe(this, {
            disposable?.dispose()
            it?.let { onSearch(it) }
        })
        mStatefulLayout?.showEmpty()
    }

    abstract fun onSearch(q: String)
}