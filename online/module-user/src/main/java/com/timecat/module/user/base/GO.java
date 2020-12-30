package com.timecat.module.user.base;

import com.timecat.data.bmob.data.common.Block;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.component.router.app.NAV;

import java.io.Serializable;

;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description 简化页面跳转
 * @usage null
 */
public class GO {
    public static void userDetail(String objectId) {
        NAV.go(RouterHub.USER_UserDetailActivity, "userId", objectId);
    }

    public static void momentDetail(String blockId) {
        NAV.go(RouterHub.USER_MomentDetailActivity, "blockId", blockId);
    }

    public static void forumDetail(String blockId) {
        NAV.go(RouterHub.USER_ForumDetailActivity, "blockId", blockId);
    }

    public static void topicDetail(String blockId) {
        NAV.go(RouterHub.USER_TopicDetailActivity, "blockId", blockId);
    }

    public static void tagDetail(String blockId) {
        NAV.go(RouterHub.USER_TagDetailActivity, "blockId", blockId);
    }

    public static void postDetail(String blockId) {
        NAV.go(RouterHub.USER_PostDetailActivity, "blockId", blockId);
    }

    public static void commentDetail(String blockId) {
        NAV.go(RouterHub.USER_CommentDetailActivity, "blockId", blockId);
    }

    public static void appDetail(String blockId) {
        NAV.go(RouterHub.APP_DETAIL_AppDetailActivity, "blockId", blockId);
    }

    public static void addCommentFor(Block parent) {
        NAV.go(RouterHub.USER_AddCommentActivity, "parent", (Serializable) parent);
    }

    public static void addMomentFor(Block parent) {
        NAV.go(RouterHub.USER_AddMomentActivity, "parent", (Serializable) parent);
    }

    public static void addPostFor(Block parent) {
        NAV.go(RouterHub.USER_AddPostActivity, "parent", (Serializable) parent);
    }

    public static void addForum(String name, String content, String icon) {
        NAV.raw(RouterHub.USER_AddForumActivity)
                .withString("name", name)
                .withString("content", content)
                .withString("icon", icon)
                .navigation();
    }

    public static void addIdentity() {
        NAV.go(RouterHub.USER_AddIdentityActivity);
    }

    public static void addRole() {
        NAV.go(RouterHub.USER_AddRoleActivity);
    }

    public static void addMetaPermission() {
        NAV.go(RouterHub.USER_AddMetaPermissionActivity);
    }

    public static void addHunPermission() {
        NAV.go(RouterHub.USER_AddHunPermissionActivity);
    }

    public static void relayMoment(Block parent, Block relay) {
        NAV.raw(RouterHub.USER_AddMomentActivity)
                .withSerializable("parent", parent)
                .withSerializable("relay", relay)
                .navigation();
    }

    public static void relayComment(Block parent, Block relay) {
        NAV.raw(RouterHub.USER_AddCommentActivity)
                .withSerializable("parent", parent)
                .withSerializable("relay", relay)
                .navigation();
    }

}
