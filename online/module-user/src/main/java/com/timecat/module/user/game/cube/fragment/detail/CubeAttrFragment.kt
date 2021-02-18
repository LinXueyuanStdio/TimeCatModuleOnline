package com.timecat.module.user.game.cube.fragment.detail

import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.data.bmob.ext.bmob.useItem
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.module.user.R
import com.timecat.module.user.ext.simpleErrorCallback
import com.timecat.module.user.game.cube.*
import com.timecat.module.user.view.dsl.Content
import com.timecat.module.user.view.item.ContentItem
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
    lateinit var contentItem: ContentItem
    lateinit var cubeLevelBar: CubeLevelItem
    override fun loadDetail(ownCube: OwnCube) {
        container.apply {
            container.removeAllViews()
            cubeLevelBar = CubeLevelBar(ownCube.maxLevel, ownCube.exp)
            contentItem = Content(ownCube.cube.content)
            if (ownCube.reachMaxExp()) {
                MaterialButton("突破") {
                    onLevelBreak()
                }
            } else {
                MaterialButton("升级") {
                    onLevelUp(it as TextView)
                }
            }
        }
    }

    fun onLevelBreak() {
        val level = cubeViewModel.maxLevel.value ?: 1
        val exp = cubeViewModel.exp.value ?: 0L
        requireActivity().showLevelBreakDialog(level, exp)
    }

    fun onLevelUp(button: TextView) {
        fetchExpItems(button, I()) {
            button.hideProgress()
            val level = cubeViewModel.maxLevel.value ?: 1
            val exp = cubeViewModel.exp.value ?: 0L
            val id = cubeViewModel.objectId.value ?: ""
            requireActivity().showLevelUpDialog(level, exp, it) { dialog, btn, expOwnItem, count ->
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
        cubeViewModel.cubeLevelBar.observe(viewLifecycleOwner) {
            if (::cubeLevelBar.isInitialized) {
                val (maxLevel, exp) = it
                cubeLevelBar.maxLevel = maxLevel
                cubeLevelBar.exp = exp
            }
        }
        cubeViewModel.cube.observe(viewLifecycleOwner) {
            if (::contentItem.isInitialized) {
                contentItem.setRichText(it.content)
            }
        }
    }

    fun fetchExpItems(button: TextView, user: User, useItems: (List<OwnItem>) -> Unit) {
        button.showProgress()
        cubeViewModel.attachLifecycle = requestOwnItem {
            query = user.allOwnExpItem()
            onEmpty = {
                MaterialDialog(requireActivity()).show {
                    message(text = "未持有任何经验！")
                    positiveButton(R.string.ok)
                }
            }
            onSuccess = {
                button.hideProgress("升级")
                useItems(it)
            }
            onError = simpleErrorCallback
            onComplete = { button.hideProgress("升级") }
        }
    }
}