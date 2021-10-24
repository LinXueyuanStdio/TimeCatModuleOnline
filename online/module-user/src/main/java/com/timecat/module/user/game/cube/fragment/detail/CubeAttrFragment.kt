package com.timecat.module.user.game.cube.fragment.detail

import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.game.OwnCube
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.ext.bmob.requestOwnItem
import com.timecat.data.bmob.ext.bmob.useItem
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.form.H2
import com.timecat.layout.ui.business.form.MaterialButton
import com.timecat.layout.ui.business.form.VerticalContainer
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.setting.PathNext
import com.timecat.module.user.R
import com.timecat.module.user.ext.simpleErrorCallback
import com.timecat.module.user.game.cube.*
import com.timecat.module.user.view.dsl.Content
import com.timecat.module.user.view.item.ContentItem
import com.timecat.module.user.view.item.CubeLevelItem

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/3
 * @description
 * 属性
 * 种族
 * 元素
 * 等级，升级
 * 简单的介绍
 * @usage null
 */
class CubeAttrFragment : BaseCubeFragment() {
    lateinit var contentItem: ContentItem
    lateinit var cubeLevelBar: CubeLevelItem
    lateinit var button: Button
    lateinit var pageContainer: LinearLayout

    override fun loadDetail(ownCube: OwnCube) {
        pageContainer.removeAllViews()
        addPathByCube(pageContainer, ownCube)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        container.apply {
            cubeLevelBar = CubeLevelBar(1, 0)
            contentItem = Content("")
            button = MaterialButton("升级") {}
            pageContainer = VerticalContainer {

            }
        }
        cubeViewModel.cubeLevelBar.observe(viewLifecycleOwner) {
            val (maxLevel, exp) = it
            LogUtil.se("$maxLevel, $exp")
            cubeLevelBar.maxLevel = maxLevel
            cubeLevelBar.exp = exp
            if (reachMaxExp(exp, maxLevel)) {
                button.text = "突破"
                button.setShakelessClickListener {
                    onLevelBreak()
                }
            } else {
                button.text = "升级"
                button.setShakelessClickListener {
                    onLevelUp(it as TextView)
                }
            }
        }
        cubeViewModel.cube.observe(viewLifecycleOwner) {
            contentItem.setRichText(it.content)
        }
    }

    fun addPathByCube(pageContainer: ViewGroup, ownCube: OwnCube) {
        val cube = ownCube.cube
        when (cube.title) {
            "创世神" -> {
                pageContainer.H2("物品")
                pageContainer.simplePath(RouterHub.USER_AllOwnItemActivity)
                pageContainer.simplePath(RouterHub.USER_AllItemActivity)
                pageContainer.simplePath(RouterHub.USER_ThingItemEditorActivity)
                pageContainer.simplePath(RouterHub.USER_PackageItemEditorActivity)
                pageContainer.simplePath(RouterHub.USER_DataItemEditorActivity)
                pageContainer.simplePath(RouterHub.USER_EquipItemEditorActivity)
                pageContainer.simplePath(RouterHub.USER_BuffItemEditorActivity)
                pageContainer.simplePath(RouterHub.USER_CubeItemEditorActivity)

                pageContainer.H2("邮件")
                pageContainer.simplePath(RouterHub.USER_AllOwnMailActivity)
                pageContainer.simplePath(RouterHub.USER_AllMailActivity)
                pageContainer.simplePath(RouterHub.USER_MailEditorActivity)

                pageContainer.H2("方块")
                pageContainer.simplePath(RouterHub.USER_AllOwnCubeActivity)
                pageContainer.simplePath(RouterHub.USER_AllIdentityActivity)
                pageContainer.simplePath(RouterHub.USER_AllMetaPermissionActivity)
                pageContainer.simplePath(RouterHub.USER_AllHunPermissionActivity)
                pageContainer.simplePath(RouterHub.USER_AllRoleActivity)
                pageContainer.simplePath(RouterHub.USER_AllAuthActivity)
                pageContainer.simplePath(RouterHub.USER_CubeSettingEditorActivity)
                pageContainer.simplePath(RouterHub.USER_CubeAttrEditorActivity)
                pageContainer.simplePath(RouterHub.USER_CubeRolesEditorActivity)
                pageContainer.simplePath(RouterHub.USER_CubeSkillEditorActivity)
                pageContainer.simplePath(RouterHub.USER_CubeStarEditorActivity)
                pageContainer.simplePath(RouterHub.USER_AddMetaPermissionActivity)
                pageContainer.simplePath(RouterHub.USER_AddHunPermissionActivity)
                pageContainer.simplePath(RouterHub.USER_AddRoleActivity)
                pageContainer.simplePath(RouterHub.USER_AddIdentityActivity)
                pageContainer.simplePath(RouterHub.USER_AddAuthActivity)

                pageContainer.H2("活动")
                pageContainer.simplePath(RouterHub.USER_AllActivityActivity)
                pageContainer.simplePath(RouterHub.USER_AllOwnActivityActivity)
                pageContainer.simplePath(RouterHub.USER_UrlActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_TextUrlActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_OneTaskActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_SevenDaySignActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_AchievementActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_CardActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_DoubleActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_DreamActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_EveryDayMainActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_GetBackActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_LifeActivityEditorActivity)
                pageContainer.simplePath(RouterHub.USER_PriceActivityEditorActivity)

                pageContainer.H2("任务")
                pageContainer.simplePath(RouterHub.USER_AllTaskActivity)
                pageContainer.simplePath(RouterHub.USER_DataTaskEditorActivity)

                pageContainer.H2("商店")
                pageContainer.simplePath(RouterHub.USER_AllOwnShopActivity)
                pageContainer.simplePath(RouterHub.USER_AllShopActivity)
                pageContainer.simplePath(RouterHub.USER_ShopEditorActivity)

                pageContainer.H2("云")
                pageContainer.simplePath(RouterHub.USER_CloudActivity)

                pageContainer.H2("动态")
                pageContainer.simplePath(RouterHub.USER_AllMomentActivity)
                pageContainer.simplePath(RouterHub.USER_AddMomentActivity)

                pageContainer.H2("评论")
                pageContainer.simplePath(RouterHub.USER_AddCommentActivity)

                pageContainer.H2("帖子")
                pageContainer.simplePath(RouterHub.USER_AllForumActivity)
                pageContainer.simplePath(RouterHub.USER_AddForumActivity)
                pageContainer.simplePath(RouterHub.USER_AddPostActivity)

                pageContainer.H2("话题")
                pageContainer.simplePath(RouterHub.USER_AllTopicActivity)
                pageContainer.simplePath(RouterHub.USER_AddTopicActivity)

                pageContainer.H2("标签")
                pageContainer.simplePath(RouterHub.USER_AllTagActivity)
                pageContainer.simplePath(RouterHub.USER_AddTagActivity)

                pageContainer.H2("排行榜")
                pageContainer.simplePath(RouterHub.USER_AllLeaderBoardActivity)
                pageContainer.simplePath(RouterHub.USER_AllRecommendActivity)
                pageContainer.simplePath(RouterHub.USER_AddLeaderBoardActivity)
                pageContainer.simplePath(RouterHub.USER_AddRecommendActivity)

                pageContainer.H2("App")
                pageContainer.simplePath(RouterHub.USER_AllAppActivity)
                pageContainer.simplePath(RouterHub.USER_AddAndroidAppActivity)
                pageContainer.simplePath(RouterHub.USER_AddiOSAppActivity)
                pageContainer.simplePath(RouterHub.USER_AddMacAppActivity)
                pageContainer.simplePath(RouterHub.USER_AddPluginAppActivity)
                pageContainer.simplePath(RouterHub.USER_AddWebAppActivity)
                pageContainer.simplePath(RouterHub.USER_AddWindowsAppActivity)
                pageContainer.simplePath(RouterHub.USER_EditPluginAppActivity)
            }
        }
    }

    fun ViewGroup.simplePath(path: String) = PathNext(path, path)


    fun onLevelBreak() {
        val level = cubeViewModel.maxLevel.value ?: 1
        val exp = cubeViewModel.exp.value ?: 0L
        requireActivity().showLevelBreakDialog(level, exp)
    }

    fun onLevelUp(button: TextView) {
        fetchExpItems(button, I()) {
            button.hideProgress()
            val level = cubeViewModel.maxLevel.value ?: 1
            val exp = cubeViewModel.exp.value ?: 0L
            val id = cubeViewModel.objectId.value ?: ""
            requireActivity().showLevelUpDialog(level, exp, it) { dialog, btn, expOwnItem, count ->
                cubeViewModel attach useItem<Any?>(expOwnItem.objectId, count, id) {
                    onSuccess = {
                        val fakeExp = id2exp(expOwnItem.objectId) * count
                        val trueExp = cubeViewModel.exp.value ?: 0L + fakeExp
                        cubeViewModel.exp.postValue(trueExp)
                        dialog.dismiss()
                    }
                    onError = simpleErrorCallback
                    onComplete = { btn.hideProgress() }
                }
            }
        }
    }

    fun fetchExpItems(button: TextView, user: User, useItems: (List<OwnItem>) -> Unit) {
        button.showProgress()
        cubeViewModel attach requestOwnItem {
            query = user.allOwnExpItem()
            onEmpty = {
                MaterialDialog(requireActivity()).show {
                    message(text = "未持有任何经验！")
                    positiveButton(R.string.ok)
                }
            }
            onSuccess = {
                useItems(it)
            }
            onError = simpleErrorCallback
            onComplete = { button.hideProgress("升级") }
        }
    }
}