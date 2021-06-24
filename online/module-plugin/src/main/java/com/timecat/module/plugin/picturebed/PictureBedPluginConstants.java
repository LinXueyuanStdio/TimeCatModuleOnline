package com.timecat.module.plugin.picturebed;

import android.content.Context;
import android.os.Environment;

import com.timecat.component.setting.DEF;

import java.io.File;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-15
 * @description null
 * @usage null
 */
public class PictureBedPluginConstants {
    /**
     * 插件所在的文件夹名
     */
    public static final String PLUGIN_DEPLOY_PATH = "pictureBedPlugins";

    /**
     * 插件包的位置
     * @param context context
     * @param pluginName 插件名（=插件包的文件名）
     * @return 插件包的位置
     */
    public static String getPluginAbsPath(Context context, String pluginName) {
        return new File(getPluginDir(context), pluginName).getAbsolutePath();
    }

    /**
     * 插件管理器的位置
     * @param context context
     * @param versionCode 版本号
     * @return 插件管理器的位置
     */
    public static String getPluginManagerAbsPath(Context context, String versionCode) {
        return new File(getPluginDir(context), "manager" + versionCode + ".plugin").getAbsolutePath();
    }

    public static int getPluginManagerVersionCode(String name) {
        return DEF.plugin().getInt(name, 0);
    }

    /**
     * 插件目录，在缓存目录中新建了个文件夹作为插件目录
     * @param context context
     * @return 插件目录
     */
    public static String getPluginDir(Context context) {
        File pluginDir = new File(getCacheDir(context), PLUGIN_DEPLOY_PATH);
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }
        return pluginDir.getAbsolutePath();
    }

    /**
     * 缓存目录
     * @param context context
     * @return 缓存目录
     */
    private static String getCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                return cacheDir.getAbsolutePath();
            }
        }

        return context.getCacheDir().getAbsolutePath();
    }
}
