package com.timecat.module.user.base

import android.app.Activity
import android.content.Intent
import com.shuyu.textutillib.model.TopicModel
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.net.allTopic
import com.timecat.module.user.adapter.block.BlockSmallItem


class TopicListBlockActivity : BaseSelectBlockActivity() {

    override fun title(): String = "选择话题"
    override fun query() = allTopic()
    override fun block2Item(block: Block) = BlockSmallItem(this, block) {
        val intent = Intent()
        intent.putExtra(DATA, TopicModel(block.title, block.objectId))
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    companion object {
        const val DATA = "data"
    }
}
