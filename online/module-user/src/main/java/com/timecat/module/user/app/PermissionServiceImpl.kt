package com.timecat.module.user.app

import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.identity.service.PermissionService
import com.timecat.module.user.permission.MetaPermissionPool
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
    override fun validate(permissionId: String, callback: PermissionService.Callback) {
        PermissionValidator.checkById(permissionId) {
            onAllowed = {
                callback.onPass()
            }
        }
    }

    override fun initPermission() {
        LogUtil.sd("init start")
        MetaPermissionPool.ensureLoaded()
        UserContext.loadByUser(UserDao.getCurrentUser())
        LogUtil.sd("init end")
    }
}