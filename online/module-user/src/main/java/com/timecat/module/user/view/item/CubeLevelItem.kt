package com.timecat.module.user.view.item

import android.content.Context
import android.util.AttributeSet
import com.timecat.module.user.game.cube.CubeLevel

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class CubeLevelItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : UserLevelItem(context, attrs, defStyleAttr) {
    override var getLevel: (Long) -> Pair<Int, Long> = { CubeLevel.getLevel(it) }
    override var expLimit: (Int) -> Long = { CubeLevel.expLimit(it) }
}