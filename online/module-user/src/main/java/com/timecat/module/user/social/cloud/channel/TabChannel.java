package com.timecat.module.user.social.cloud.channel;

import com.cheng.channel.Channel;
import com.timecat.layout.ui.standard.navi.TabBlockItem;
import com.timecat.module.user.game.task.channal.TaskChannel;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-10-29
 * @description null
 * @usage null
 */
public class TabChannel extends Channel {

    public TabChannel() {
        this("任务");
    }

    public TabChannel(String channelName) {
        super(channelName);
    }

    public TabChannel(String channelName, int channelBelong, Object obj) {
        super(channelName, channelBelong, obj);
    }

    public TabChannel(String channelName, int channelBelong) {
        super(channelName, channelBelong);
    }

    public TabChannel(String channelName, Object obj) {
        super(channelName, obj);
    }
}
