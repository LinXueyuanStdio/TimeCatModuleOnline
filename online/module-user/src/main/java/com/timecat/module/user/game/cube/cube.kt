package com.timecat.module.user.game.cube

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.layout.ui.business.form.wrapContext
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.module.user.R
import com.timecat.module.user.game.core.Level
import com.timecat.module.user.view.item.CubeLevelItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/15
 * @description null
 * @usage null
 */

/**
 * 方块的数据
 */
object CubeLevel {
    /**
     * 等级为 level 时的最大经验值限制
     */
    fun expLimit(level: Int): Long = 10 * Level.expLimit(level)
    fun getLevel(exp: Long) = Level.getLevel(exp) { expLimit(it) }

    /**
     * 等级为 level 时的最大累计经验值限制
     */
    fun expAccLimit(level: Int): Long = Level.expAccLimit(level) { expLimit(it) }
}

fun OwnCube.reachMaxExp(): Boolean {
    val (level, curExp) = CubeLevel.getLevel(exp)
    val expLimit = CubeLevel.expLimit(level)
    return curExp >= expLimit - 1 && level >= maxLevel
}

fun ViewGroup.CubeLevelBar(
    maxLevel: Int,
    exp: Long,
    style: Int? = null,
    autoAdd: Boolean = true
) = CubeLevelItem(style.wrapContext(context)).apply {
    this.maxLevel = maxLevel
    this.exp = exp
}.also { if (autoAdd) addView(it) }

/**
 * 显示突破窗口
 */
fun FragmentActivity.showLevelBreakDialog(ownCube: OwnCube) {
    val container = ContainerItem(this)
    container.apply {
        CubeLevelBar(ownCube.maxLevel, ownCube.exp)
    }
    MaterialDialog(this, BottomSheet()).show {
        title(text = "升级")
        customView(view = container)
        positiveButton(R.string.ok) {

        }
        negativeButton(R.string.cancel)
    }
}

/**
 * 显示升级窗口
 */
fun FragmentActivity.showLevelUpDialog(ownCube: OwnCube) {
    val container = ContainerItem(this)
    container.apply {

    }
    MaterialDialog(this, BottomSheet()).show {
        title(text = "升级")
        customView(view = container)
        positiveButton(R.string.ok) {

        }
        negativeButton(R.string.cancel)
    }
}
