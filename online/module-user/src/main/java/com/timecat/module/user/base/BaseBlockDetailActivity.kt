package com.timecat.module.user.base

import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.timecat.data.bmob.data.common.Block
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.social.share.showMore
import com.timecat.module.user.view.CommentFooterView
import com.timecat.module.user.view.ToolbarHeadView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-24
 * @description 动态、评论
 * @usage null
 */
abstract class BaseBlockDetailActivity : BaseBlockCollapseActivity() {
    lateinit var userHerf: ToolbarHeadView
    lateinit var footer: CommentFooterView
    lateinit var more: View
    override fun layout(): Int = R.layout.user_detail_collapse_viewpager_footer
    override fun bindView() {
        super.bindView()
        userHerf = findViewById(R.id.user_herf)
        footer = findViewById(R.id.footer)
        more = findViewById(R.id.more_dialog)
    }

    open fun focusCreatorInHeader(): Boolean = true

    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        if (focusCreatorInHeader()) {
            userHerf.head.bindBlock(block.user)
        } else {
            userHerf.head.bindBlock(block)
        }
        userHerf.title.text = title()
        footer.bindBlock(this, block)
        footer.response.setShakelessClickListener {
            GO.addCommentFor(block)
        }
        footer.comment.setShakelessClickListener {
            collapseContainer.setScrimsShown(true, true)
        }
        more.setShakelessClickListener {
            showMore(supportFragmentManager, block)
        }
    }

    override fun setupCollapse() {
        setupCollapse { appBarLayout.totalScrollRange }
    }

    fun setupCollapse(visHeight: () -> Int) {
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
//            LogUtil.se("${i} / ${visHeight()}")
            //滑动超过 head的可见高度，则显示 userHerf
            userHerf.displayedChild = if (Math.abs(i) >= visHeight()) 1 else 0
        })
    }
}