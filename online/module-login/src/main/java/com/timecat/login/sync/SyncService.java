package com.timecat.login.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/5/8
 * @description null
 * @usage null
 */
public class SyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (sSyncAdapter != null) return sSyncAdapter.getSyncAdapterBinder();
        return null;
    }
}
