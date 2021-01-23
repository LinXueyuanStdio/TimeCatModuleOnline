package com.timecat.module.user.app;

import com.timecat.data.bmob.dao.UserDao;
import com.timecat.data.bmob.data._User;
import com.timecat.identity.service.UserService;
import com.xiaojinzi.component.anno.ServiceAnno;

import androidx.annotation.NonNull;
import cn.bmob.v3.datatype.BmobFile;

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
        _User user = UserDao.getCurrentUser();
        return user == null ? "薛定谔的喵" : user.getNickName();
    }

    @NonNull
    @Override
    public String getEmail() {
        _User user = UserDao.getCurrentUser();
        return (user == null || user.getEmail() == null) ? "" : user.getEmail();
    }

    @NonNull
    @Override
    public String getAvatar() {
        _User user = UserDao.getCurrentUser();
        return user == null ? "" : user.getAvatar();
    }

    @NonNull
    @Override
    public String getCoverPageUrl() {
        _User user = UserDao.getCurrentUser();
        if (user == null) {
            return "";
        }
        BmobFile file = user.getCoverPage();
        if (file == null) {
            return "";
        }
        return file.getFileUrl();
    }

    @NonNull
    @Override
    public String getObjectId() {
        _User user = UserDao.getCurrentUser();
        return user == null ? "" : user.getObjectId();
    }

    @Override
    public boolean isLogin() {
        _User user = UserDao.getCurrentUser();
        return user != null;
    }
}
