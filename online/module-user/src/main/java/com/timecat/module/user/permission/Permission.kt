package com.timecat.module.user.permission

import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.middle.block.permission.HunPermission
import com.timecat.middle.block.permission.MetaPermission
import com.timecat.middle.block.permission.PermissionCallback
import com.timecat.middle.block.permission.Why

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/21
 * @description 权限相关的工具
 * @usage null
 */

/**
 * 鉴权器
 *
 * 检查是否拥有权限
 */
class Checker {
    /**
     * 要检查是否拥有的元权限集
     *
     * 由 App 持有
     */
    lateinit var target: Map<MetaPermission, PermissionCallback>

    /**
     * 拥有的混权限集
     *
     * 由用户持有
     * 考虑缓存这个
     */
    lateinit var owns: List<HunPermission>

    private fun hasPermission(metaPermission: MetaPermission): Pair<Boolean, Why> {
        for (i in owns) {
            if (i.checker.matches(metaPermission)) {
                return true to i.why
            }
        }
        return false to Why("无权限")
    }

    fun build() {
        LogUtil.d("$owns")
        for (j in target) {
            LogUtil.sd("checking... ${j.key}")
            val result = hasPermission(j.key)
            if (result.first) {
                j.value.onAllowed?.invoke(result.second)
            } else {
                j.value.onBanned?.invoke(result.second)
            }
        }
    }
}

/**
 * 鉴权器
 * 友好的使用接口
 */
object PermissionValidator {

    private fun checkPermission(build: Checker.() -> Unit) {
        Checker().apply(build).build()
    }

    @JvmStatic
    fun check(userContext: UserContext, permission: MetaPermission, callback: PermissionCallback.() -> Unit) {
        val ownsPermission = userContext.ownsPermission
        checkPermission {
            target = mapOf(permission to PermissionCallback().apply(callback))
            owns = ownsPermission
        }
    }

    @JvmStatic
    fun checkById(userContext: UserContext, permissionId: String, callback: PermissionCallback.() -> Unit) {
        val p = userContext.of(permissionId) ?: return
        check(userContext, p, callback)
    }

    @JvmStatic
    fun check(ownsPermission: MutableList<HunPermission>, permissions: Map<MetaPermission, PermissionCallback>) {
        checkPermission {
            target = permissions
            owns = ownsPermission
        }
    }

    @JvmStatic
    fun checkById(userContext: UserContext, permissions: Map<String, PermissionCallback>) {
        val pMap = HashMap<MetaPermission, PermissionCallback>()
        for (permissionId in permissions.keys) {
            val p = userContext.of(permissionId) ?: continue
            val pCallback = permissions[permissionId] ?: continue
            pMap[p] = pCallback
        }
        check(userContext.ownsPermission, pMap)
    }
}

