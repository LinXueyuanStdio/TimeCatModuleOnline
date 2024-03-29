package com.timecat.module.user.adapter.detail

import android.app.Activity
import android.view.View
import android.widget.TextView
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.ext.bmob.requestUserRelationCount
import com.timecat.data.bmob.ext.net.allFollow
import com.timecat.data.bmob.ext.net.fansOf
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 用户关系数据：粉丝，关注，获赞
 * @usage
 */
class UserRelationItem(
    val activity: Activity,
    var user: User
) : BaseDetailItem<UserRelationItem.DetailVH>("用户关系") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter) {
        val fans: TextView by lazy { root.findViewById(R.id.fans) }
        val follows: TextView by lazy { root.findViewById(R.id.follows) }
        val star: TextView by lazy { root.findViewById(R.id.star) }
        val creation: TextView by lazy { root.findViewById(R.id.creation) }
    }

    override fun getLayoutRes(): Int = R.layout.user_base_item_user_relation

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
            fans.apply {
                setTextColor(Attr.getPrimaryTextColor(activity))
                requestUserRelationCount {
                    query = fansOf(user)
                    onSuccess = {
                        text = "$it\n粉丝"
                    }
                    onError = {
                        it.printStackTrace()
                        text = "0\n粉丝"
                    }
                }
                setShakelessClickListener {
                    NAV.go(RouterHub.USER_UserFansListActivity, "id", user.objectId)
                }
            }
            follows.apply {
                setTextColor(Attr.getPrimaryTextColor(activity))
                requestUserRelationCount {
                    query = user.allFollow()
                    onSuccess = {
                        text = "$it\n关注"
                    }
                    onError = {
                        it.printStackTrace()
                        text = "0\n关注"
                    }
                }
                setShakelessClickListener {
                    NAV.go(RouterHub.USER_UserFollowListActivity, "id", user.objectId)
                }
            }
//            val query = AVQuery<Block>("Block")
//            query.whereEqualTo("user", user)
//            query.sum(arrayOf("likes"))
//            query.setHasGroupCount(true)
//            query.findStatistics(Block::class.java, object : QueryListener<JSONArray?>() {
//                override fun done(jsonArray: JSONArray?, e: BmobException?) {
//                    if (e == null) {
//                        try {
//                            val jsonObject: JSONObject? = jsonArray?.getJSONObject(0)
//                            val sum: Int = jsonObject?.getInt("_sumLikes") ?: return
//                            root.star.apply {
//                                text = "$sum\n获赞"
//                            }
//                            val sum2: Int = jsonObject?.getInt("_count") ?: return
//                            root.creation.apply {
//                                text = "$sum2\n造物"
//                            }
//                        } catch (e1: JSONException) {
//                            e1.printStackTrace()
//                        }
//                    }
//                }
//            })
        }
    }
}