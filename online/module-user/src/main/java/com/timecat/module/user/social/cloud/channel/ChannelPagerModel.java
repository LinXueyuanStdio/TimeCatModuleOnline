package com.timecat.module.user.social.cloud.channel;

import android.content.Context;

import com.cheng.channel.Channel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-10-27
 * @description null
 * @usage null
 */
public class ChannelPagerModel {

    private String title;
    private Fragment fragment;
    private Channel channel;
    private String key;

    private ChannelPagerModel(String title, Fragment fragment, Channel channel) {
        this(title, fragment, channel, null);
    }

    public ChannelPagerModel(String title, Fragment fragment, Channel channel, String key) {
        this.title = title;
        this.fragment = fragment;
        this.key = key;
    }

    //region getter and setter
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChannelPagerModel that = (ChannelPagerModel) o;

        return key != null ? key.equals(that.key) : that.key == null;
    }

    @Override
    public int hashCode() {
        return key != null ? key.hashCode() : 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    //endregion

    @NonNull
    public static List<ChannelPagerModel> buildForAll(@NonNull Context context, List<Channel> channelList) {
        List<ChannelPagerModel> list = new ArrayList<>();
        for (Channel channel : channelList) {
            String title = channel.getChannelName();
            Fragment fragment = ChannelManager.buildFragmentFromChannel(channel);
            list.add(new ChannelPagerModel(title, fragment, channel));
        }
        return list;
    }
}
