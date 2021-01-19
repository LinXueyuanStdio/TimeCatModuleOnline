package com.timecat.module.user.permission

import cn.bmob.v3.BmobQuery
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.InterAction
import com.timecat.data.bmob.ext.bmob.requestBlockRelation
import com.timecat.data.bmob.ext.bmob.requestInterAction
import com.timecat.data.bmob.ext.net.allTargetedByAuth
import com.timecat.data.bmob.ext.net.findAllHunPermission
import com.timecat.data.bmob.ext.net.findAllRoles
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.data.action.INTERACTION_Auth_Identity
import com.timecat.identity.data.action.INTERACTION_Auth_Permission
import com.timecat.identity.data.action.INTERACTION_Auth_Role
import com.timecat.middle.block.permission.HunPermission
import com.timecat.middle.block.permission.Why

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/22
 * @description 用户上下文
 * @usage null
 */
object UserContext {
    @JvmStatic
    var I: _User? = UserDao.getCurrentUser()

    @JvmStatic
    val identity: MutableList<Block> = mutableListOf()

    @JvmStatic
    val role: MutableList<Block> = mutableListOf()

    /**
     * 被直接授予的角色
     */
    @JvmStatic
    val roleOfAuth: MutableList<Block> = mutableListOf()

    /**
     * 身份的角色
     */
    @JvmStatic
    val roleOfIdentity: MutableList<Block> = mutableListOf()

    @JvmStatic
    val hunPermission: MutableList<Block> = mutableListOf()

    /**
     * 被直接授予的权限
     */
    @JvmStatic
    val hunPermissionOfAuth: MutableList<Block> = mutableListOf()

    /**
     * 角色的权限
     */
    @JvmStatic
    val hunPermissionOfRole: MutableList<Block> = mutableListOf()

    @JvmStatic
    var ownsPermission: MutableList<HunPermission> = mutableListOf()

    @JvmStatic
    fun loadByUser(user: _User?) {
        I = user ?: return
        loadInterAction(user)
    }

    @JvmStatic
    fun loadInterAction(user: _User) {
        LogUtil.sd(user)
        requestInterAction {
            query = user.allTargetedByAuth().apply {
                cachePolicy = BmobQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                clearInterAction()
                loadRole()
            }
            onSuccess = {
                LogUtil.sd(it)
                clearInterAction()
                loadInterAction(it)
                loadRole()
            }
            onListSuccess = {
                LogUtil.d(it)
                clearInterAction()
                it.forEach {
                    loadInterAction(it)
                }
                loadRole()
            }
        }
    }

    @JvmStatic
    private fun clearInterAction() {
        hunPermissionOfAuth.clear()
        roleOfAuth.clear()
        identity.clear()
    }

    @JvmStatic
    private fun loadInterAction(data: InterAction) {
        when (data.type) {
            INTERACTION_Auth_Permission -> {
                hunPermissionOfAuth.add(data.block)
            }
            INTERACTION_Auth_Role -> {
                roleOfAuth.add(data.block)
            }
            INTERACTION_Auth_Identity -> {
                identity.add(data.block)
            }
        }
    }

    @JvmStatic
    fun loadRole() {
        LogUtil.sd(identity)
        if (identity.isEmpty()) {
            loadHunPermission()
            return
        }
        requestBlockRelation {
            query = identity.findAllRoles().apply {
                cachePolicy = BmobQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                roleOfIdentity.clear()
                loadHunPermission()
            }
            onSuccess = {
                LogUtil.sd(it)
                roleOfIdentity.clear()
                roleOfIdentity.add(it.to)
                loadHunPermission()
            }
            onListSuccess = {
                LogUtil.d(it)
                roleOfIdentity.clear()
                roleOfIdentity.addAll(it.map { it.to })
                loadHunPermission()
            }
        }
    }

    @JvmStatic
    fun loadHunPermission() {
        role.clear()
        role.addAll(roleOfIdentity + roleOfAuth)
        LogUtil.d(roleOfIdentity)
        LogUtil.d(roleOfAuth)
        LogUtil.d(role)
        if (role.isEmpty()) {
            loadOwnsPermission()
            return
        }
        requestBlockRelation {
            query = role.findAllHunPermission().apply {
                cachePolicy = BmobQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                hunPermissionOfRole.clear()
                loadOwnsPermission()
            }
            onSuccess = {
                LogUtil.sd(it)
                hunPermissionOfRole.clear()
                hunPermissionOfRole.add(it.to)
                loadOwnsPermission()
            }
            onListSuccess = {
                LogUtil.d(it)
                hunPermissionOfRole.clear()
                hunPermissionOfRole.addAll(it.map { it.to })
                loadOwnsPermission()
            }
        }
    }

    @JvmStatic
    fun loadOwnsPermission() {
        hunPermission.clear()
        hunPermission.addAll(hunPermissionOfAuth + hunPermissionOfRole)
        LogUtil.d(hunPermissionOfAuth)
        LogUtil.d(hunPermissionOfRole)
        LogUtil.d(hunPermission)
        ownsPermission.clear()
        ownsPermission.addAll(hunPermission.map {
            HunPermission(it.content.toRegex(), it.content, Why("您拥有权限 ${it.title}"))
        })
        LogUtil.d(ownsPermission)
    }
}