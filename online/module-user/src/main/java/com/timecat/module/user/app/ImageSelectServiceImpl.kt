package com.timecat.module.user.app

import android.app.Activity
import com.timecat.middle.image.ImageSelectService
import com.timecat.module.user.ext.*
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/12
 * @description null
 * @usage null
 */
@ServiceAnno(ImageSelectService::class)
class ImageSelectServiceImpl : ImageSelectService {
    override fun selectAvatar(activity: Activity, onSuccess: (String) -> Unit) {
        activity.chooseAvatar(onSuccess)
    }

    override fun selectImage(activity: Activity, onSuccess: (String) -> Unit) {
        activity.chooseImage(onSuccess)
    }

    override fun selectImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.chooseImage(iar, onSuccess)
    }

    override fun selectOneLocalIcon(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        activity.selectOneLocalIcon(onSuccess)
    }

    override fun selectOneLocalImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneLocalImage(iar, onSuccess)
    }

    override fun selectOneOnlineImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneOnlineImage(iar, onSuccess)
    }

    override fun selectOneRandomAvatar(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneRandomAvatar(onSuccess)
    }

    override fun selectOneRandomImage(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneRandomImage(onSuccess)
    }

    override fun takeOnePhoto(activity: Activity, cropId: Int, onSuccess: (String) -> Unit) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.takeOnePhoto(iar, onSuccess)
    }
}