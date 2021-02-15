package com.timecat.module.user.game.cube.fragment.detail

import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.module.user.game.cube.CubeLevelBar
import com.timecat.module.user.game.cube.reachMaxExp
import com.timecat.module.user.game.cube.showLevelBreakDialog
import com.timecat.module.user.game.cube.showLevelUpDialog

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description
 * 属性
 * 种族
 * 元素
 * 等级，升级
 * 简单的介绍
 * @usage null
 */
class CubeAttrFragment : BaseCubeFragment() {

    override fun loadDetail(ownCube: OwnCube) {
        container.apply {
            CubeLevelBar(ownCube.maxLevel, ownCube.exp)
            if (ownCube.reachMaxExp()) {
                MaterialButton("突破") {
                    requireActivity().showLevelBreakDialog(ownCube)
                }
            } else {
                MaterialButton("升级") {
                    requireActivity().showLevelUpDialog(ownCube)
                }
            }
        }
    }

}