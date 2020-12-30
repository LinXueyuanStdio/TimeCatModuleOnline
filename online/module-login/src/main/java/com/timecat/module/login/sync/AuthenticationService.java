package com.timecat.module.login.sync;

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
public class AuthenticationService extends Service {

    private AccountAuthenticator mAuthenticator;

    private AccountAuthenticator getAuthenticator() {
        if (mAuthenticator == null) {
            mAuthenticator = new AccountAuthenticator(this);
        }
        return mAuthenticator;
    }

    @Override
    public void onCreate() {
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getAuthenticator().getIBinder();
    }
}