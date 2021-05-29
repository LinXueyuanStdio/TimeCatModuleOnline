package com.timecat.module.user.game.task.fragment.card

import android.content.res.ColorStateList
import android.view.View
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.google.android.material.chip.Chip
import com.timecat.component.identity.Attr
import com.timecat.data.bmob.data.User
import com.timecat.layout.ui.business.form.Body
import com.timecat.layout.ui.business.form.H2
import com.timecat.layout.ui.business.setting.HeadItem
import com.timecat.layout.ui.layout.*
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginScrollContainerFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/29
 * @description 常驻卡池，写死吧
 * @usage null
 */
open class NormalCardPoolFragment : BaseLoginScrollContainerFragment() {
    override fun loadDetail(user: User) {
        onContentLoaded()
    }

    lateinit var titleItem: HeadItem
    lateinit var contentItem: HeadItem

    override fun bindView(view: View) {
        super.bindView(view)
        container.apply {
            val iconColor = ColorStateList.valueOf(Attr.getIconColor(context))
            orientation = VERTICAL
            View {
                layout_width = match_parent
                layout_height = 100
            }
            titleItem = H2("常驻祈愿")
            contentItem = Body("常驻")
            LinearLayout {
                orientation = HORIZONTAL
                layout_width = match_parent
                layout_height = wrap_content

                val getOne = Chip(context).apply {
                    layout_width = 0
                    layout_height = wrap_content
                    weight = 1f
                    margin = 10
                    setChipIconResource(R.drawable.ic_done)
                    chipIconTint = iconColor

                    text = "抽取一次"
                    setShakelessClickListener {
                        requestCard(1)
                    }
                }.also {
                    addView(it)
                }
                val getTen = Chip(context).apply {
                    layout_width = 0
                    layout_height = wrap_content
                    weight = 1f
                    margin = 10
                    setChipIconResource(R.drawable.ic_done)
                    chipIconTint = iconColor

                    text = "抽取十次"
                    setShakelessClickListener {
                        requestCard(10)
                    }
                }.also {
                    addView(it)
                }
            }
            View {
                layout_width = match_parent
                layout_height = 100
            }
        }
    }

    /**
     * 抽卡算法，写成云函数
     */
    open fun requestCard(count: Int = 1) {
        val cards = mutableListOf<Card>()
        for (i in 0..count) {
            cards.add(Card("", 4))
        }
        receiveCard(cards)
    }

    data class Card(
        val name: String,
        val star: Int,
    )

    fun receiveCard(cards: List<Card>) {
        MaterialDialog(requireActivity()).show {
            title(text = "常驻")
            listItems(items = cards.map { "${it.name} ${it.star}*" })
            positiveButton(R.string.ok)
        }
    }
}