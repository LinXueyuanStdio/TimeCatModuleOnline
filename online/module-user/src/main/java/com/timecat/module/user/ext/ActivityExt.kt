package com.timecat.module.user.ext

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.GridItem
import com.afollestad.materialdialogs.bottomsheets.gridItems
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.system.model.api.GitFileResponse
import com.timecat.data.system.model.api.GiteeFile
import com.timecat.data.system.network.WEB
import com.timecat.data.system.network.api.GiteeService
import com.timecat.element.alert.ToastUtil
import com.timecat.extend.image.IMG
import com.timecat.extend.image.savablePath
import com.timecat.extend.image.selectForResult
import com.timecat.identity.font.FontAwesome
import com.timecat.identity.font.FontDrawable
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import java.io.File
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/9
 * @description null
 * @usage null
 */
//region 图片上传
fun Activity.uploadImageByUser(
    currentUser: User,
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
//endregion

//region 图片处理逻辑

fun Activity.chooseAvatar(onSuccess: (String) -> Unit) {
    MaterialDialog(this).show {
        val choice = listOf(
            "拍照",
            "本地相册",
            "内置图标",
            "随机图标",
//                "我的在线相册" TODO
        )
        positiveButton(R.string.ok)
        listItemsSingleChoice(items = choice, initialSelection = 2) { dialog, index, text ->
            when (index) {
                0 -> {
                    //拍照
                    takeOnePhoto(ImageAspectRatio.Avatar, onSuccess)
                }
                1 -> {
                    //本地相册
                    selectOneLocalImage(ImageAspectRatio.Avatar, onSuccess)
                }
                2 -> {
                    //内置图标
                    selectOneLocalIcon(onSuccess)
                }
                3 -> {
                    //随机图标
                    selectOneRandomImage(ImageAspectRatio.Avatar, onSuccess)
                }
                4 -> {
                    //我的在线相册
                    selectOneOnlineImage(ImageAspectRatio.Avatar, onSuccess)
                }
            }
        }
    }
}

enum class ImageAspectRatio(
    val aspect_ratio_x: Int,
    val aspect_ratio_y: Int
) {
    Avatar(1, 1),
    Wallpaper(9, 16),
    Horizon(16, 9)
}

fun Activity.chooseImage(aspectRatio: ImageAspectRatio = ImageAspectRatio.Avatar, onSuccess: (String) -> Unit) {
    if (aspectRatio == ImageAspectRatio.Avatar) {
        chooseAvatar(onSuccess)
        return
    }
    MaterialDialog(this).show {
        val choice = listOf(
            "拍照",
            "本地相册",
            "随机图标",
//                "我的在线相册" TODO
        )
        positiveButton(R.string.ok)
        listItemsSingleChoice(items = choice, initialSelection = 2) { dialog, index, text ->
            when (index) {
                0 -> {
                    //拍照
                    takeOnePhoto(aspectRatio, onSuccess)
                }
                1 -> {
                    //本地相册
                    selectOneLocalImage(aspectRatio, onSuccess)
                }
                2 -> {
                    //随机图标
                    selectOneRandomImage(aspectRatio, onSuccess)
                }
                3 -> {
                    //我的在线相册
                    selectOneOnlineImage(aspectRatio, onSuccess)
                }
            }
        }
    }
}

fun Activity.takeOnePhoto(
    aspectRatio: ImageAspectRatio = ImageAspectRatio.Avatar,
    onSuccess: (String) -> Unit
) {
    val aspect_ratio_x = aspectRatio.aspect_ratio_x
    val aspect_ratio_y = aspectRatio.aspect_ratio_y
    IMG.select(PictureSelector.create(this).openCamera(PictureMimeType.ofImage()))
        .maxSelectNum(1)
        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
        .isGif(false)
        .isEnableCrop(true)
        .selectForResult {
            if (it.isNotEmpty()) {
                val media = it[0]
                val path = media.savablePath()
                if (path == null) {
                    ToastUtil.e_long("图片路径错误！")
                } else {
                    onSuccess(path)
                }
            }
        }
}

fun Activity.selectOneLocalImage(
    aspectRatio: ImageAspectRatio = ImageAspectRatio.Avatar,
    onSuccess: (String) -> Unit
) {
    val aspect_ratio_x = aspectRatio.aspect_ratio_x
    val aspect_ratio_y = aspectRatio.aspect_ratio_y
    IMG.select(PictureSelector.create(this).openGallery(PictureMimeType.ofImage()))
        .maxSelectNum(1)
        .withAspectRatio(aspect_ratio_x, aspect_ratio_y)
        .isGif(false)
        .isEnableCrop(true)
        .selectForResult {
            if (it.isNotEmpty()) {
                val media = it[0]
                val path = media.savablePath()
                if (path == null) {
                    ToastUtil.e_long("图片路径错误！")
                } else {
                    onSuccess(path)
                }
            }
        }
}

fun Activity.selectOneRandomImage(
    aspectRatio: ImageAspectRatio = ImageAspectRatio.Avatar,
    onSuccess: (String) -> Unit
) {
    val aspect_ratio_x = aspectRatio.aspect_ratio_x
    val aspect_ratio_y = aspectRatio.aspect_ratio_y
    onSuccess(IconLoader.randomAvatar(UUID.randomUUID().toString()))
}

fun Activity.selectOneOnlineImage(
    aspectRatio: ImageAspectRatio = ImageAspectRatio.Avatar,
    onSuccess: (String) -> Unit
) {
    val aspect_ratio_x = aspectRatio.aspect_ratio_x
    val aspect_ratio_y = aspectRatio.aspect_ratio_y
}

class FontAwesomeGridItem(val context: Context, val font: Typeface, val iconRes: Int) : GridItem {
    override val title: String
        get() = context.resources.getResourceEntryName(iconRes)

    override fun populateIcon(imageView: ImageView) {
        val fontDrawable = FontDrawable(context, font, iconRes)
        fontDrawable.setTextSize(24f)
        imageView.setImageDrawable(fontDrawable)
    }

    override fun configureTitle(textView: TextView) {
//        textView.typeface = font
////                    textView.ellipsize = TextUtils.TruncateAt.END
//        textView.setLines(1)
//        textView.gravity = Gravity.CENTER
//        textView.text =""// title.substring(3)
        textView.beGone()
    }

}

fun Activity.selectOneLocalIcon(onSuccess: (String) -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        val regularFont = FontAwesome.getFontAwesomeRegular(this@selectOneLocalIcon)
        val regularIcons = FontAwesome.regularIcons().map {
            FontAwesomeGridItem(this@selectOneLocalIcon, regularFont, it)
        }
        val maxRegularCount = regularIcons.size
        val solidFont = FontAwesome.getFontAwesomeSolid(this@selectOneLocalIcon)
        val solidIcons = FontAwesome.solidIcons().map {
            FontAwesomeGridItem(this@selectOneLocalIcon, solidFont, it)
        }.plus(regularIcons)
        val maxSolidCount = solidIcons.size + maxRegularCount
        val brandFont = FontAwesome.getFontAwesomeBrand(this@selectOneLocalIcon)
        val brandIcons = FontAwesome.brandIcons().map {
            FontAwesomeGridItem(this@selectOneLocalIcon, brandFont, it)
        }
        val maxBrandCount = brandIcons.size + maxSolidCount
        val icons = regularIcons.plus(solidIcons).plus(brandIcons)
        withContext(Dispatchers.Main) {
            MaterialDialog(this@selectOneLocalIcon, BottomSheet()).show {
                positiveButton(R.string.ok)
                negativeButton(R.string.cancel)
                gridItems(icons, R.integer.icon_row_count) { _, idx, item ->
                    val text = item.title
                    val url = when (idx) {
                        in 0 until maxRegularCount -> "${IconLoader.FONTAWESOME_SCHEME}${IconLoader.FONTAWESOME_REGULAR}$text"
                        in maxRegularCount until maxSolidCount -> "${IconLoader.FONTAWESOME_SCHEME}${IconLoader.FONTAWESOME_SOLID}$text"
                        in maxSolidCount until maxBrandCount -> "${IconLoader.FONTAWESOME_SCHEME}${IconLoader.FONTAWESOME_BRAND}$text"
                        else -> "${IconLoader.FONTAWESOME_SCHEME}$text"
                    }
                    LogUtil.sd(url)
                    onSuccess(url)
                }
            }
        }
    }
}
//endregion

//region 图像保存逻辑
fun Activity.receiveImage(
    currentUser: User,
    filePaths: List<String>,
    isCompress: Boolean,
    onFinish: (List<String>) -> Unit
) {
    val needUpload = mutableListOf<String>()
    val ans = mutableListOf<String>()
    for (path in filePaths) {
        if (path.startsWith(IconLoader.AVATAR_SCHEME) ||
            path.startsWith(IconLoader.FONTAWESOME_SCHEME) ||
            path.startsWith(IconLoader.APP_SCHEME) ||
            path.startsWith(IconLoader.BUILDIN_DRAWABLE_SCHEME) ||
            path.startsWith(IconLoader.BUILDIN_MIPMAP_SCHEME)) {
            ans.add(path)
        } else {
            needUpload.add(path)
        }
    }
    uploadImageByUser(currentUser, needUpload, isCompress) {
        ans.addAll(it)
        onFinish(ans)
    }
}

