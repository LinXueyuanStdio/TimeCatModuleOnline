package com.timecat.module.user.game.cube.fragment.detail

import com.afollestad.materialdialogs.MaterialDialog
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.data.bmob.ext.bmob.useItem
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.module.user.R
import com.timecat.module.user.game.cube.*
import com.timecat.module.user.view.item.CubeLevelItem

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
    lateinit var cubeLevelBar: CubeLevelItem
    override fun loadDetail(ownCube: OwnCube) {
        val I = I()
        container.apply {
            cubeLevelBar = CubeLevelBar(ownCube.maxLevel, ownCube.exp)
            if (ownCube.reachMaxExp()) {
                MaterialButton("突破") {
                    requireActivity().showLevelBreakDialog(I, ownCube)
                }
            } else {
                MaterialButton("升级") {
                    fetchExpItems(I) {
                        requireActivity().showLevelUpDialog(it, ownCube) { dialog, expOwnItem, count ->
                            useItem<Any?>(expOwnItem.objectId, count) {
                                onSuccess = {
                                    dialog.dismiss()
                                }
                                onError = {
                                    ToastUtil.e_long("发生错误：$it")
                                }
                            }
                            cubeViewModel.reloadCurrentCube()
                        }
                    }
                }
            }
        }
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        cubeViewModel.cubeLevelBar.observe(this) {
            val (maxLevel, exp) = it
            cubeLevelBar.maxLevel = maxLevel
            cubeLevelBar.exp = exp
        }
    }

    fun fetchExpItems(user: User, useItems: (List<OwnItem>) -> Unit) {
        requestOwnItem {
            query = user.allOwnExpItem()
            onEmpty = {
                MaterialDialog(requireActivity()).show {
                    message(text = "未持有任何经验！")
                    positiveButton(R.string.ok)
                }
            }
            onError = {
                ToastUtil.e("出现错误：$it")
            }
            onSuccess = {
                useItems(it)
            }
        }
    }
}