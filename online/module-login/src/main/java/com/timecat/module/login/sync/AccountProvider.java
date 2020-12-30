package com.timecat.module.login.sync;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/5/8
 * @description null
 * @usage null
 */
public class AccountProvider extends ContentProvider {

    public static final String AUTHORITY = "com.time.cat.account.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;
    public static final String TABLE_NAME = "data";
    public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_BASE + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        return 0;
    }
}