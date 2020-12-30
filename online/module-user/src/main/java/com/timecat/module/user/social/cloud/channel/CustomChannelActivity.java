package com.timecat.module.user.social.cloud.channel;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xiaojinzi.component.anno.RouterAnno;
import com.cheng.channel.Channel;
import com.cheng.channel.ChannelView;
import com.cheng.channel.ViewHolder;
import com.cheng.channel.adapter.BaseStyleAdapter;
import com.cheng.channel.adapter.ChannelListenerAdapter;
import com.timecat.page.base.friend.toolbar.BaseToolbarActivity;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.module.user.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RouterAnno(hostAndPath = RouterHub.USER_CustomChannelActivity)
public class CustomChannelActivity extends BaseToolbarActivity {
    private String TAG = "CustomChannelActivity:";
    private ChannelView channelView;

    @Override
    protected int layout() {
        return R.layout.user_activity_custom_channel;
    }

    @NonNull
    @Override
    protected String title() {
        return "自定义视图";
    }

    @Override
    protected void initView() {
        TextView textView = findViewById(R.id.save);
        textView.setOnClickListener(v -> {
            List<Channel> channelList = channelView.getMyChannel();
            List<String> strings = new ArrayList<>();
            for (Channel c:channelList){
                strings.add(c.getChannelName());
            }
            ChannelManager.saveChannel(strings);
            finish();
        });
        channelView = findViewById(R.id.channelView);
        channelView.setChannelFixedCount(2);
        channelView.setStyleAdapter(new MyAdapter());
        channelView.setOnChannelListener(new ChannelListenerAdapter() {
            @Override
            public void channelItemClick(int position, Channel channel) {
                Log.i(TAG, position + ".." + channel);
            }

            @Override
            public void channelEditStateItemClick(int position, Channel channel) {
                Log.i(TAG + "EditState:", position + ".." + channel);
            }

            @Override
            public void channelEditFinish(List<Channel> channelList) {
                List<String> strings = new ArrayList<>();
                for (Channel c:channelList){
                    strings.add(c.getChannelName());
                }
                ChannelManager.saveChannel(strings);
            }

            @Override
            public void channelEditStart() {
                Log.i(TAG, "channelEditStart");
            }
        });
    }

    class MyAdapter extends BaseStyleAdapter<MyAdapter.MyViewHolder> {

        @Override
        public MyViewHolder createStyleView(ViewGroup parent, String channelName) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_custom_channel, null);
            MyViewHolder customViewHolder = new MyViewHolder(inflate);
            customViewHolder.tv.setText(channelName);
            return customViewHolder;
        }

        @Override
        public LinkedHashMap<String, List<Channel>> getChannelData() {
            return ChannelManager.getData();
        }

        @Override
        public void setNormalStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_normal);
            viewHolder.iv.setVisibility(View.INVISIBLE);
        }

        @Override
        public void setFixedStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setTextColor(Color.parseColor("#1E87FF"));
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_fixed);
        }

        @Override
        public void setEditStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_edit);
            viewHolder.iv.setVisibility(View.VISIBLE);
        }

        @Override
        public void setFocusedStyle(MyViewHolder viewHolder) {
            viewHolder.tv.setBackgroundResource(R.drawable.bg_channel_custom_focused);
        }

        class MyViewHolder extends ViewHolder {
            TextView tv;
            ImageView iv;

            public MyViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv_channel);
                iv = itemView.findViewById(R.id.iv_delete);
            }
        }
    }

}
