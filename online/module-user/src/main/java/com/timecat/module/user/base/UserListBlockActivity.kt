package com.timecat.module.user.base


import com.timecat.data.bmob.ext.net.allFollow

class UserListBlockActivity : BaseSelectUserRelationActivity() {
    override fun title(): String = "提及 @"
    override fun query() = I().allFollow()

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    companion object {
        const val DATA = "data"
    }
}
