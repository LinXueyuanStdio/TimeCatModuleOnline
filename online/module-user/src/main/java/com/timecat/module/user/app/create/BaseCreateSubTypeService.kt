package com.timecat.module.user.app.create

import android.content.Context
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.service.PermissionService
import com.timecat.middle.block.adapter.SubItem
import com.timecat.middle.block.service.CreateBlockSubTypeService
import com.timecat.middle.block.service.ItemCommonListener
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/24
 * @description null
 * @usage null
 */
abstract class BaseCreateSubTypeService : CreateBlockSubTypeService {
    val permissionService by lazy { NAV.service(PermissionService::class.java) }
    suspend fun checkPermission(permissionId: String): Boolean = suspendCoroutine { res ->
        permissionService?.validate(permissionId, object : PermissionService.Callback {
            override fun onPass() {
                res.resume(true)
            }

            override fun onReject() {
                res.resume(false)
            }
        }) ?: res.resume(false)
    }

    suspend fun checkNotLoginOrNotPermission(permissionId: String): Boolean {
        return checkNotLogin() || !checkPermission(permissionId)
    }

    suspend fun checkNotLogin(): Boolean {
        return UserDao.getCurrentUser() == null
    }

    override fun create(context: Context, subItem: SubItem, parent: RoomRecord?, listener: ItemCommonListener) {
        createInActivity(context, subItem, parent, listener)
    }
}