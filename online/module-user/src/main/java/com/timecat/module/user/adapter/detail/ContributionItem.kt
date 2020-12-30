package com.timecat.module.user.adapter.detail

import android.app.Activity
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import cn.bmob.v3.BmobQuery
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.data.common.Block
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.module.user.R
import com.timecat.module.user.view.ContributionsDay
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.android.synthetic.main.user_base_item_contribution.view.*
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json.JSONArray
import org.json.JSONException
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 一个作者
 * @usage 可关注作者，点击进入作者个人详情页
 */
class ContributionItem(
    val activity: Activity,
    val user: _User,
) : BaseDetailItem<ContributionItem.DetailVH>("贡献") {

    class DetailVH(val root: View, adapter: FlexibleAdapter<*>) : BaseDetailVH(root, adapter)

    override fun getLayoutRes(): Int = R.layout.user_base_item_contribution

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<*>>
    ): DetailVH = DetailVH(view, adapter)

    val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd")

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<*>>,
        holder: DetailVH,
        position: Int,
        payloads: MutableList<Any>?
    ) {
        super.bindViewHolder(adapter, holder, position, payloads)
        holder.apply {
            val query = BmobQuery<Block>()
            query.addWhereEqualTo("user", user)
            query.groupby(arrayOf("createdAt"))
            query.setHasGroupCount(true)
            query.findStatistics(Block::class.java, object : QueryListener<JSONArray?>() {
                override fun done(jsonArray: JSONArray?, e: BmobException?) {
                    //[{"_count":2,"createdAt":"2020-10-07"},
                    // {"_count":12,"createdAt":"2020-04-11"},
                    // {"_count":1,"createdAt":"2020-06-10"},
                    // {"_count":2,"createdAt":"2020-08-23"}]
                    if (e != null) return
                    val days: MutableList<ContributionsDay> = ArrayList()
                    val map: MutableMap<Int, ContributionsDay> = HashMap()
                    jsonArray?.let {
                        try {
                            for (i in 0 until it.length()) {
                                it.getJSONObject(i)?.let { jsonObject ->
                                    val time: String = jsonObject.getString("createdAt")
                                    val datetime = dateTimeFormat.parseDateTime(time)
                                    val count: Int = jsonObject.getInt("_count")
                                    val level = when {
                                        count >= 80 -> 4
                                        count >= 30 -> 3
                                        count >= 10 -> 2
                                        count > 0 -> 1
                                        else -> 0
                                    }
                                    val day = ContributionsDay(
                                        datetime.year, datetime.monthOfYear, datetime.dayOfMonth,
                                        level, 10
                                    )
                                    map.put(day.key(), day)
                                }
                            }
                            var dateTime = DateTime().minusWeeks(54)
                            for (j in 0 until 54 * 7) {
                                dateTime = dateTime.plusDays(1)
                                val day = map.get(dateTime.dayOfYear) ?: ContributionsDay(
                                    dateTime.year, dateTime.monthOfYear, dateTime.dayOfMonth,
                                    0, 0
                                )
                                days.add(day)
                            }
                            root.contributionView.apply {
                                loadContributionDays(days)
                            }
                        } catch (e1: JSONException) {
                            e1.printStackTrace()
                        }
                    }
                }
            })
        }
    }

}