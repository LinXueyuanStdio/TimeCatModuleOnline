package com.timecat.module.user.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.timecat.component.commonsdk.helper.HERF
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestBlockCount
import com.timecat.data.bmob.ext.focusSum
import com.timecat.data.bmob.ext.net.findAllComment
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.*
import com.timecat.identity.service.PluginService
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.middle.block.view.ExpandableTextView
import com.timecat.module.user.R
import com.timecat.module.user.adapter.ImageAdapter
import com.timecat.module.user.view.dsl.setupFollowBlockButton

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

    lateinit var placeholder: View

    lateinit var icon: ImageView
    lateinit var appname: TextView

    lateinit var downloadSum: TextView
    lateinit var download: TextView
    lateinit var commentSum: TextView
    lateinit var focusSum: TextView
    lateinit var focus: TextView

    lateinit var preface: RecyclerView
    lateinit var introDetail: ExpandableTextView

    lateinit var userHead: UserHeadView
    lateinit var share: ShareView

    private fun init(context: Context, layout: Int = R.layout.header_app_layout_detail) {
        val inflater = LayoutInflater.from(context)
        root = inflater.inflate(layout, this)

        placeholder = root.findViewById(R.id.placeholder)

        icon = root.findViewById(R.id.icon)
        appname = root.findViewById(R.id.appname)

        downloadSum = root.findViewById(R.id.downloadSum)
        download = root.findViewById(R.id.download)
        commentSum = root.findViewById(R.id.commentSum)
        focusSum = root.findViewById(R.id.focusSum)
        focus = root.findViewById(R.id.focus)

        preface = root.findViewById(R.id.preface)
        introDetail = root.findViewById(R.id.introDetail)

        userHead = root.findViewById(R.id.userSection)
        share = root.findViewById(R.id.share)

        orientation = VERTICAL
    }

    fun setPlaceholderHeight(height: Int) {
        placeholder.updateLayoutParams<LayoutParams> {
            this.height = height
        }
    }

    class InitItem(var title: String)

    /**
     * 必须调用，初始化
     */
    fun bindBlock(block: Block, onInit: (InitItem) -> Unit) {
        initAvater(root, block)
        initStatistic(root, block)
        initFocusButton(block)
        introDetail.setText(block.content)
        val app = AppBlock.fromJson(block.structure)
        initByAppBlock(root, block.subtype, app, onInit)
        userHead.bindBlock(block.user)
    }

    private fun initAvater(root: View, block: Block) {
        IconLoader.loadIcon(context, icon, "R.drawable.ic_launcher")
        appname.text = block.title
    }

    private fun initFocusButton(block: Block) {
        block.focusSum {
            focusSum.text = "${it} 关注"
        }
        setupFollowBlockButton(context, focus, block) {
            block.focusSum {
                focusSum.text = "${it} 关注"
            }
        }
    }

    private fun initStatistic(root: View, block: Block) {
        requestBlockCount {
            query = block.findAllComment()
            onSuccess = {
                commentSum.text = "$it 评论"
            }
        }
        downloadSum.text = "${block.usedBy} 浏览"
    }

    private fun initByAppBlock(root: View, subtype: Int, app: AppBlock, onInit: (InitItem) -> Unit) {
        app.mediaScope?.let {
            preface.adapter = ImageAdapter(it.getPhoto())
        }
        when (subtype) {
            APP_AndroidApp -> {
                val androidApp = AndroidApp.fromJson(app.structure)
                onInit(InitItem("Android 应用"))
                initByAndroidApp(root, androidApp)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
            APP_WebApp -> {
                val webApp = WebApp.fromJson(app.structure)
                onInit(InitItem("网页"))
                initByWebApp(root, webApp)
                download.text = "访问"
                download.setShakelessClickListener {
                    HERF.gotoUrl(context, app.url)
                }
            }
            APP_iOS -> {
                val iOSApp = iOSApp.fromJson(app.structure)
                onInit(InitItem("iOS 应用"))
                initByiOSApp(root, iOSApp)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
            APP_Mac -> {
                val macApp = MacApp.fromJson(app.structure)
                onInit(InitItem("Mac 应用"))
                initByMacApp(root, macApp)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
            APP_Linux -> {
                val linuxApp = LinuxApp.fromJson(app.structure)
                onInit(InitItem("Linux 应用"))
                initByLinuxApp(root, linuxApp)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
            APP_Windows -> {
                val windowsApp = WindowsApp.fromJson(app.structure)
                onInit(InitItem("Windows 应用"))
                initByWindowsApp(root, windowsApp)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
            APP_PluginManager -> {
                val pluginApp = PluginApp.fromJson(app.structure)
                onInit(InitItem("插件管理器"))
                initByPluginApp(root, pluginApp, false)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
            APP_Plugin -> {
                val pluginApp = PluginApp.fromJson(app.structure)
                onInit(InitItem("云插件"))
                initByPluginApp(root, pluginApp, true)
                download.text = "下载"
                download.setShakelessClickListener {
                    ToastUtil.w("暂不可用")
                }
            }
        }

    }

    private fun initByAndroidApp(root: View, androidApp: AndroidApp) {

    }

    private fun initByWebApp(root: View, webApp: WebApp) {

    }

    private fun initByiOSApp(root: View, iOSApp: iOSApp) {

    }

    private fun initByMacApp(root: View, macApp: MacApp) {

    }

    private fun initByLinuxApp(root: View, linuxApp: LinuxApp) {

    }

    private fun initByWindowsApp(root: View, windowsApp: WindowsApp) {

    }

    private fun initByPluginApp(root: View, pluginApp: PluginApp, isPlugin: Boolean) {

        download.text = "下载"

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
        download.setOnClickListener {
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
                        if (download != null) {
                            download.post {
                                download.isEnabled = true
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun onDownloadFail(s: String) {
                        if (download != null) {
                            download.post {
                                download.isEnabled = true
                                ToastUtil.e(s)
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun setEnabled(enabled: Boolean) {
                        download.isEnabled = enabled
                    }

                    override fun onEnterFail(s: String) {
                        if (download != null) {
                            download.post {
                                download.isEnabled = true
                                ToastUtil.e(s)
                                checkDownloadButtonStatus(pluginApp.packageName)
                            }
                        }
                    }

                    override fun onEnterComplete() {
                        if (download != null) {
                            download.post {
                                download.isEnabled = true
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
                        download.text = "下载中"
                    }
                }
                start(input, output)
            }
        }
    }
}