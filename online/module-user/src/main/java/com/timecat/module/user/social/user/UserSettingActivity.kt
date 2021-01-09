package com.timecat.module.user.social.user

import android.view.ViewGroup
import cn.bmob.v3.datatype.BmobFile
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data._User
import com.timecat.data.bmob.ext.bmob.updateUser
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.setting.ImageItem
import com.timecat.middle.setting.BaseSettingActivity
import com.timecat.module.user.ext.chooseImage
import com.timecat.module.user.ext.uploadImageByUser
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description 编辑资料
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_UserSettingActivity)
class UserSettingActivity : BaseSettingActivity() {

    @AttrValueAutowiredAnno("user")
    @JvmField
    var user: _User? = null

    override fun title(): String = "编辑资料"
    override fun routerInject() = NAV.inject(this)

    lateinit var avatarItem: ImageItem
    lateinit var coverItem: ImageItem

    override fun addSettingItems(container: ViewGroup) {
        if (user == null) {
            ToastUtil.w("找不到用户！")
            finish()
            return
        }
        user?.let { user ->
            avatarItem = simpleImage(container, "头像", user.avatar) { avatarItem ->
                chooseImage(isAvatar = true) { path ->
                    uploadImageByUser(user, listOf(path), false) {
                        for (i in it) {
                            val file = BmobFile(path, "avatar", i)
                            user.setheadPortrait(file)
                            updateUserObject(user) {
                                ToastUtil.ok("头像更换成功")
                                avatarItem.setImage(path)
                            }
                        }
                    }
                }
            }
            coverItem = simpleImage(container, "封面", user.cover) { coverItem ->
                chooseImage(isAvatar = false) { path ->
                    uploadImageByUser(user, listOf(path), false) {
                        for (i in it) {
                            val file = BmobFile(path, "cover", i)
                            user.coverPage = file
                            updateUserObject(user) {
                                ToastUtil.ok("封面更换成功")
                                coverItem.setImage(path)
                            }
                        }
                    }
                }
            }
            simpleNext(container, "名字", null, user.nickName, null) { item ->
                editNickname { item.hint = it }
            }
            simpleNext(container, "简介", null, user.brief_intro, null) { item ->
                editBriefIntro { item.hint = it }
            }
        }
    }

    private fun updateUserObject(user: _User, success: () -> Unit) {
        updateUser {
            target = user
            onSuccess = {
                ToastUtil.ok("修改成功")
                success()
            }
            onError = { e ->
                ToastUtil.e("修改失败$e")
                LogUtil.e(e)
            }
        }
    }

    private fun editNickname(success: (text: String) -> Unit) {
        user?.let {
            MaterialDialog(this).show {
                title(text = "昵称")
                input(hint = "阁下如何称呼", prefill = it.nickName, allowEmpty = false) { _, input: CharSequence? ->
                    if (input != null && input.length <= 10) {
                        val s = input.toString()
                        it.nickName = s
                        updateUserObject(it) {
                            success(s)
                        }
                    } else {
                        ToastUtil.e("太长了，请输入 10 个字或以内")
                    }
                }
            }
        }
    }

    private fun editBriefIntro(success: (text: String) -> Unit) {
        user?.let {
            MaterialDialog(this).show {
                title(text = "简介")
                input(hint = "一句话介绍一下自己", prefill = it.brief_intro, allowEmpty = false) { _, input: CharSequence? ->
                    if (input != null && input.length <= 20) {
                        val s = input.toString()
                        it.brief_intro = s
                        updateUserObject(it) {
                            success(s)
                        }
                    } else {
                        ToastUtil.e("太长了，请输入 20 个字或以内")
                    }
                }
            }
        }
    }
}