package com.timecat.module.user.app

import androidx.lifecycle.LifecycleOwner
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.identity.service.PermissionService
import com.timecat.module.user.game.task.rule.GameService
import com.timecat.module.user.permission.PermissionValidator
import com.timecat.module.user.permission.UserContext
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description 权限服务
 * @usage null
 */
@ServiceAnno(PermissionService::class)
class PermissionServiceImpl : PermissionService {
    var userContext: UserContext? = null
    override fun validate(permissionId: String, callback: PermissionService.Callback) {
        userContext?.let {
            PermissionValidator.checkById(it, permissionId) {
                onAllowed = {
                    callback.onPass()
                }
            }
        }
    }

    override fun initPermission(owner: LifecycleOwner) {
        LogUtil.sd("init start")
        val service = NAV.service(GameService::class.java)
        val user = UserDao.getCurrentUser()
        user?.let {
            service?.cubeContext(owner, it, {}) {
                userContext = it
            }
        }
        LogUtil.sd("init end")
    }
}