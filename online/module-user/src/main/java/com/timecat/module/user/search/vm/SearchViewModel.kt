package com.timecat.module.user.search.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.module.user.ext.RxViewModel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/8
 * @description null
 * @usage null
 */
class SearchViewModel : RxViewModel() {
    val searchText: MutableLiveData<String> = MutableLiveData()
}