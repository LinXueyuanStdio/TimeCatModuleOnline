package com.timecat.module.user.permission


import androidx.lifecycle.LifecycleOwner
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
import com.timecat.module.user.game.task.rule.ActivityContext
import com.timecat.module.user.game.task.rule.LoadableContext
import com.timecat.module.user.game.task.rule.bindLoadableContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/22
 * @description 用户上下文
 * @usage null
 */
class UserContext(
    private val owner: LifecycleOwner,
    val user: User,
    private val onLoading: () -> Unit,
    private val onLoaded: (ActivityContext) -> Unit
) : LoadableContext() {
    var I: User? = UserDao.getCurrentUser()

    val identity: MutableList<Block> = mutableListOf()
    val role: MutableList<Block> = mutableListOf()

    /**
     * 身份的角色
     */
    val roleOfIdentity: MutableList<Block> = mutableListOf()
    val hunPermission: MutableList<Block> = mutableListOf()

    /**
     * 角色的权限
     */
    val hunPermissionOfRole: MutableList<Block> = mutableListOf()
    var ownsPermission: MutableList<HunPermission> = mutableListOf()

    fun load() {
        onLoading()
        owner.bindLoadableContext(this)
        loadInterAction(user)
    }

    private fun loadInterAction(user: User) {
        LogUtil.sd(user)
        this attach requestOwnCube {
            query = user.allOwnCube().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                clearInterAction()
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

    private fun clearInterAction() {
        identity.clear()
    }

    private fun loadInterAction(data: OwnCube) {
        identity.add(data.cube)
    }

    private fun loadRole() {
        LogUtil.sd(identity)
        if (identity.isEmpty()) {
            return
        }
        requestBlockRelation {
            query = identity.findAllRoles().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                roleOfIdentity.clear()
            }
            onSuccess = {
                LogUtil.d(it)
                roleOfIdentity.clear()
                roleOfIdentity.addAll(it.map { it.to })
                loadHunPermission()
            }
        }
    }

    private fun loadHunPermission() {
        role.clear()
        role.addAll(roleOfIdentity)
        LogUtil.d(role)
        if (role.isEmpty()) {
            return
        }
        requestBlockRelation {
            query = role.findAllHunPermission().apply {
                cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
            }
            onEmpty = {
                LogUtil.sd("empty")
                hunPermissionOfRole.clear()
            }
            onSuccess = {
                LogUtil.d(it)
                hunPermissionOfRole.clear()
                hunPermissionOfRole.addAll(it.map { it.to })
                loadOwnsPermission()
            }
        }
    }

    private fun loadOwnsPermission() {
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