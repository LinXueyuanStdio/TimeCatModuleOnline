package com.timecat.module.user.game.cube.fragment.detail

import android.widget.Button
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.data.bmob.ext.bmob.useItem
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.layout.setShakelessClickListener
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
    lateinit var button: Button

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

    override fun loadDetail(ownCube: OwnCube) {
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        container.apply {
            cubeLevelBar = CubeLevelBar(1, 0)
            contentItem = Content("")
            button = MaterialButton("升级") {}
        }
        cubeViewModel.cubeLevelBar.observe(viewLifecycleOwner) {
            val (maxLevel, exp) = it
            cubeLevelBar.maxLevel = maxLevel
            cubeLevelBar.exp = exp
            if (reachMaxExp(exp, maxLevel)) {
                button.text = "突破"
                button.setShakelessClickListener {
                    onLevelBreak()
                }
            } else {
                button.text = "升级"
                button.setShakelessClickListener {
                    onLevelUp(it as TextView)
                }
            }
        }
        cubeViewModel.cube.observe(viewLifecycleOwner) {
            contentItem.setRichText(it.content)
        }
    }

    fun fetchExpItems(button: TextView, user: User, useItems: (List<OwnItem>) -> Unit) {
        button.showProgress()
        cubeViewModel attach requestOwnItem {
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