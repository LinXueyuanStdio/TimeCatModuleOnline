package com.timecat.module.user.app;

import com.timecat.data.bmob.dao.UserDao;
import com.timecat.data.bmob.data.User;
import com.timecat.identity.service.UserService;
import com.xiaojinzi.component.anno.ServiceAnno;

import androidx.annotation.NonNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-08
 * @description null
 * @usage null
 */
@ServiceAnno(UserService.class)
public class UserServiceImpl implements UserService {
    @NonNull
    @Override
    public String getUsername() {
        User user = UserDao.getCurrentUser();
        return user == null ? "薛定谔的喵" : user.getNickName();
    }

    @NonNull
    @Override
    public String getEmail() {
        User user = UserDao.getCurrentUser();
        return (user == null || user.getEmail() == null) ? "" : user.getEmail();
    }

    @NonNull
    @Override
    public String getAvatar() {
        User user = UserDao.getCurrentUser();
        return user == null ? "" : user.getAvatar();
    }

    @NonNull
    @Override
    public String getCoverPageUrl() {
        User user = UserDao.getCurrentUser();
        return user == null ? "" : user.getCover();
    }

    @NonNull
    @Override
    public String getObjectId() {
        User user = UserDao.getCurrentUser();
        return user == null ? "" : user.getObjectId();
    }

    @Override
    public boolean isLogin() {
        User user = UserDao.getCurrentUser();
        return user != null;
    }
}
