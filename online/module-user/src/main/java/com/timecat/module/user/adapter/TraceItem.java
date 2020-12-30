package com.timecat.module.user.adapter;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.timecat.data.bmob.data.trace.Trace;

import androidx.annotation.NonNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-13
 * @description null
 * @usage null
 */
public class TraceItem implements MultiItemEntity {
    @NonNull
    Trace trace;

    public TraceItem(@NonNull Trace trace) {
        this.trace = trace;
    }

    @Override
    public int getItemType() {
        return trace.getType();
    }

    @NonNull
    public Trace getTrace() {
        return trace;
    }
}