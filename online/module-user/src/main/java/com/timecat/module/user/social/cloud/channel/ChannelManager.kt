package com.timecat.module.user.social.cloud.channel

import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.CacheDoubleUtils
import com.cheng.channel.Channel
import com.timecat.component.router.app.NAV
import com.timecat.layout.ui.standard.navi.TabBlockItem
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-10-27
 * @description null
 * @usage null
 */
object ChannelManager {
    @JvmStatic
    private val channels = LinkedHashMap<String, List<Channel>>()
    private const val key = "UserChannelList2"
    private val myChannel = arrayOf(UserChannel.Focus, UserChannel.World)
    private val recommendChannel1 = arrayOf(
        UserChannel.Recommend
    )
    private val recommendChannel2 = arrayOf(
        UserChannel.Forum
    )

    @JvmStatic
    val data: LinkedHashMap<String, List<Channel>>
        get() {
            buildChannels()
            return channels
        }

    private fun buildChannels() {
        val myChannelList = myChannels
        channels["我的视图"] = myChannelList
        channels["私人"] = getSelectableChannels(recommendChannel1, myChannelList)
        channels["可选"] = getSelectableChannels(recommendChannel2, myChannelList)
    }

    private fun getSelectableChannels(
        recommendChannel: Array<UserChannel>,
        myChannelList: List<Channel>
    ): List<Channel> {
        val recommendChannelList: MutableList<Channel> = ArrayList()
        for (aMyChannel in recommendChannel) {
            var isSelected = false
            for (channel in myChannelList) {
                if (aMyChannel.title == channel.channelName) {
                    isSelected = true
                    break
                }
            }
            if (isSelected) continue
            recommendChannelList.add(getChannelByEnum(aMyChannel))
        }
        return recommendChannelList
    }

    @JvmStatic
    fun getItemByEnum(channel: UserChannel): TabBlockItem {
        return TabBlockItem(
            0, channel.title,
            channel.imagePath,
            channel.fragmentRouterPath,
            channel.actionRouterPath
        )
    }

    fun getChannelByEnum(recordChannel: UserChannel): TabChannel {
        return TabChannel(recordChannel.title, getItemByEnum(recordChannel))
    }

    @JvmStatic
    val myChannels: List<Channel>
        get() {
            val cache = CacheDoubleUtils.getInstance()
            val history = cache.getString(key)
            val myChannelList = ArrayList<Channel>()
            if (!TextUtils.isEmpty(history)) {
                val titleList = JSONArray.parseArray(history, String::class.java)
                for (s in titleList) {
                    UserChannel.values().firstOrNull { it.title == s }?.let {
                        myChannelList.add(getChannelByEnum(it))
                    }
                }
            } else {
                myChannelList.add(getChannelByEnum(UserChannel.Focus))
                myChannelList.add(getChannelByEnum(UserChannel.World))
            }
            return myChannelList
        }

    @JvmStatic
    fun buildFragmentFromChannel(channel: Channel): Fragment {
        val tab = channel as TabChannel
        var item: TabBlockItem? = null
        if (tab.obj is JSONObject) {
            item = (tab.obj as JSONObject).toJavaObject(TabBlockItem::class.java) as TabBlockItem
        } else if (tab.obj is TabBlockItem) {
            item = tab.obj as TabBlockItem
        }
        return NAV.fragment(item?.fragmentRouterPath)
    }

    /**
     * 将 channelList 保存为 stateList
     *
     * @param channelList channel 的 title 有对 state 的简单描述
     */
    @JvmStatic
    fun saveChannel(channelList: List<String>) {
        val cache = CacheDoubleUtils.getInstance()
        val history = JSONArray.toJSONString(channelList)
        cache.put(key, history)
    }

}