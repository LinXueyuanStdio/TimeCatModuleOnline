package com.same.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.timecat.identity.readonly.RouterHub;
import com.xiaojinzi.component.impl.Callback;
import com.xiaojinzi.component.impl.Router;
import com.xiaojinzi.component.impl.RouterErrorResult;
import com.xiaojinzi.component.impl.RouterRequest;
import com.xiaojinzi.component.impl.RouterResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button = new Button(this);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.with().hostAndPath(RouterHub.LOGIN_LoginActivity)
                      .forward(new Callback() {
                          @Override
                          public void onSuccess(@NonNull RouterResult result) {
                          }

                          @Override
                          public void onEvent(@Nullable RouterResult successResult, @Nullable RouterErrorResult errorResult) {
                          }

                          @Override
                          public void onCancel(@Nullable RouterRequest originalRequest) {

                          }

                          @Override
                          public void onError(@NonNull RouterErrorResult errorResult) {
                              Log.e("ui", errorResult.getError().toString());
                          }
                      });
            }
        });
setContentView(button);
    }

}
