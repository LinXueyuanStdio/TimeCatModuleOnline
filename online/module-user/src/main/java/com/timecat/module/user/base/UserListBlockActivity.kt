package com.timecat.module.user.base


import com.timecat.data.bmob.data.common.User2User
import com.timecat.data.bmob.ext.net.allFollow

class UserListBlockActivity : BaseSelectUserRelationActivity() {
    override fun title(): String = "提及 @"
    override fun query(): BmobQuery<User2User> = I().allFollow()

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    companion object {
        const val DATA = "data"
    }
}
