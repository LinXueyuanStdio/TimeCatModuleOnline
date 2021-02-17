package com.timecat.module.user.game.cube.fragment.detail

import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.ext.bmob.requestBlockRelation
import com.timecat.data.bmob.ext.net.findAllRole
import com.timecat.module.user.ext.AvatarHead

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description 话题的详细信息
 * 创建者，关注，点赞数等
 * @usage null
 */
class CubeRolesFragment : BaseCubeFragment() {

    override fun loadDetail(ownCube: OwnCube) {
        val block = ownCube.cube
        mStatefulLayout?.showLoading()
        cubeViewModel.attachLifecycle = requestBlockRelation {
            query = block.findAllRole()
            onError = {
                mStatefulLayout?.showError {
                    loadDetail(ownCube)
                }
            }
            onEmpty = {
                LogUtil.e("empty")
                mStatefulLayout?.showContent()
            }
            onSuccess = {
                roles = it.map { it.to }
                mStatefulLayout?.showContent()
            }
        }
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