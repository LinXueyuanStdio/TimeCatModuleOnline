package com.timecat.module.user.social.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data.User
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 用户上下文
 * @usage null
 */
class UserViewModel : ViewModel() {
    val user: MutableLiveData<User?> = MutableLiveData()

    var intro: MutableLiveData<String> = MutableLiveData()
    var nickName: MutableLiveData<String> = MutableLiveData()
    var gender: MutableLiveData<String> = MutableLiveData()
    var address: MutableLiveData<String> = MutableLiveData()
    var exp: MutableLiveData<Long> = MutableLiveData()
    var star: MutableLiveData<Int> = MutableLiveData()
    var water: MutableLiveData<Int> = MutableLiveData()
    var lastSettleTime: MutableLiveData<Date> = MutableLiveData()
    var currency: MutableLiveData<Long> = MutableLiveData()
    var charge: MutableLiveData<Long> = MutableLiveData()
    var moneyCharge: MutableLiveData<Long> = MutableLiveData()
    var avatar: MutableLiveData<String> = MutableLiveData()
    var cover: MutableLiveData<String> = MutableLiveData()

    fun loadUser(currentUser: User) {
        user.postValue(currentUser)
        intro.postValue(currentUser.intro)
        nickName.postValue(currentUser.nickName)
        gender.postValue(currentUser.gender)
        address.postValue(currentUser.address)
        exp.postValue(currentUser.exp)
        star.postValue(currentUser.star)
        water.postValue(currentUser.water)
        lastSettleTime.postValue(currentUser.lastSettleTime)
        currency.postValue(currentUser.currency)
        charge.postValue(currentUser.charge)
        moneyCharge.postValue(currentUser.moneyCharge)
        avatar.postValue(currentUser.avatar)
        cover.postValue(currentUser.cover)
    }
}