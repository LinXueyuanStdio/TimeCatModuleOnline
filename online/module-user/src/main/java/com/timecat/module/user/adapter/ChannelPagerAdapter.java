package com.timecat.module.user.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.cheng.channel.Channel;
import com.timecat.module.user.social.cloud.channel.ChannelPagerModel;

import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-10-27
 * @description null
 * @usage null
 */
public class ChannelPagerAdapter extends FragmentStatePagerAdapter {

    private List<ChannelPagerModel> fragments;

    public ChannelPagerAdapter(FragmentManager fm, List<ChannelPagerModel> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }

    @Override
    public float getPageWidth(int position) {
        return super.getPageWidth(position);
    }

    public Channel getChannel(int position) {
        return fragments.get(position).getChannel();
    }

    public void remove(ChannelPagerModel model) {
        if (fragments != null) {
            fragments.remove(model);
            notifyDataSetChanged();
        }
    }

    public void remove(int position) {
        if (fragments != null) {
            fragments.remove(position);
            notifyDataSetChanged();
        }
    }
}
