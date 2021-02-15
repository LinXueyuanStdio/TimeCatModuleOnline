package com.timecat.module.user.game.mail

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allMail
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.adapter.game.ItemItem
import com.timecat.module.user.adapter.game.MailItem
import com.timecat.module.user.base.BaseEndlessBlockActivity
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/1/15
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllMailActivity)
class AllMailActivity : BaseEndlessBlockActivity() {
    override fun title(): String = "邮箱"
    override fun query() = allMail()
    override fun block2Item(block: Block) = MailItem(this, block)
    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(context, 4)
    }
}