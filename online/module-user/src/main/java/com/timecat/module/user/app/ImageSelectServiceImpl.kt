package com.timecat.module.user.app

import android.app.Activity
import com.timecat.identity.service.ImageSelectService
import com.timecat.identity.service.UploadCallback
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

    override fun selectAvatar(activity: Activity, callback: UploadCallback) {
        activity.chooseAvatar { callback.onSuccess(it) }
    }

    override fun selectImage(activity: Activity, callback: UploadCallback) {
        activity.chooseImage { callback.onSuccess(it) }
    }

    override fun selectImage(activity: Activity, cropId: Int, callback: UploadCallback) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.chooseImage(iar) { callback.onSuccess(it) }
    }

    override fun selectOneLocalIcon(activity: Activity, cropId: Int, callback: UploadCallback) {
        activity.selectOneLocalIcon { callback.onSuccess(it) }
    }

    override fun selectOneLocalImage(activity: Activity, cropId: Int, callback: UploadCallback) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneLocalImage(iar) { callback.onSuccess(it) }
    }

    override fun selectOneOnlineImage(activity: Activity, cropId: Int, callback: UploadCallback) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneOnlineImage(iar) { callback.onSuccess(it) }
    }

    override fun selectOneRandomAvatar(activity: Activity, cropId: Int, callback: UploadCallback) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneRandomAvatar { callback.onSuccess(it) }
    }

    override fun selectOneRandomImage(activity: Activity, cropId: Int, callback: UploadCallback) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.selectOneRandomImage { callback.onSuccess(it) }
    }

    override fun takeOnePhoto(activity: Activity, cropId: Int, callback: UploadCallback) {
        val iar = ImageAspectRatio.values().find { it.id == cropId } ?: ImageAspectRatio.Avatar
        activity.takeOnePhoto(iar) { callback.onSuccess(it) }
    }
}