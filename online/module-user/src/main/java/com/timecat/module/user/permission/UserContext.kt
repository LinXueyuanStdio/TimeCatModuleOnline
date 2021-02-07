package com.timecat.module.user.permission


import cn.leancloud.AVQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.bmob.requestBlockRelation
import com.timecat.data.bmob.ext.bmob.requestOwnCube
import com.timecat.data.bmob.ext.net.allOwnCube
import com.timecat.data.bmob.ext.net.findAllHunPermission
import com.timecat.data.bmob.ext.net.findAllRoles
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
    var I: User? = UserDao.getCurrentUser()

    @JvmStatic
    val identity: MutableList<Block> = mutableListOf()

    @JvmStatic
    val role: MutableList<Block> = mutableListOf()

    /**
     * 身份的角色
     */
    @JvmStatic
    val roleOfIdentity: MutableList<Block> = mutableListOf()

    @JvmStatic
    val hunPermission: MutableList<Block> = mutableListOf()

    /**
     * 角色的权限
     */
    @JvmStatic
    val hunPermissionOfRole: MutableList<Block> = mutableListOf()

    @JvmStatic
    var ownsPermission: MutableList<HunPermission> = mutableListOf()

    @JvmStatic
    fun loadByUser(user: User?) {
        I = user ?: return
        loadInterAction(user)
    }

    @JvmStatic
    fun loadInterAction(user: User) {
        LogUtil.sd(user)
        requestOwnCube {
            query = user.allOwnCube().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                clearInterAction()
                loadRole()
            }
            onSuccess = {
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
        identity.clear()
    }

    @JvmStatic
    private fun loadInterAction(data: OwnCube) {
        identity.add(data.cube)
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
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                roleOfIdentity.clear()
                loadHunPermission()
            }
            onSuccess = {
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
        role.addAll(roleOfIdentity)
        LogUtil.d(roleOfIdentity)
        LogUtil.d(role)
        if (role.isEmpty()) {
            loadOwnsPermission()
            return
        }
        requestBlockRelation {
            query = role.findAllHunPermission().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                hunPermissionOfRole.clear()
                loadOwnsPermission()
            }
            onSuccess = {
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
        hunPermission.addAll(hunPermissionOfRole)
        LogUtil.d(hunPermissionOfRole)
        ownsPermission.clear()
        ownsPermission.addAll(hunPermission.map {
            HunPermission(it.content.toRegex(), it.content, Why("您拥有权限 ${it.title}"))
        })
        LogUtil.d(ownsPermission)
    }
}