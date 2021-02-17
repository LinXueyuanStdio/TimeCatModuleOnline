package com.timecat.module.user.game.cube

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.github.razir.progressbutton.bindProgressButton
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.net.allDataItem
import com.timecat.data.bmob.ext.net.allOwnItem
import com.timecat.identity.data.block.ItemBlock
import com.timecat.layout.ui.business.form.CenterBody
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.business.form.add
import com.timecat.layout.ui.business.form.wrapContext
import com.timecat.layout.ui.business.setting.ContainerItem
import com.timecat.layout.ui.business.setting.RewardItem
import com.timecat.layout.ui.business.setting.RewardListItem
import com.timecat.layout.ui.drawabe.COLOR_DEFAULT
import com.timecat.layout.ui.layout.dp
import com.timecat.layout.ui.layout.padding
import com.timecat.module.user.R
import com.timecat.module.user.game.core.Level
import com.timecat.module.user.view.item.CounterItem
import com.timecat.module.user.view.item.CubeLevelItem
import top.defaults.drawabletoolbox.DrawableBuilder
import top.defaults.drawabletoolbox.LayerDrawableBuilder
import top.defaults.drawabletoolbox.StateListDrawableBuilder
import kotlin.math.min
import kotlin.math.roundToInt

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
    fun getLevel(exp: Long) = Level.getLevel(exp) { CubeLevel.expLimit(it) }
    fun needExpToMax(accExp: Long, maxLevel: Int): Long {
        return expAccLimit(maxLevel - 1) - accExp
    }

    /**
     * 等级为 level 时的最大累计经验值限制
     */
    fun expAccLimit(level: Int): Long = Level.expAccLimit(level) { expLimit(it) }

    /**
     * 最大等级 maxLevel 限制下，已持有累计经验 currentExp
     * 现使用经验道具 itemId，有 itemCount 个
     * 返回可使用经验道具的个数 [0, itemCount]
     */
    fun maxCount(itemId: String, itemCount: Int, currentExp: Long, maxLevel: Int): Int {
        val itemExp: Long = id2exp(itemId)
        if (itemExp == 0L) return 0
        val needExp: Long = needExpToMax(currentExp, maxLevel)
        val expNeedItemCount = needExp.toFloat() / itemExp.toFloat()
        LogUtil.e("$needExp / $itemExp = $expNeedItemCount")
        return if (expNeedItemCount > itemCount) {
            itemCount
        } else expNeedItemCount.roundToInt()
    }
}

fun OwnCube.reachMaxExp(): Boolean {
    return CubeLevel.needExpToMax(exp, maxLevel) <= 1
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

fun ViewGroup.StepCounter(
    step: Int = 1,
    defaultValue: Int = 1,
    style: Int? = null,
    autoAdd: Boolean = true,
    SetValue: (Int) -> Unit = {}
): CounterItem = CounterItem(style.wrapContext(context)).apply {
    stepSize = step.toFloat()
    this.intValue = defaultValue
    onIntCount = { value ->
        SetValue(value)
    }
}.also { if (autoAdd) addView(it) }

/**
 * 显示突破窗口
 */
fun FragmentActivity.showLevelBreakDialog(
    maxLevel: Int,
    exp: Long,
) {
    val container = ContainerItem(this)
    container.apply {
        val expBar = CubeLevelBar(maxLevel, exp, autoAdd = false)
    }
    MaterialDialog(this, BottomSheet()).show {
        title(text = "突破")
        customView(view = container)
        positiveButton(R.string.ok) {

        }
        negativeButton(R.string.cancel)
    }
}

fun roundRectSolidDrawableBuilder(solidColor: Int) = DrawableBuilder()
    .rectangle()
    .rounded()
    .cornerRadius(5.dp)
    .solidColor(solidColor)

fun roundRectStrokeDrawableBuilder(strokeColor: Int) = DrawableBuilder()
    .rectangle()
    .rounded()
    .cornerRadius(5.dp)
    .strokeColor(strokeColor)

fun roundRectDashedDrawableBuilder(strokeColor: Int) = DrawableBuilder()
    .rectangle()
    .rounded()
    .hairlineBordered()
    .cornerRadius(5.dp)
    .dashed()
    .strokeColor(strokeColor)

fun roundRectSelector(
    colorDefault: Int = COLOR_DEFAULT
): Drawable {
    val layer5 = roundRectDashedDrawableBuilder(colorDefault).build()
    val normalState = layer5
    val selectedState = roundRectSolidDrawableBuilder(colorDefault).build()
    val disabledState = roundRectDashedDrawableBuilder(Color.GRAY).build()
    val pressedState = LayerDrawableBuilder()
        .add(layer5)
        .inset(10).build()
    return StateListDrawableBuilder()
        .normal(normalState)
        .pressed(pressedState)
        .selected(selectedState)
        .disabled(disabledState)
        .build()
}

/**
 * 显示升级窗口
 */
fun FragmentActivity.showLevelUpDialog(
    maxLevel: Int,
    exp: Long,
    expItems: List<OwnItem>,
    onUsed: (dialog: MaterialDialog, button: TextView, expItem: OwnItem, count: Int) -> Unit
) {
    val dialog = MaterialDialog(this, BottomSheet())
    val container = ContainerItem(this)
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
    val (currentLevel, currentExp) = CubeLevel.getLevel(exp)
    val maxNeedExp = CubeLevel.needExpToMax(exp, maxLevel)
    var currentOwnExpItem = expItems[0]
    var currentExpItem = currentOwnExpItem.item
    container.apply {
        padding = 10

        val expBar = CubeLevelBar(maxLevel, exp, autoAdd = false)
        val stepCounter = StepCounter(1, 1, autoAdd = false)
        val usage = CenterBody("获得经验", autoAdd = false)
        val button = MaterialButton("使用 1 个", autoAdd = false) {
            onUsed(dialog, it as TextView, currentOwnExpItem, stepCounter.intValue)
        }
        bindProgressButton(button)

        fun useExpItem(expUnit: Long, count: Int) {
            if (count <= 0) {
                //希望使用0个经验物品
                button.isEnabled = false
                stepCounter.minusView.isEnabled = false
                stepCounter.plusView.isEnabled = maxNeedExp > 1

                expBar.fakeExp = 0
                usage.text = "至少使用 1 个"
                button.text = "无法使用"
            } else {
                val useExp = count * expUnit
                val (fakeLevel, fakeExp) = CubeLevel.getLevel(exp + useExp)

                button.isEnabled = true
                stepCounter.minusView.isEnabled = true
                stepCounter.plusView.isEnabled = maxNeedExp - useExp > 1

                expBar.fakeExp = useExp
                button.text = "使用 $count"
                val levelUp = if (fakeLevel > currentLevel) "，等级 +${min(fakeLevel, maxLevel) - currentLevel}" else ""
                val exceedExp = if (fakeLevel >= maxLevel) "\n溢出经验 ${useExp - maxNeedExp}" else ""
                usage.text = "使用 $count 个，经验 +${count * expUnit}$levelUp$exceedExp"
            }
        }

        fun refresh() {
            stepCounter.intValue = 1
            val count = stepCounter.intValue
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
            select(it)
            id2Item[it.reward.uuid]?.let {
                currentOwnExpItem = it
                currentExpItem = it.item
                refresh()
            }
        }

        expItemSelector.rewards = rewards
        val accentColor = Attr.getAccentColor(context)
        expItemSelector.container.forEach {
            it.background = roundRectSelector(accentColor)
        }
        expItemSelector.container.get(0).isSelected = true

        stepCounter.onIntCount = { count ->
            val expUnit = id2exp(currentExpItem.objectId)
            useExpItem(expUnit, count)
        }
        add(
            expBar,
            expItemSelector,
            usage,
            stepCounter,
            button
        )

        val count = stepCounter.intValue
        val expUnit = id2exp(currentExpItem.objectId)
        useExpItem(expUnit, count)
    }
    dialog.show {
        title(text = "升级")
        customView(view = container)
    }
}
