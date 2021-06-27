package com.timecat.module.plugin.manager.common;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.FileUtils;
import com.tencent.shadow.dynamic.host.EnterCallback;
import com.tencent.shadow.dynamic.host.PluginManager;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.data.system.model.api.PluginDownloadService;
import com.timecat.identity.readonly.PluginHub;
import com.timecat.identity.service.PluginService;
import com.timecat.module.plugin.manager.DownloadListener;
import com.timecat.module.plugin.manager.DownloadUtil;
import com.timecat.module.plugin.core.InitApplication;
import com.timecat.module.plugin.manager.picturebed.PictureBedPluginConstants;
import com.xiaojinzi.component.anno.ServiceAnno;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/4/12
 * @description null
 * @usage null
 */
@ServiceAnno(PluginService.class)
public class PluginServiceImpl implements PluginService {
    private Context context;

    public PluginServiceImpl(Context context) {
        this.context = context;
    }

    /**
     * 调用一个插件，如果插件没下载，就下载后再调用
     * @param input 传递给插件管理器的、关于插件的信息
     *              一方面，让管理器能根据这些信息调用到插件
     *              另一方面，传递对应的数据给插件作为输入
     * @param output 回调给当前宿主，说明下载、调用的状态，分失败，成功，调用中等等
     */
    @Override
    public void start(InputToPlugin input, OutputFromPlugin output) {

        String pluginFileName = input.filename();
        String downloadUrl = input.downloadUrl();
        boolean loadAfterDownload = input.isPlugin();
        output.setEnabled(false);
        if (existPlugin(pluginFileName)) {
            // 已下载
            if (loadAfterDownload) {
                loadPlugin(pluginFileName, input, output);
            } else {
                // manager 不能被加载
                output.setEnabled(true);
            }
        } else {
            // 未下载
            // manager 不能被加载
            LogUtil.se(pluginFileName + " <- " + downloadUrl);
            String downloadFile = PictureBedPluginConstants.getPluginAbsPath(context, pluginFileName);
            downloadFile(downloadUrl, downloadFile, new DownloadListener() {
                @Override
                public void onStart() {
                    output.onDownloadStart();
                }

                @Override
                public void onProgress(int currentLength, int total) {
                    output.onDownloadProgress(currentLength, total);
                }

                @Override
                public void onFinish(String localPath) {
                    LogUtil.se("");
                    output.onDownloadSuccess();
                    if (loadAfterDownload) {
                        LogUtil.se("");
                        loadPlugin(pluginFileName, input, output);
                    } else {
                        output.setEnabled(true);
                    }
                }

                @Override
                public void onFailure() {
                    LogUtil.se("");
                    output.onDownloadFail("下载失败！");
                }
            });
        }
    }

    private Thread mThread;

    public static PluginDownloadService getPluginService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://github.com/LinXueyuanStdio/TimeCatMaven/raw/master/")
                .client(new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(20, TimeUnit.SECONDS)
                        .addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request r = chain.request();
                                LogUtil.se(r.toString());
                                return chain.proceed(r);
                            }
                        })
                        .cache(null)
                        .build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(PluginDownloadService.class);
    }

    public void downloadFile(String fromUrl, String toFilePath, final DownloadListener downloadListener) {
        File mFile = new File(toFilePath);
        if (!FileUtils.isFileExists(mFile) && FileUtils.createOrExistsFile(mFile)) {
            getPluginService().downloadFile(fromUrl)
                    .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                    .observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                    .subscribe(new Observer<ResponseBody>() {
                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e != null) e.printStackTrace();
                            downloadListener.onFailure(); //下载失败
                        }

                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(ResponseBody response) {
                            //下载文件放在子线程
                            mThread = new Thread(() -> DownloadUtil.writeFile2Disk(response, mFile, downloadListener));
                            mThread.start();
                        }
                    });
        } else {
            downloadListener.onFinish(toFilePath); //下载完成
        }
    }

    @Override
    public boolean existPlugin(String filename) {
        String path = PictureBedPluginConstants.getPluginAbsPath(context, filename);
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    private void loadPlugin(String pluginFileName, InputToPlugin input, OutputFromPlugin output) {
        PluginManager pluginManager = InitApplication.getPictureBedPluginManager();
        if (pluginManager == null) {
            output.onEnterFail("未找到插件管理器，请安装后重试");
            return;
        }
        Uri uri = input.uri();
        Bundle extra = input.extra();
        String action = input.action();
        String partKey = input.partKey();
        String activityClassName = input.activityClassName();
        String filepath = PictureBedPluginConstants.getPluginAbsPath(context, pluginFileName);
        Bundle bundle = getBundle(filepath, partKey, activityClassName, extra, uri, action);

        pluginManager.enter(context, PluginHub.FROM_ID_START_ACTIVITY, bundle, new EnterCallback() {
            @Override
            public void onShowLoadingView(View view) {
                //显示Manager传来的Loading页面
                output.onLoading(view);
            }

            @Override
            public void onCloseLoadingView() {
                output.onCloseLoadingView();
            }

            @Override
            public void onEnterComplete() {
                output.onEnterComplete();
            }
        });
    }

    private Bundle getBundle(String zipAbsPathForPlugin,
                             String partName,
                             String activity_class_name,
                             Bundle extra,
                             Uri data,
                             String action) {
        Bundle bundle = new Bundle();
        LogUtil.se(zipAbsPathForPlugin);
        LogUtil.se(partName);
        LogUtil.se(activity_class_name);
        bundle.putString(PluginHub.KEY_PLUGIN_ZIP_PATH, zipAbsPathForPlugin);
        bundle.putString(PluginHub.KEY_PLUGIN_PART_KEY, partName);
        bundle.putString(PluginHub.KEY_ACTIVITY_CLASSNAME, activity_class_name);
        bundle.putBundle(PluginHub.KEY_EXTRAS, extra);
        bundle.putString(PluginHub.KEY_ACTION, action);
        bundle.putParcelable(PluginHub.KEY_DATA, data);
        return bundle;
    }

}
