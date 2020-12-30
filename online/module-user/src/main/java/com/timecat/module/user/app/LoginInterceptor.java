package com.timecat.module.user.app;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.timecat.data.bmob.dao.UserDao;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.component.router.app.NavInterceptor;
import com.xiaojinzi.component.anno.InterceptorAnno;
import com.xiaojinzi.component.impl.Router;
import com.xiaojinzi.component.impl.RouterErrorResult;
import com.xiaojinzi.component.impl.RouterResult;
import com.xiaojinzi.component.support.CallbackAdapter;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-01-20
 * @description 登录拦截器，进入需要登录的页面时会尝试进行拦截
 * - 已登录则不拦截
 * - 未登录则拦截并跳转到登录页面，登录完后自动进入目标页面
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
@InterceptorAnno("LoginInterceptor")
public class LoginInterceptor implements NavInterceptor {

    private boolean isLogin() {
        return UserDao.getCurrentUser() != null;
    }

    @Override
    public void intercept(@NonNull Chain chain) throws Exception {
        LogUtil.se("登录校验");
        if (isLogin()) {
            chain.proceed(chain.request());
        } else {
            final Context context = chain.request().getRawContext();
            Router.with(context)
                    .hostAndPath(RouterHub.LOGIN_LoginActivity)
                    .requestCodeRandom()
                    // 匹配目标界面返回的 ResultCode
                    .forwardForResultCodeMatch(new CallbackAdapter() {
                        @Override
                        public void onSuccess(@NonNull RouterResult result) {
                            chain.proceed(chain.request());
                        }

                        @Override
                        public void onError(@NonNull RouterErrorResult errorResult) {
                            chain.callback().onError(new Exception("login fail"));
                        }
                    }, Activity.RESULT_OK);
        }
    }
}
