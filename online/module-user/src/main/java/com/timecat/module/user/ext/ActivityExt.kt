package com.timecat.module.user.ext

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.data._User
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.system.model.api.GitFileResponse
import com.timecat.data.system.model.api.GiteeFile
import com.timecat.data.system.network.WEB
import com.timecat.data.system.network.api.GiteeService
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description null
 * @usage null
 */
fun Activity.uploadImageByUser(
    currentUser: _User,
    filePaths: List<String>,
    isCompress: Boolean,
    onFinish: (List<String>) -> Unit
) {
    fun getUploadText(done: Int, all: Int): String {
        return if (isCompress) {
            "正在上传压缩图中($done/${all})"
        } else {
            "正在上传原图中($done/${all})"
        }
    }

    val list = arrayListOf<String>()
    val d = MaterialDialog(this).show {
        message(text = getUploadText(0, filePaths.size))
        cancelOnTouchOutside(false)
    }
    LogUtil.se("")
    Observable.concat(filePaths.map {
        val file = File(it)
        val bytes = FileIOUtils.readFile2BytesByStream(file)
        val encode2String: String = EncodeUtils.base64Encode2String(bytes)
        val today = DateTime().toString("yyyyMMdd")
        val HHmm = DateTime().toString("HHmm-ss-")
        val path = if (isCompress) {
            "${GiteeService.imagePathPrefix}${today}/compress/$HHmm${file.name}"
        } else {
            "${GiteeService.imagePathPrefix}${today}/$HHmm${file.name}"
        }
        WEB.gitee().upload(
            GiteeService.owner, GiteeService.repo, path,
            GiteeFile(
                message = "${currentUser.objectId} username=${currentUser.username} email=${currentUser.email}",
                content = encode2String, access_token = GiteeService.GiteeToken
            )
        ).subscribeOn(Schedulers.io())
    })
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<GitFileResponse> {
            override fun onComplete() {
                list.forEach { LogUtil.se(it) }
                onFinish(list)
                d.dismiss()
            }

            override fun onSubscribe(p0: Disposable) {
            }

            override fun onNext(resp: GitFileResponse) {
                LogUtil.se("${resp.content.name}  => ${resp.content.path}")
                val url = resp.content.download_url
                val newUrlIdx = url.indexOf(GiteeService.imagePathPrefix)
                val token = url.lastIndexOf("?")

                val trueUrl = if (token > 0) {
                    GiteeService.urlPathPrefix + url.substring(newUrlIdx, token)
                } else {
                    GiteeService.urlPathPrefix + url.substring(newUrlIdx)
                }
                LogUtil.se(trueUrl)
                list.add(trueUrl)
                d.message(text = getUploadText(list.size, filePaths.size))
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
                list.forEach { LogUtil.se(it) }
                ToastUtil.e("上传失败 " + e.message)
                d.dismiss()
            }
        })
}
