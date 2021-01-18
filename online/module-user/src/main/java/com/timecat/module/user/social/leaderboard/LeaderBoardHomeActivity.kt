package com.timecat.module.user.social.leaderboard

import android.view.Menu
import android.view.MenuItem
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allLeaderBoard
import com.timecat.data.bmob.ext.net.allTopic
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.R
import com.timecat.module.user.adapter.block.BlockSmallItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.xiaojinzi.component.anno.RouterAnno
import java.util.*

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/4/17
 * @description 所有排行榜
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.LEADERBOARD_LeaderBoardHomeActivity)
class LeaderBoardHomeActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "造物榜"
    override fun query() = allLeaderBoard()
    override fun block2Item(block: Block) = BlockSmallItem(this, block)

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.leaderboard_leaderboard, menu)//排行榜规则、推荐到排行榜的推荐列表
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == android.R.id.home) {
            finish()
            return true
        }
        if (i == R.id.recommend) {
            NAV.go(this, RouterHub.LEADERBOARD_RecommendActivity)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}