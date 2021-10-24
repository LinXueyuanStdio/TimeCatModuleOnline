package com.timecat.module.user.game.cube

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.identity.data.block.ForumBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseBlockCollapseActivity
import com.timecat.module.user.base.BaseBlockDetailActivity
import com.timecat.module.user.view.TopicCard
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage 方块相关的评论、帖子、动态等等，不包括方块的养成方面
 * 如果要访问方块的养成，可以访问 AllCubeActivity
 * 这个页面一般用来展示用户未拥有的那些方块的详情
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeActivity)
class CubeActivity : BaseBlockDetailActivity() {

    override fun title(): String = "方块"

    @AttrValueAutowiredAnno("blockId")
    var blockId: String = ""

    lateinit var card: TopicCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        card = TopicCard(this)
        card.setPlaceholderHeight(getStatusBarHeightPlusToolbarHeight())
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    override fun focusCreatorInHeader(): Boolean = false
    override fun loadDetail(block: Block) {
        super.loadDetail(block)
        // 1. 加载头部卡片
        val headerBlock = ForumBlock.fromJson(block.structure)
        titleString = block.title
        card.apply {
            title = block.title
            desc = "点赞 ${block.likes}  讨论 ${block.comments}  分享 ${block.relays}"
            icon = headerBlock.header?.icon ?: "R.drawable.ic_launcher"
            setupFollowBlockButton(context, button, block)
        }
    }

    override fun fetch() {
        fetch(blockId)
    }
}