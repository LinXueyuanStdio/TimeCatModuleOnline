package com.timecat.module.user.game.cube.fragment.detail

import com.afollestad.materialdialogs.MaterialDialog
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.data.bmob.ext.bmob.useItem
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.module.user.R
import com.timecat.module.user.ext.simpleErrorCallback
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
        container.apply {
            cubeLevelBar = CubeLevelBar(ownCube.maxLevel, ownCube.exp)
            if (ownCube.reachMaxExp()) {
                MaterialButton("突破") { onLevelBreak() }
            } else {
                MaterialButton("升级") { onLevelUp() }
            }
        }
    }

    fun onLevelBreak() {
        val level = cubeViewModel.maxLevel.value ?: 1
        val exp = cubeViewModel.exp.value ?: 0L
        requireActivity().showLevelBreakDialog(level, exp)
    }

    fun onLevelUp() {
        fetchExpItems(I()) {
            val level = cubeViewModel.maxLevel.value ?: 1
            val exp = cubeViewModel.exp.value ?: 0L
            val id = cubeViewModel.objectId.value ?: ""
            requireActivity().showLevelUpDialog(level, exp, it) { dialog, btn, expOwnItem, count ->
                btn.showProgress()
                cubeViewModel.attachLifecycle = useItem<Any?>(expOwnItem.objectId, count, id) {
                    onSuccess = {
                        val fakeExp = id2exp(expOwnItem.objectId) * count
                        val trueExp = cubeViewModel.exp.value ?: 0L + fakeExp
                        cubeViewModel.exp.postValue(trueExp)
                        dialog.dismiss()
                    }
                    onError = simpleErrorCallback
                    onComplete = { btn.hideProgress() }
                }
            }
        }
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        cubeViewModel.cubeLevelBar.observe(this) {
            LogUtil.e("$it")
            val (maxLevel, exp) = it
            cubeLevelBar.maxLevel = maxLevel
            cubeLevelBar.exp = exp
        }
    }

    fun fetchExpItems(user: User, useItems: (List<OwnItem>) -> Unit) {
        mStatefulLayout?.showLoading()
        cubeViewModel.attachLifecycle = requestOwnItem {
            query = user.allOwnExpItem()
            onEmpty = {
                mStatefulLayout?.showContent()
                MaterialDialog(requireActivity()).show {
                    message(text = "未持有任何经验！")
                    positiveButton(R.string.ok)
                }
            }
            onError = {
                mStatefulLayout?.showContent()
                ToastUtil.e("出现错误：$it")
            }
            onSuccess = {
                mStatefulLayout?.showContent()
                useItems(it)
            }
        }
    }
}