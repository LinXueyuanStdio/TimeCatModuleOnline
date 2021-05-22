package com.same.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import cn.leancloud.AVLogger
import cn.leancloud.AVOSCloud
import cn.leancloud.AVObject
import cn.leancloud.Transformer
import cn.leancloud.json.JSONObject
import com.google.android.material.button.MaterialButton
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.asBlock
import com.timecat.data.bmob.ext.bmob.asUser
import com.timecat.data.bmob.ext.bmob.findAllComments
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.dp
import com.xiaojinzi.component.impl.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AVOSCloud.setLogLevel(AVLogger.Level.DEBUG)
        LogUtil.DEBUG = true
        LogUtil.OPEN_LOG = true
        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(createButton("登录", RouterHub.LOGIN_LoginActivity))
        linearLayout.addView(createButton("游戏化", RouterHub.USER_GameHomeActivity))
        linearLayout.addView(createButton("背包", RouterHub.USER_BagActivity))

        linearLayout.addView(createButton("用户") {
            val user = UserDao.getCurrentUser()
            Router.with().hostAndPath(RouterHub.USER_UserDetailActivity)
                .putString("userId", user?.objectId)
                .forward()
        })
        linearLayout.addView(createButton("测试") {
            findAllComments(1, 10, "602f42689c05de4254617ef0") {
                onSuccess = {
                    LogUtil.se(it::class)
                    LogUtil.se(it[0]::class)
                    LogUtil.se(it[0].keys)
//                    val rawObject = AVObject.parseAVObject(it[0].toString())
//                    val block= Transformer.transform(rawObject, Block::class.java)
//                    LogUtil.e(block)
//                    LogUtil.e(block::class.java)
                    for (i in it) {
                        LogUtil.se(i.asBlock())
                        LogUtil.se(i["parent"]!!::class.java)
                        LogUtil.se(i["user"]!!.asUser())
                        val hot_children = i["hot_children"] as ArrayList<*>
                        for (j in hot_children) {
                            val block= j.asBlock()
                            LogUtil.e(block)
                        }
                    }
                }
                onError = {
                    LogUtil.e(it)
                }
            }
        })
        linearLayout.addView(createPathButton(RouterHub.USER_AllUserActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllTraceActivity))

        linearLayout.addView(createText("物品"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllItemActivity))

        linearLayout.addView(createPathButton(RouterHub.USER_ThingItemEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_PackageItemEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_DataItemEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_EquipItemEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_BuffItemEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CubeItemEditorActivity))

        linearLayout.addView(createText("邮件"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllMailActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllOwnMailActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_MailEditorActivity))

        linearLayout.addView(createText("方块"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllOwnCubeActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllIdentityActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllMetaPermissionActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllHunPermissionActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllRoleActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllAuthActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CubeSettingEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CubeAttrEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CubeRolesEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CubeSkillEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CubeStarEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddMetaPermissionActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddHunPermissionActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddRoleActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddIdentityActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddAuthActivity))

        linearLayout.addView(createText("活动"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllActivityActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllOwnActivityActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_UrlActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_TextUrlActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_OneTaskActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_SevenDaySignActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AchievementActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_CardActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_DoubleActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_DreamActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_EveryDayMainActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_GetBackActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_LifeActivityEditorActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_PriceActivityEditorActivity))

        linearLayout.addView(createText("任务"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllTaskActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_DataTaskEditorActivity))

        linearLayout.addView(createText("动态"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllMomentActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddMomentActivity))

        linearLayout.addView(createText("评论"))
        linearLayout.addView(createPathButton(RouterHub.USER_AddCommentActivity))

        linearLayout.addView(createText("帖子"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllForumActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddPostActivity))

        linearLayout.addView(createText("话题"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllTopicActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddTopicActivity))

        linearLayout.addView(createText("标签"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllTagActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddTagActivity))

        linearLayout.addView(createText("排行榜"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllLeaderBoardActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AllRecommendActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddLeaderBoardActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddRecommendActivity))

        linearLayout.addView(createText("App"))
        linearLayout.addView(createPathButton(RouterHub.USER_AllAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddAndroidAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddiOSAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddMacAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddPluginAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddWebAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_AddWindowsAppActivity))
        linearLayout.addView(createPathButton(RouterHub.USER_EditPluginAppActivity))

        val sv = ScrollView(this)
        sv.addView(linearLayout)
        setContentView(sv)
    }

    private fun createButton(name: String, path: String): Button {
        val button = createButton(name)
        button.setOnClickListener { go(path) }
        return button
    }

    private fun createPathButton(path: String): Button {
        val button = createButton(path)
        button.setOnClickListener { go(path) }
        return button
    }

    private fun createButton(name: String, onClickListener: View.OnClickListener): Button {
        val button = createButton(name)
        button.setOnClickListener(onClickListener)
        return button
    }

    private fun createButton(name: String): Button {
        val button = MaterialButton(this)
        button.text = name
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
            marginStart = 10.dp
            marginEnd = 10.dp
        }
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER_VERTICAL
        button.isAllCaps = false
        return button
    }

    private fun createText(name: String): TextView {
        val button = TextView(this)
        button.text = name
        val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        button.layoutParams = layoutParams
        button.gravity = Gravity.CENTER
        button.isAllCaps = false
        return button
    }

    private fun go(path: String) {
        Router.with().hostAndPath(path)
            .forward(object : Callback {
                override fun onSuccess(result: RouterResult) {}
                override fun onEvent(successResult: RouterResult?, errorResult: RouterErrorResult?) {}
                override fun onCancel(originalRequest: RouterRequest?) {}
                override fun onError(errorResult: RouterErrorResult) {
                    Log.e("ui", errorResult.error.toString())
                }
            })
    }
}