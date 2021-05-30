package com.timecat.module.user.permission


import androidx.lifecycle.LifecycleOwner
import cn.leancloud.AVQuery
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.net.allMetaPermission
import com.timecat.data.bmob.ext.net.allOwnCube
import com.timecat.data.bmob.ext.net.findAllHunPermission
import com.timecat.data.bmob.ext.net.findAllRoles
import com.timecat.data.bmob.ext.toDataError
import com.timecat.middle.block.permission.HunPermission
import com.timecat.middle.block.permission.MetaPermission
import com.timecat.middle.block.permission.Why
import com.timecat.module.user.game.task.rule.LoadableContext
import com.timecat.module.user.game.task.rule.bindLoadableContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
    private val onLoaded: (UserContext) -> Unit
) : LoadableContext() {
    val identity: MutableList<Block> = mutableListOf()
    val role: MutableList<Block> = mutableListOf()

    /**
     * 元权限池
     * 云端合法的所有的元权限
     */
    var pool: MutableMap<String, MetaPermission> = HashMap()

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

    data class PermissionsAndOwnCubes(
        val permissions: List<Block>? = null,
        val ownCubes: List<OwnCube>? = null,
    )

    private fun loadInterAction(user: User) {
        LogUtil.sd(user)
        val getAllMetaPermissions = allMetaPermission().apply {
            cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
        }.findInBackground()
        val getAllOwnCubes = user.allOwnCube().apply {
            cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
        }.findInBackground()
        this attach Observable.zip(getAllMetaPermissions, getAllOwnCubes) { permissions, ownCubes ->
            PermissionsAndOwnCubes(permissions, ownCubes)
        }.map {
            if (it.permissions.isNullOrEmpty()) {
                pool.clear()
            } else {
                pool.clear()
                it.permissions.forEach {
                    pool[it.title] = it.content
                }
            }
            if (it.ownCubes.isNullOrEmpty()) {
                identity.clear()
            } else {
                identity.clear()
                it.ownCubes.forEach {
                    identity.add(it.cube)
                }
            }
            LogUtil.sd("identity: len=${identity.size}")
            for (i in identity) {
                LogUtil.sd("${i.title} -> ${i.content}")
            }
            identity
        }.flatMap {
            if (it.isEmpty()) {
                Observable.just(listOf())
            } else {
                it.findAllRoles().apply {
                    cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
                }.findInBackground()
            }
        }.map {
            roleOfIdentity.clear()
            role.clear()
            if (!it.isNullOrEmpty()) {
                roleOfIdentity.addAll(it.map { it.to })
                role.addAll(roleOfIdentity)
            }
            LogUtil.sd("role: len=${role.size}")
            for (i in role) {
                LogUtil.sd("${i.title} -> ${i.content}")
            }
            role
        }.flatMap {
            if (it.isEmpty()) {
                Observable.just(listOf())
            } else {
                it.findAllHunPermission().apply {
                    cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
                }.findInBackground()
            }
        }.map {
            hunPermissionOfRole.clear()
            hunPermission.clear()
            ownsPermission.clear()
            if (!it.isNullOrEmpty()) {
                hunPermissionOfRole.addAll(it.map { it.to })
                hunPermission.addAll(hunPermissionOfRole)
            }
            ownsPermission.addAll(hunPermission.map {
                HunPermission(it.content.toRegex(), it.content, Why("您拥有权限 ${it.title}"))
            })
            LogUtil.sd("Hun: len=${ownsPermission.size}")
            for (i in ownsPermission) {
                LogUtil.sd("${i.uri} -> ${i.checker}")
            }
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onLoaded(this) }, { LogUtil.e(it.toDataError()) })
    }

    fun of(UiId: String): MetaPermission? {
        LogUtil.d("$UiId \nin\n$pool")
        return pool[UiId]
    }
}