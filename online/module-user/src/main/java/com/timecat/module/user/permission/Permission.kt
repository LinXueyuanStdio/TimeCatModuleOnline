package com.timecat.module.user.permission

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allMetaPermission
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
 * 元权限池
 */
object MetaPermissionPool {
    var pool: MutableMap<String, MetaPermission> = HashMap()

    /**
     * 加载所有的元权限
     */
    private fun load() {
        pool.clear()
        requestBlock {
            query = allMetaPermission().apply {
                cachePolicy = BmobQuery.CachePolicy.CACHE_THEN_NETWORK
            }
            onListSuccess = {
                LogUtil.sd(it)
                it.forEach {
                    pool[it.title] = it.content
                }
            }
        }
    }

    @JvmStatic
    fun ensureLoaded() {
        if (pool.isEmpty()) {
            load()
        }
    }

    @JvmStatic
    fun of(UiId: String): MetaPermission? {
        LogUtil.d("$UiId in $pool")
        return pool[UiId]
    }
}

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
    fun check(permission: MetaPermission, callback: PermissionCallback.() -> Unit) {
        val ownsPermission = UserContext.ownsPermission
        checkPermission {
            target = mapOf(permission to PermissionCallback().apply(callback))
            owns = ownsPermission
        }
    }

    @JvmStatic
    fun checkById(permissionId: String, callback: PermissionCallback.() -> Unit) {
        val p = MetaPermissionPool.of(permissionId) ?: return
        check(p, callback)
    }

    @JvmStatic
    fun check(permissions: Map<MetaPermission, PermissionCallback>) {
        val ownsPermission = UserContext.ownsPermission
        checkPermission {
            target = permissions
            owns = ownsPermission
        }
    }

    @JvmStatic
    fun checkById(permissions: Map<String, PermissionCallback>) {
        val pMap = HashMap<MetaPermission, PermissionCallback>()
        for (permissionId in permissions.keys) {
            val p = MetaPermissionPool.of(permissionId) ?: continue
            val pCallback = permissions[permissionId] ?: continue
            pMap[p] = pCallback
        }
        check(pMap)
    }
}

