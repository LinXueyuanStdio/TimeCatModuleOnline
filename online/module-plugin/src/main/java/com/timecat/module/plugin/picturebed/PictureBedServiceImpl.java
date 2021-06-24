package com.timecat.module.plugin.picturebed;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.timecat.identity.readonly.PluginManagerAgreement;
import com.timecat.identity.service.PictureBedService;
import com.timecat.module.plugin.core.InitApplication;
import com.xiaojinzi.component.anno.ServiceAnno;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/12
 * @description null
 * @usage null
 */
@ServiceAnno(PictureBedService.class)
public class PictureBedServiceImpl implements PictureBedService {
    @Override
    public void start(Context context, List<String> url, Work work) {
        PluginManager pluginManager = InitApplication.getPictureBedPluginManager();
        if (pluginManager == null) {
            work.onEnterFail("未找到插件管理器，请安装后重试");
            return;
        }
        Bundle bundle = new Bundle();
        ArrayList<String> urls = new ArrayList<>(url);
        bundle.putStringArrayList(PluginManagerAgreement.KEY_PICTURE_PATH, urls);
        pluginManager.enter(context, PluginManagerAgreement.FROM_ID_LOAD_PICTURE, bundle, new EnterCallback() {
            @Override
            public void onShowLoadingView(View view) {
                work.onShowLoadingView(view);
            }

            @Override
            public void onCloseLoadingView() {
                work.onCloseLoadingView();
            }

            @Override
            public void onEnterComplete() {
                work.onEnterComplete();
            }
        });
    }
}
