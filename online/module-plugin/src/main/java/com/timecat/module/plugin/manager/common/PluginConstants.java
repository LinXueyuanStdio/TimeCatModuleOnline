package com.timecat.module.plugin.manager.common;

import android.content.Context;

import com.timecat.component.setting.DEF;
import com.timecat.module.plugin.manager.Plugin;

import java.io.File;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-08-15
 * @description null
 * @usage null
 */
public class PluginConstants {
    public static final String PLUGIN_DEPLOY_PATH = "plugins";

    public static String getPluginAbsPath(Context context, String pluginName) {
        return new File(getPluginDir(context), pluginName).getAbsolutePath();
    }

    public static String getPluginManagerAbsPath(Context context, String versionCode) {
        return new File(getPluginDir(context), "manager" + versionCode + ".plugin").getAbsolutePath();
    }

    public static int getPluginManagerVersionCode(String name) {
        return DEF.plugin().getInt(name, 0);
    }

    public static String getPluginDir(Context context) {
        File pluginDir = new File(Plugin.getCacheDir(context), PluginConstants.PLUGIN_DEPLOY_PATH);
        if (pluginDir.exists() || pluginDir.mkdirs()) {
            return pluginDir.getAbsolutePath();
        }
        return null;
    }
}
