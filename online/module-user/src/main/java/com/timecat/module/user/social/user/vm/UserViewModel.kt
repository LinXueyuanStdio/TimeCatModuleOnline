package com.timecat.module.user.social.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timecat.data.bmob.data._User

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 用户上下文
 * @usage null
 */
class UserViewModel : ViewModel() {
    val user: MutableLiveData<_User> = MutableLiveData()
}