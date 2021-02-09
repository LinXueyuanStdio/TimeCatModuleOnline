package com.timecat.module.user.app

import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.allForum
import com.timecat.identity.readonly.RouterHub
import com.timecat.identity.service.ForumService
import com.timecat.module.user.base.GO
import com.xiaojinzi.component.anno.ServiceAnno
import com.xiaojinzi.component.impl.Router

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description 论坛服务
 * @usage null
 */
@ServiceAnno(ForumService::class)
class ForumServiceImpl : ForumService {
    /**
     * 去论坛
     * 当前用户没有登录：跳到登录界面
     * 如果论坛存在，可以跳转
     * - 用户有访问论坛的权限，正常跳转
     * - 用户被论坛拉黑，显示被封禁的页面
     * 如果论坛不存在，需要创建
     * - 用户有创建论坛的权限，当前用户成为新论坛的创建着
     * - 用户没有创建论坛的权限，显示无权限的页面
     * 创建页面和详情页面根据权限分状态
     * @param name 论坛名
     */
    override fun gotoForum(name: String, content: String, icon: String) {
        if (!isLogin) {
            NAV.go(RouterHub.LOGIN_LoginActivity)
            return
        }
        requestOneBlock {
            query = allForum().whereEqualTo("title", name)
            onError = {

            }
            onSuccess = {
                if (it == null) {
                    //当前用户创建论坛
                    Router.with().hostAndPath(RouterHub.USER_AddForumActivity)
                        .putString("name", name)
                        .putString("content", content)
                        .putString("icon", icon)
                        .forward()
                } else {
                    //论坛已存在，正常跳转
                    GO.forumDetail(it.objectId)
                }
            }
        }
    }

    private val isLogin: Boolean = UserDao.getCurrentUser() != null
}