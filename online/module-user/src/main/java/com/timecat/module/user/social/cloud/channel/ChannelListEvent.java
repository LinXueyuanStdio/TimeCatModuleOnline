package com.timecat.module.user.social.cloud.channel;

import com.cheng.channel.Channel;

import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-10-27
 * @description null
 * @usage null
 */
public class ChannelListEvent {
    private List<Channel> channelList;

    public ChannelListEvent(List<Channel> channelList) {
        this.channelList = channelList;
    }

    public List<Channel> getChannelList() {
        return channelList;
    }
}
