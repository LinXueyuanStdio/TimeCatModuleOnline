package com.timecat.module.user.adapter.detail

import android.view.View
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.extend.arms.BaseApplication
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.module.user.base.GO
import com.timecat.module.user.ext.commentText
import com.timecat.module.user.ext.likeText
import com.timecat.module.user.ext.shareText
import com.timecat.module.user.ext.starText
import com.timecat.module.user.view.dsl.setupLikeBlockButton
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_action.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 点赞评论收藏分享
 * @usage
 */
class ActionItem(
    var block: Block
) : BaseDetailItem<ActionItem.DetailVH>("点赞评论收藏分享") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.user_base_item_action

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)
        holder.apply {
            root.like.setText(block.likeText())
            setupLikeBlockButton(BaseApplication.getContext(), root.like, block) {
                rebind(adapter, block)
            }
            root.share.setText(block.shareText())
            root.share.setShakelessClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setShakelessClickListener
                }
                //转发动态
                GO.relayMoment(block.parent, block)
            }
            root.comment.setText(block.commentText())
            root.comment.setShakelessClickListener {
                if (UserDao.getCurrentUser() == null) {
                    NAV.go(RouterHub.LOGIN_LoginActivity)
                    return@setShakelessClickListener
                }
                GO.addCommentFor(block)
            }
            root.star.setText(block.starText())
            root.star.setShakelessClickListener {

            }
        }
    }

    fun rebind(adapter: FlexibleAdapter<IFlexible<*>>, block: Block) {
        requestBlock {
            query = oneBlockOf(block.objectId)
            onSuccess = {
                this@ActionItem.block = it
                adapter.updateItem(this@ActionItem)
            }
            onError = {
                it.printStackTrace()
            }
        }
    }
}