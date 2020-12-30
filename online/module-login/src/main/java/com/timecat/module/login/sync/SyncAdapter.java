package com.timecat.module.login.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.timecat.component.commonsdk.utils.override.LogUtil;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/5/8
 * @description null
 * @usage null
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        LogUtil.e("autoInitialize = " + autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority,
                              ContentProviderClient provider, SyncResult syncResult) {
        //TODO 实现数据同步
        LogUtil.e("喵喵喵？假装同步中...(实际上什么也没有发生)");
        if (account != null) LogUtil.e(account.toString());
        if (extras != null) LogUtil.e(extras.toString());
        LogUtil.e(authority);
        if (syncResult != null) LogUtil.e(syncResult.toString());
//    getContext().getContentResolver().notifyChange(AccountProvider.CONTENT_URI, null, false);
    }
}