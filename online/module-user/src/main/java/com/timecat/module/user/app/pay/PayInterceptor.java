package com.timecat.module.user.app.pay;

import android.content.Context;

import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.router.app.NavInterceptor;
import com.timecat.data.bmob.dao.UserDao;
import com.timecat.data.bmob.data._User;
import com.timecat.identity.readonly.RouterHub;
import com.xiaojinzi.component.anno.InterceptorAnno;

import androidx.annotation.NonNull;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/31
 * @description 比较经典的应用就是在跳转过程中处理登陆事件，这样就不需要在目标页重复做登陆检查
 * @usage 拦截器会在跳转之间执行，多个拦截器会按优先级顺序依次执行
 * <p>
 * 路径分类：
 * 1. 无条件路径：
 * 如：splash页，登录页
 * 不需要校验即可通过
 * 2. 登录路径：
 * 如：用户详情页
 * 需要登录才能跳转的路径
 * 3. 付费路径：
 * 需要登录后并付费才能跳转的路径
 * <p>
 * 默认全部都是无条件路径
 * 先判断付费路径（优先级高）
 * 在判断登录路径（优先级低）
 */
@InterceptorAnno("PayInterceptor")
public class PayInterceptor implements NavInterceptor {

    /**
     * 检查是否能通过登录拦截
     *
     * @param path 任意路径（内部自动判断是否是登录路径）
     * @return true 则不拦截
     */
    private boolean checkPay(String path) {
        if (!needPay(path)) {
            return true;
        }
        return isPay(path);
    }

    private boolean needPay(String path) {
        return pathBlackList(path);
    }

    private boolean isPay(String path) {
        _User user = UserDao.getCurrentUser();
        if (user == null) { return false; }
        return canContinue_assetHasPermission(path, user);
    }

    /**
     * 时间资产的权限里有 path 对应的权限，则可正常跳转
     *
     * @param path 特定的喵才能去到的地方
     * @param user 用户
     * @return 有权限:true；无权限:false
     */
    private boolean canContinue_assetHasPermission(@NonNull String path, @NonNull _User user) {
        return true;//默认都可以跳转
    }

    /**
     * 路由黑名单
     *
     * @param path 路由
     * @return 在黑名单里：true，不在：false
     */
    private boolean pathBlackList(String path) {
        //只在有key
        return path.startsWith(RouterHub.APP_EnhanceNotificationActivity)
                || path.startsWith(RouterHub.THEME_ThemeActivity)
                || path.startsWith(RouterHub.GITHUB_ThemeActivity)
                || path.startsWith(RouterHub.Data_DataManagerActivity)
                || path.startsWith(RouterHub.GITHUB_MainActivity)
                //        || path.startsWith(RouterHub.NOVEL_ReadBookActivity)//不要拦截，影响体验
                || path.startsWith(RouterHub.NOVEL_ReadStyleActivity);
    }

    @Override
    public void intercept(@NonNull Chain chain) throws Exception {
        LogUtil.se("付费校验");
        final Context context = chain.request().getRawContext();
        //        chain.request().
        //                String path = chain.getPath();
        //        if (checkPay(path)) {
        //            callback.onContinue(postcard);
        //        } else {
        //            postcard.withString("InterruptError", "权限不足");
        //            callback.onInterrupt(new Exception("权限不足"));
        //        }
    }
}
