package com.timecat.module.user.game.cube

import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.net.allDataItem
import com.timecat.data.bmob.ext.net.allOwnItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.layout.ui.business.form.*
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.business.setting.RewardItem
import com.timecat.layout.ui.business.setting.RewardListItem
import com.timecat.layout.ui.drawabe.roundBorderSelector
import com.timecat.layout.ui.layout.padding
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
const val expBlock1 = "602a44e09c05de425460028f" //日常经验
const val expBlock2 = "602a4b7e9c05de425460047e" //专业经验
const val expBlock3 = "602a4cba286a8427b94dde52" //顿悟经验
const val expBlock4 = "602a4d299c05de42546004c6" //实践经验
const val expBlock5 = "602a4f2b286a8427b94ddeb2" //造物经验
fun isExpItem(id: String): Boolean = when (id) {
    expBlock1, expBlock2, expBlock3, expBlock4, expBlock5 -> true
    else -> false
}

fun allExpItem() = allDataItem().apply {
    whereContainedIn("objectId", listOf(
        expBlock1,
        expBlock2,
        expBlock3,
        expBlock4,
        expBlock5,
    ))
}

fun id2exp(id: String): Long = when (id) {
    expBlock1 -> 60
    expBlock2 -> 300
    expBlock3 -> 1500
    expBlock4 -> 7500
    expBlock5 -> 37500
    else -> 0
}

fun User.allOwnExpItem() = allOwnItem().apply {
    whereMatchesQuery("item", allExpItem())
}

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
fun FragmentActivity.showLevelBreakDialog(user: User, ownCube: OwnCube) {
    val container = ContainerItem(this)
    container.apply {
        CubeLevelBar(ownCube.maxLevel, ownCube.exp)
    }
    MaterialDialog(this, BottomSheet()).show {
        title(text = "突破")
        customView(view = container)
        positiveButton(R.string.ok) {

        }
        negativeButton(R.string.cancel)
    }
}

/**
 * 显示升级窗口
 */
fun FragmentActivity.showLevelUpDialog(
    expItems: List<OwnItem>,
    ownCube: OwnCube,
    onUsed: (dialog: MaterialDialog, expItem: OwnItem, count: Int) -> Unit
) {
    val expItemSelector = RewardListItem(this)
    val id2Item = mutableMapOf<String, OwnItem>()
    for (i in expItems) {
        id2Item[i.objectId] = i
    }
    val rewards = expItems.map {
        val block = it.item
        val header = ItemBlock.fromJson(block.structure)
        val avatar = header.header.avatar
        val name = block.title
        RewardItem.Reward(it.objectId, avatar, it.count.toLong(), name)
    }
    var currentOwnExpItem = expItems[0]
    var currentExpItem = currentOwnExpItem.item
    val container = ContainerItem(this)
    MaterialDialog(this, BottomSheet()).show {
        title(text = "升级")
        container.apply {
            padding = 10
            val expBar = CubeLevelBar(ownCube.maxLevel, ownCube.exp, autoAdd = false)
            val stepSliderItem = StepSlide(
                from = 1f, to = currentOwnExpItem.count.toFloat(), step = 1f,
                defaultValue = 1f, autoAdd = false
            )
            val usage = CenterBody("获得经验", autoAdd = false)
            val text = "使用"
            val button = MaterialButton("$text 1 个", autoAdd = false) {
                onUsed(this@show, currentOwnExpItem, stepSliderItem.value.toInt())
            }

            fun useExpItem(expUnit: Long, count: Int) {
                expBar.fakeExp = count * expUnit
                button.text = "${text} $count"
                usage.text = "${text} $count 个，获得 ${count * expUnit} 经验"
            }

            fun refresh() {
                val count = stepSliderItem.value.toInt()
                val expUnit = id2exp(currentExpItem.objectId)
                useExpItem(expUnit, count)
            }

            fun select(it: RewardItem) {
                expItemSelector.container.forEach {
                    it.isSelected = false
                }
                it.isSelected = true
            }
            expItemSelector.onClick = {
                val ownItem = id2Item[it.reward.uuid]
                if (ownItem != null) {
                    select(it)
                    val count = ownItem.count
                    if (count <= 1) {
                        stepSliderItem.beGone()
                        stepSliderItem.valueTo = 2f
                        stepSliderItem.value = 1f
                    } else {
                        stepSliderItem.beVisible()
                        stepSliderItem.valueTo = count.toFloat()
                        stepSliderItem.value = 1f
                    }
                    currentOwnExpItem = ownItem
                    currentExpItem = ownItem.item
                    refresh()
                }
            }

            expItemSelector.rewards = rewards
            val accentColor = Attr.getAccentColor(context)
            expItemSelector.container.forEach {
                it.background = roundBorderSelector(accentColor)
            }

            stepSliderItem.onSlide {
                val count = it.toInt()
                val expUnit = id2exp(currentExpItem.objectId)
                useExpItem(expUnit, count)
            }
            add(
                expBar,
                expItemSelector,
                usage,
                stepSliderItem,
                button
            )
            val count = stepSliderItem.value.toInt()
            val expUnit = id2exp(currentExpItem.objectId)
            useExpItem(expUnit, count)

        }
        customView(view = container)
    }
}
