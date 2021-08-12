package com.timecat.module.user.app

import android.app.Activity
import com.timecat.data.bmob.dao.UserDao
import com.timecat.identity.service.ImageUploadService
import com.timecat.identity.service.UploadCallback
import com.timecat.module.user.ext.receiveImage
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/12
 * @description null
 * @usage null
 */
@ServiceAnno(ImageUploadService::class)
class ImageUploadServiceImpl : ImageUploadService {
    override fun upload(activity: Activity, path: String, uploadCallback: UploadCallback?) {
        val user = UserDao.getCurrentUser()
        if (user == null) {
            uploadCallback?.onFail("未登录")
            return
        }
        activity.receiveImage(user, listOf(path), false) {
            for (i in it) {
                uploadCallback?.onSuccess(i)
            }
        }
    }
}