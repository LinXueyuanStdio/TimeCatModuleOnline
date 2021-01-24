package com.timecat.module.user.base;

import android.os.Parcelable;

import com.timecat.component.router.app.NAV;
import com.timecat.data.bmob.data.common.Block;
import com.timecat.identity.readonly.RouterHub;

import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_APP;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_COMMENT;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_FORUM;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_LEADER_BOARD;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_MOMENT;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_POST;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_TAG;
import static com.timecat.identity.data.block.type.BlockTypeKt.BLOCK_TOPIC;

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

    public static void toAnyDetail(Block block) {
        String id = block.getObjectId();
        switch (block.getType()) {
            case BLOCK_MOMENT:
                momentDetail(id);
                break;
            case BLOCK_FORUM:
                forumDetail(id);
                break;
            case BLOCK_TOPIC:
                topicDetail(id);
                break;
            case BLOCK_TAG:
                tagDetail(id);
                break;
            case BLOCK_POST:
                postDetail(id);
                break;
            case BLOCK_COMMENT:
                commentDetail(id);
                break;
            case BLOCK_APP:
                appDetail(id);
                break;
            case BLOCK_LEADER_BOARD:
                leaderBoardDetail(id);
                break;
        }
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

    public static void leaderBoardDetail(String blockId) {
        NAV.go(RouterHub.USER_LeaderBoardDetailActivity, "blockId", blockId);
    }

    public static void allTag(Block parent) {
        NAV.go(RouterHub.USER_AllTagActivity, "parent", (Parcelable) parent);
    }

    public static void addRecommendFor(Block parent) {
        NAV.go(RouterHub.USER_AddRecommendActivity, "parent", (Parcelable) parent);
    }

    public static void addCommentFor(Block parent) {
        NAV.go(RouterHub.USER_AddCommentActivity, "parent", (Parcelable) parent);
    }

    public static void addMomentFor(Block parent) {
        NAV.go(RouterHub.USER_AddMomentActivity, "parent", (Parcelable) parent);
    }

    public static void addPostFor(Block parent) {
        NAV.go(RouterHub.USER_AddPostActivity, "parent", (Parcelable) parent);
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

    public static void addIdentity(Block block) {
        NAV.go(RouterHub.USER_AddIdentityActivity, "block", (Parcelable) block);
    }

    public static void addRole(Block block) {
        NAV.go(RouterHub.USER_AddRoleActivity, "block", (Parcelable) block);
    }

    public static void addMetaPermission(Block block) {
        NAV.go(RouterHub.USER_AddMetaPermissionActivity, "block", (Parcelable) block);
    }

    public static void addHunPermission(Block block) {
        NAV.go(RouterHub.USER_AddHunPermissionActivity, "block", (Parcelable) block);
    }

    public static void relayMoment(Block parent, Block relay) {
        NAV.raw(RouterHub.USER_AddMomentActivity)
           .withParcelable("parent", parent)
           .withParcelable("relay", relay)
           .navigation();
    }

    public static void relayComment(Block parent, Block relay) {
        NAV.raw(RouterHub.USER_AddCommentActivity)
           .withParcelable("parent", parent)
           .withParcelable("relay", relay)
           .navigation();
    }

}
