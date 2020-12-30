package com.timecat.module.user.social.user

import android.view.ViewGroup
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.middle.setting.BaseSettingActivity
import com.timecat.module.user.R
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/7
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_EditUserActivity)
class EditUserActivity : BaseSettingActivity() {
    @AttrValueAutowiredAnno("userId")
    @JvmField
    var userId: String? = null


    override fun routerInject() = NAV.inject(this)

    override fun title(): String = "账号资料"

    override fun addSettingItems(container: ViewGroup) {
        if (userId == null) finish()
        UserDao.queryUserInfo(userId) { user, e ->
            if (e != null) {
                ToastUtil.e(e.message)
                LogUtil.e(e)
                finish()
            } else if (user == null) {
                ToastUtil.e("找不到用户")
                LogUtil.e(e)
            } else {
                addSettingItems(container, user)
            }
        }
    }

    private fun addSettingItems(container: ViewGroup, user: _User) {
        ImageItem(this).apply {
            title = "头像"
            setImage(user.avatar)
            onClick {
                //TODO 编辑头像，拍照、本地图片、推荐网络图片
            }
        }.also { container.addView(it) }

        simpleDivider(container)
        simpleNext(container, getString(R.string.username), null, user.username, null) {

        }
        simpleDivider(container)
        simpleNext(container, getString(R.string.username), null, user.username, null) {

        }
    }

}