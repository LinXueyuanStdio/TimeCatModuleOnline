package com.timecat.module.user.base;

import android.content.Context;
import android.widget.ImageView;

import com.timecat.extend.arms.BaseApplication;
import com.timecat.layout.ui.utils.IconLoader;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-13
 * @description 图片加载器，简化使用
 * @usage null
 */
public class LOAD {
    public static void image(String icon, ImageView iv) {
        image(BaseApplication.getContext(), icon, iv);
    }

    public static void image(Context context, String icon, ImageView iv) {
        IconLoader.loadIcon(context, iv, icon);
    }
}
