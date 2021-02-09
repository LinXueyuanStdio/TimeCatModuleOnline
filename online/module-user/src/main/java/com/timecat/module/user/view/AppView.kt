package com.timecat.module.user.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlockCount
import com.timecat.data.bmob.ext.focusSum
import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.*
import com.timecat.identity.service.PluginService
import com.timecat.module.user.R
import com.timecat.module.user.adapter.ImageAdapter
import com.timecat.module.user.base.LOAD
import com.timecat.module.user.view.dsl.setupFollowBlockButton
import kotlinx.android.synthetic.main.header_app_layout_detail.view.*
import kotlinx.android.synthetic.main.header_moment_detail.view.userSection
import kotlinx.android.synthetic.main.user_base_item_comment_header.view.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description null
 * @usage null
 */
class AppView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {
    lateinit var root: View

    init {
        init(context)
    }

    private fun init(context: Context, layout: Int = R.layout.header_app_layout_detail) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)
        root.focus.tag = "关注"
    }

    class InitItem(var title: String)

    /**
     * 必须调用，初始化
     */
    fun bindBlock(block: Block, onInit: (InitItem) -> Unit) {
        setCommentSum(block.comments)
        initAvater(root, block)
        initStatistic(root, block)
        initFocusButton(root, block)
        root.introDetail.setText(block.content)
        val app = AppBlock.fromJson(block.structure)
        initByAppBlock(root, app, onInit)
        root.userSection.bindBlock(block.user)
    }

    fun setCommentSum(count: Int) {
        root.commentSumTitle.text = "评论 ($count)"
        root.commentSum.text = "$count 评论"
    }

    private fun initAvater(root: View, block: Block) {
        LOAD.image("R.drawable.ic_launcher", root.icon)
        root.appname.text = block.title
    }

    private fun initFocusButton(root: View, block: Block) {
        block.focusSum {
            root.focusSum.text = "${it} 关注"
        }
        setupFollowBlockButton(context, root.focus, block) {
            block.focusSum {
                root.focusSum.text = "${it} 关注"
            }
        }
    }

    private fun initStatistic(root: View, block: Block) {
        requestBlockCount {
            query = block.findAllComment()
            onSuccess = {
                root.commentSum.text = "$it 评论"
            }
        }
        root.downloadSum.text = "${block.usedBy} 浏览"
    }

    private fun initByAppBlock(root: View, app: AppBlock, onInit: (InitItem) -> Unit) {
        when (app.type) {
            APP_AndroidApp -> {
                val androidApp = AndroidApp.fromJson(app.structure)
                onInit(InitItem("Android 应用"))
                initByAndroidApp(root, androidApp)
            }
            APP_WebApp -> {
                val webApp = WebApp.fromJson(app.structure)
                onInit(InitItem("网页"))
                initByWebApp(root, webApp)
            }
            APP_iOS -> {
                val iOSApp = iOSApp.fromJson(app.structure)
                onInit(InitItem("iOS 应用"))
                initByiOSApp(root, iOSApp)
            }
            APP_Mac -> {
                val macApp = MacApp.fromJson(app.structure)
                onInit(InitItem("Mac 应用"))
                initByMacApp(root, macApp)
            }
            APP_Linux -> {
                val linuxApp = LinuxApp.fromJson(app.structure)
                onInit(InitItem("Linux 应用"))
                initByLinuxApp(root, linuxApp)
            }
            APP_Windows -> {
                val windowsApp = WindowsApp.fromJson(app.structure)
                onInit(InitItem("Windows 应用"))
                initByWindowsApp(root, windowsApp)
            }
            APP_PluginManager -> {
                val pluginApp = PluginApp.fromJson(app.structure)
                onInit(InitItem("插件管理器"))
                initByPluginApp(root, pluginApp, false)
            }
            APP_Plugin -> {
                val pluginApp = PluginApp.fromJson(app.structure)
                onInit(InitItem("云插件"))
                initByPluginApp(root, pluginApp, true)
            }
        }

    }

    private fun initByAndroidApp(root: View, androidApp: AndroidApp) {
        root.version.text = androidApp.latestVersion
        root.rom.text = androidApp.minROM
        root.preface.adapter = ImageAdapter(androidApp.show!!)
        root.updateDetail.setText(androidApp.updateInfo!!)

        root.download.text = "下载"
        root.download.setOnClickListener {
            ToastUtil.w("暂不可用")
        }
    }

    private fun initByWebApp(root: View, webApp: WebApp) {
        root.rom.visibility = View.GONE
        root.version.visibility = View.GONE
        root.updateInfo.visibility = View.GONE
        root.updateDetail.visibility = View.GONE
        root.preface.visibility = View.GONE
        root.download.text = "进入"
        root.download.setOnClickListener {
            val url = webApp.appUrl
            HERF.gotoUrl(context, url)
        }
    }

    private fun initByiOSApp(root: View, iOSApp: iOSApp) {
        root.rom.visibility = View.GONE
        root.version.visibility = View.GONE

        root.updateInfo.visibility = View.GONE
        root.updateDetail.visibility = View.GONE
        root.preface.visibility = View.GONE
        root.download.text = "下载"
        root.download.setOnClickListener {
            ToastUtil.w("暂不可用")
        }
    }

    private fun initByMacApp(root: View, macApp: MacApp) {
        root.rom.visibility = View.GONE
        root.version.visibility = View.GONE
        root.updateInfo.visibility = View.GONE
        root.updateDetail.visibility = View.GONE
        root.preface.visibility = View.GONE

        root.download.text = "下载"
        root.download.setOnClickListener {
            ToastUtil.w("暂不可用")
        }
    }

    private fun initByLinuxApp(root: View, linuxApp: LinuxApp) {
        root.rom.visibility = View.GONE
        root.version.visibility = View.GONE
        root.updateInfo.visibility = View.GONE
        root.updateDetail.visibility = View.GONE
        root.preface.visibility = View.GONE

        root.download.text = "下载"
        root.download.setOnClickListener {
            ToastUtil.w("暂不可用")
        }
    }

    private fun initByWindowsApp(root: View, windowsApp: WindowsApp) {
        root.rom.visibility = View.GONE
        root.version.visibility = View.GONE
        root.updateInfo.visibility = View.GONE
        root.updateDetail.visibility = View.GONE
        root.preface.visibility = View.GONE

        root.download.text = "下载"
        root.download.setOnClickListener {
            ToastUtil.w("暂不可用")
        }
    }

    private fun initByPluginApp(root: View, pluginApp: PluginApp, isPlugin: Boolean) {

        root.rom.visibility = View.GONE
        root.version.visibility = View.VISIBLE
        root.version.text = pluginApp.updateInfo.first().version_name
        root.updateInfo.visibility = View.VISIBLE
        root.updateDetail.visibility = View.VISIBLE
        root.updateDetail.setText(pluginApp.updateInfo.first().updateInfo)
        root.preface.visibility = View.GONE

        root.download.text = "下载"

        val s: PluginService? = NAV.service(PluginService::class.java)

        fun checkDownloadButtonStatus(filename: String) {
            if (s?.existPlugin(filename) == true) {
                if (!isPlugin) {
                    download.text = "已安装"
                } else {
                    download.text = "打开"
                }
            } else {
                download.text = "下载"
            }
        }
        checkDownloadButtonStatus(pluginApp.packageName)
        root.download.setOnClickListener {
            s?.apply {
                val input = object : PluginService.InputToPlugin {
                    override fun downloadUrl(): String {
                        return pluginApp.updateInfo.first().download_url
                    }

                    override fun uri(): Uri? {
                        return null
                    }

                    override fun isPlugin(): Boolean {
                        return isPlugin
                    }

                    override fun activityClassName(): String {
                        return pluginApp.activity_class_names.first()
                    }

                    override fun extra(): Bundle {
                        return Bundle()
                    }

                    override fun action(): String? {
                        return null
                    }

                    override fun partKey(): String {
                        return ""
                    }

                    override fun filename(): String {
                        return pluginApp.packageName
                    }
                }
                val output = object : PluginService.OutputFromPlugin {
                    override fun onDownloadSuccess() {
                        if (root.download != null) {
                            root.download.post {
                                root.download.isEnabled = true
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun onDownloadFail(s: String) {
                        if (root.download != null) {
                            root.download.post {
                                root.download.isEnabled = true
                                ToastUtil.e(s)
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun setEnabled(enabled: Boolean) {
                        root.download.isEnabled = enabled
                    }

                    override fun onEnterFail(s: String) {
                        if (root.download != null) {
                            root.download.post {
                                root.download.isEnabled = true
                                ToastUtil.e(s)
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun onEnterComplete() {
                        if (root.download != null) {
                            root.download.post {
                                root.download.isEnabled = true
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun onLoading(view: View?) {
                    }

                    override fun onDownloadProgress(progress: Int, total: Int) {
                    }

                    override fun onCloseLoadingView() {
                    }

                    override fun onDownloadStart() {
                        root.download.text = "下载中"
                    }
                }
                start(input, output)
            }
        }
    }
}