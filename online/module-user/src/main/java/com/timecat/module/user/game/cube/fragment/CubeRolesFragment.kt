package com.timecat.module.user.game.cube.fragment

import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlockRelation
import com.timecat.data.bmob.ext.net.findAllRole
import com.timecat.module.user.base.BaseBlockFragment
import com.timecat.module.user.ext.AvatarHead
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class CubeRolesFragment : BaseBlockFragment() {

    var current: Disposable? = null
    fun loadRoles(block: Block) {
        mStatefulLayout?.showLoading()
        current?.dispose()
        current = requestBlockRelation {
            query = block.findAllRole()
            onError = {
                mStatefulLayout?.showError {
                    loadRoles(block)
                }
            }
            onEmpty = {
                LogUtil.e("empty")
                roles = listOf()
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                roles = it.map { it.to }
                mStatefulLayout?.showContent()
            }
        }
        blockViewModel attach current
    }

    override fun loadDetail(block: Block) {
        loadRoles(block)
    }

    var roles: List<Block> = listOf()
        set(value) {
            container.apply {
                removeAllViews()
                value.forEach {
                    AvatarHead("R.drawable.ic_launcher", it.title, it.content)
                }
            }
            field = value
        }

}