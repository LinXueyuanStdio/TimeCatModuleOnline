package com.timecat.module.user.app;

import com.timecat.component.router.app.NAV;
import com.timecat.data.bmob.dao.UserDao;
import com.timecat.data.bmob.data.common.Block;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.identity.service.ForumService;
import com.timecat.module.user.base.GO;
import com.xiaojinzi.component.anno.ServiceAnno;
import com.xiaojinzi.component.impl.Router;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description 论坛服务
 * @usage null
 */
@ServiceAnno(ForumService.class)
public class ForumServiceImpl implements ForumService {
    /**
     * 去论坛
     * 当前用户没有登录：跳到登录界面
     * 如果论坛存在，可以跳转
     *   - 用户有访问论坛的权限，正常跳转
     *   - 用户被论坛拉黑，显示被封禁的页面
     * 如果论坛不存在，需要创建
     *   - 用户有创建论坛的权限，当前用户成为新论坛的创建着
     *   - 用户没有创建论坛的权限，显示无权限的页面
     * 创建页面和详情页面根据权限分状态
     * @param name 论坛名
     */
    @Override
    public void gotoForum(String name, String content, String icon) {
        if (!isLogin()) {
            NAV.go(RouterHub.LOGIN_LoginActivity);
            return;
        }
        AVQuery<Block> query = new AVQuery<>();
        query.whereEqualTo("title", name);
        query.setLimit(1);
        query.findObjects(new FindListener<Block>() {
            @Override
            public void done(List<Block> list, BmobException e) {
                if (e == null) {
                    if (list == null || list.isEmpty()) {
                        //当前用户创建论坛
                        Router.with().hostAndPath(RouterHub.USER_AddForumActivity)
                              .putString("name", name)
                              .putString("content", content)
                              .putString("icon", icon)
                              .forward();
                    } else {
                        //论坛已存在，正常跳转
                        Block b = list.get(0);
                        GO.forumDetail(b.getObjectId());
                    }
                }
            }
        });
    }

    private boolean isLogin() {
        return UserDao.getCurrentUser() != null;
    }
}
