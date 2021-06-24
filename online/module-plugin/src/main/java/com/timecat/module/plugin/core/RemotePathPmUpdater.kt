package com.timecat.module.plugin.core

import android.content.Context
import com.blankj.utilcode.util.FileUtils
import com.tencent.shadow.dynamic.host.PluginManagerUpdater
import com.timecat.extend.arms.BaseApplication
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.system.network.RetrofitHelper
import com.timecat.component.setting.DEF
import com.timecat.module.plugin.common.PluginConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.*
import java.util.concurrent.Future

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description 这个Updater没有任何升级能力。直接将指定路径作为其升级结果。
 * @usage 更新 Manager
 */
abstract class RemotePathPmUpdater(context: Context) : PluginManagerUpdater {

    private var apk: File = getApk(context)

    private fun getApk(context: Context): File {
        val code = getPluginManagerVersionCode()
        val path = getPluginManagerAbsPath(context, code)
        return File(path)
    }

    open fun getPluginManagerVersionCode(): Int =
        PluginConstants.getPluginManagerVersionCode(managerVersionCodeStr())

    open fun getPluginManagerAbsPath(context: Context, code: Int): String =
        PluginConstants.getPluginManagerAbsPath(context, name() + code)

    open fun updatingStr(): String = "ManagerUpdating"
    open fun managerVersionCodeStr(): String = "ManagerVersionCode"
    open fun name(): String = "Common"
    open fun managerJsonFileName(): String = "manager.json"

    /**
     * @return `true`表示之前更新过程中意外中断了
     */
    override fun wasUpdating(): Boolean {
        LogUtil.se("")
        return DEF.plugin().getBoolean(updatingStr(), false)
    }

    /**
     * 更新 TODO
     *
     * @return 当前最新的PluginManager，可能是之前已经返回过的文件，但它是最新的了。
     */
    override fun update(): Future<File>? {
        var versionCode = -1
        return RetrofitHelper.getPluginService()[managerJsonFileName()].flatMap {
            versionCode = it.data.first().data.first().plugin_version_code
            val old = DEF.plugin().getInt(managerVersionCodeStr(), 0)
            LogUtil.se("$old -> $versionCode")
            //当最新云端版本小于当前本地版本时，我们的本地版本是合法的
            if (versionCode <= old) {
                //不需要更新
                Observable.just(ResponseBody.create(null, ""))
            } else {
                //需要更新
                LogUtil.se("开始更新")
                DEF.plugin().putBoolean(updatingStr(), true)
                LogUtil.se("获得配置")
                val plugin_info = it.data.first().data.first()
                val url = plugin_info.plugin_download_url
                LogUtil.se(url)
                val path = getPluginManagerAbsPath(BaseApplication.getContext(), versionCode)
                apk = File(path)
                FileUtils.createOrExistsFile(apk)
                RetrofitHelper.getPluginService().downloadFile(url)
            }
        }.map {
            if (it.contentType() != null) {
                //下载文件
                LogUtil.se("下载文件")
                var os: OutputStream? = null
                val inputStream = it.byteStream() //获取下载输入流
                try {
                    os = FileOutputStream(apk) //输出流
                    val buff = ByteArray(1024)
                    var len: Int = inputStream.read(buff)
                    while (len != -1) {
                        os.write(buff, 0, len)
                        len = inputStream.read(buff)
                    }

                    LogUtil.se("下载完成")
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                } finally {
                    if (os != null) {
                        try {
                            os.close() //关闭输出流
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    try {
                        inputStream.close() //关闭输入流
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    DEF.plugin().putInt(managerVersionCodeStr(), versionCode)
                    DEF.plugin().putBoolean(updatingStr(), false)
                }
            }
            apk
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toFuture()
    }

    /**
     * 获取本地最新可用的
     *
     * @return `null`表示本地没有可用的
     */
    override fun getLatest(): File? {
        if (apk.exists()) return apk
        return null
    }

    /**
     * 查询是否可用
     * 查询服务器最新 Manager id，与参数file的 id 比较
     * @param file PluginManagerUpdater返回的file
     * @return `true`表示可用，`false`表示不可用
     */
    override fun isAvailable(file: File?): Future<Boolean> {
        LogUtil.se(file?.absolutePath)
        return RetrofitHelper.getPluginService()[managerJsonFileName()].map {
            val version = it.data.first().data.first().plugin_version_code
            val old = DEF.plugin().getInt(managerVersionCodeStr(), 0)
            LogUtil.se("$old -> $version")
            //当最新云端版本小于当前本地版本时，我们的本地版本是合法的
            version <= old
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toFuture()
    }

    fun needWaitingUpdate(): Boolean {
        LogUtil.se("")
        return (wasUpdating()//之前正在更新中，暗示更新出错了，应该放弃之前的缓存
                || latest == null)//没有本地缓存
    }
}