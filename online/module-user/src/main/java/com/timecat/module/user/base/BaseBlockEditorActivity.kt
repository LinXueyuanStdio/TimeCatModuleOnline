package com.timecat.module.user.base

import android.app.Activity
import android.content.Intent
import com.afollestad.materialdialogs.MaterialDialog
import com.blankj.utilcode.util.EncodeUtils
import com.blankj.utilcode.util.FileIOUtils
import com.shuyu.textutillib.RichEditBuilder
import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.system.model.api.GitFileResponse
import com.timecat.data.system.model.api.GiteeFile
import com.timecat.data.system.network.WEB
import com.timecat.data.system.network.api.GiteeService
import com.timecat.extend.image.savablePath
import com.timecat.layout.ui.business.keyboardManager.SmartKeyboardManager
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginEditorActivity
import com.timecat.identity.data.base.AIT_PHOTO
import com.timecat.identity.data.base.AttachmentItem
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.middle.block.util.KeyboardUtil
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.user_activity_moment_add.*
import org.joda.time.DateTime
import java.io.File

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 重量级编辑器
 * @usage 简化添加 社区 block 流程
 */
abstract class BaseBlockEditorActivity : BaseLoginEditorActivity() {
    companion object {
        val REQUEST_USER_CODE_INPUT = 1111
        val REQUEST_USER_CODE_CLICK = 2222
        val REQUEST_TOPIC_CODE_INPUT = 3333
        val REQUEST_TOPIC_CODE_CLICK = 4444
    }

    /**
     * 涉及的话题
     */
    private val topicModels = ArrayList<TopicModel>()

    /**
     * 提及的用户
     */
    private val nameList = ArrayList<UserModel>()

    /**
     * 当前用户
     */
    protected val currentUser = UserDao.getCurrentUser()

    override fun layout(): Int = R.layout.user_activity_moment_add

    override fun initViewAfterLogin() {
        title_tv.setText(title())
        SmartKeyboardManager.Builder(this)
            .setEditText(emojiEditText)
            .setContentView(content_view)
            .addKeyboard(emoji, emojiLayout)
            .create()
        add_pos.setOnClickListener {

        }

        privacy.setOnClickListener {

        }

        photo.setOnClickListener {
            onAddImage()
        }

        at.setOnClickListener {
            val intent = Intent(this, UserListActivity::class.java)
            startActivityForResult(intent, REQUEST_USER_CODE_CLICK)
        }

        topic.setOnClickListener {
            val intent = Intent(this, TopicListActivity::class.java)
            startActivityForResult(intent, REQUEST_TOPIC_CODE_CLICK)
        }

        block.setOnClickListener {

        }

        more.setOnClickListener {

        }

        keyboard.setOnClickListener {
            KeyboardUtil.hideKeyboard(emojiEditText)
        }

        publish.setOnClickListener {
            KeyboardUtil.hideKeyboard(emojiEditText)
            val content = emojiEditText.realText
            val originImgs = imageAdapter.data.map { it.savablePath() }
            if (originImgs.isEmpty()) {
                publish(content, null)
            } else {
                uploadImage(originImgs, false) { origins ->
                    publish(
                        content, AttachmentTail(
                            origins.map {
                                AttachmentItem(AIT_PHOTO, it)
                            }.toMutableList()
                        )
                    )
                }
            }
        }

        val richEditBuilder = RichEditBuilder()
        richEditBuilder.setEditText(emojiEditText)
            .setTopicModels(topicModels)
            .setUserModels(nameList)
            .setColorAtUser("#2962FF")
            .setColorTopic("#2962FF")
            .setEditTextAtUtilJumpListener(object : OnEditTextUtilJumpListener {
                override fun notifyAt() {
                    val intent = Intent(this@BaseBlockEditorActivity, UserListActivity::class.java)
                    startActivityForResult(intent, REQUEST_USER_CODE_INPUT)
                }

                override fun notifyTopic() {
                    val intent = Intent(this@BaseBlockEditorActivity, TopicListActivity::class.java)
                    startActivityForResult(intent, REQUEST_TOPIC_CODE_INPUT)
                }
            })
            .builder()
    }

    abstract fun publish(content: String, attachments: AttachmentTail?)

    private fun uploadImage(
        filePaths: List<String?>,
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
        Observable.concat(filePaths.filterNotNull().map {
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
                    message = "${currentUser?.objectId}" +
                            " username=${currentUser?.username}" +
                            " email=${currentUser?.email}",
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    REQUEST_USER_CODE_CLICK -> emojiEditText.resolveAtResult(
                        it.getSerializableExtra(
                            UserListActivity.DATA
                        ) as UserModel
                    )
                    REQUEST_USER_CODE_INPUT -> emojiEditText.resolveAtResultByEnterAt(
                        it.getSerializableExtra(
                            UserListActivity.DATA
                        ) as UserModel
                    )

                    REQUEST_TOPIC_CODE_INPUT -> emojiEditText.resolveTopicResultByEnter(
                        it.getSerializableExtra(
                            TopicListActivity.DATA
                        ) as TopicModel
                    )
                    REQUEST_TOPIC_CODE_CLICK -> emojiEditText.resolveTopicResult(
                        it.getSerializableExtra(
                            TopicListActivity.DATA
                        ) as TopicModel
                    )
                }
            }
        }
    }
}