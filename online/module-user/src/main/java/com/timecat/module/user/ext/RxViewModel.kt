package com.timecat.module.user.ext

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/17
 * @description null
 * @usage null
 */
open class RxViewModel : ViewModel() {
    val disposables = CompositeDisposable()
    var attachLifecycle: Disposable? = null
        set(value) {
            value?.let { disposables.add(it) }
            field = value
        }

    infix fun attach(disposable: Disposable?) {
        attachLifecycle = disposable
    }

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}