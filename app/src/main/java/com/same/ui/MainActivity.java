package com.same.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

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
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.addView(createButton("登录", RouterHub.LOGIN_LoginActivity));
        linearLayout.addView(createButton("添加插件", RouterHub.USER_AddPluginAppActivity));
        linearLayout.addView(createButton("添加动态", RouterHub.USER_AddMomentActivity));

        setContentView(linearLayout);
    }

    private Button createButton(String name, String path) {
        Button button = new Button(this);
        button.setText(name);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        button.setLayoutParams(layoutParams);
        button.setGravity(Gravity.CENTER);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go(path);
            }
        });
        return button;
    }

    private void go(String path) {
        Router.with().hostAndPath(path)
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

}
