package com.timecat.module.plugin.manager

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import java.io.File
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
data class PluginModel(
    val name: String,
    val data: List<PluginInfo>
) : Serializable

const val RemotePlugin = 0
const val AssetsPlugin = 1
const val LocalPlugin = 2
data class PluginInfo(
    val uuid: String,
    val type: Int,
    val name: String,
    val versionCode: Int,
    val versionName: String,

    val managerUrl: String,
    /**
     * 动态加载的插件管理apk
     */
    val managerFilename: String,

    val pluginZipUrl: String,
    /**
     * 动态加载的插件包，里面包含以下几个部分，插件apk，插件框架apk（loader apk和runtime apk）, apk信息配置关系json文件
     */
    val pluginZipFilename: String,
) : Serializable {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): PluginInfo {
            val uuid: String = jsonObject.getString("uuid")
            val type: Int = jsonObject.getInteger("type")
            val name: String = jsonObject.getString("name")
            val versionCode: Int = jsonObject.getInteger("versionCode")
            val versionName: String = jsonObject.getString("versionName")
            val managerUrl: String = jsonObject.getString("managerUrl")
            val managerFilename: String = jsonObject.getString("managerFilename")
            val pluginZipUrl: String = jsonObject.getString("pluginZipUrl")
            val pluginZipFilename: String = jsonObject.getString("pluginZipFilename")
            return PluginInfo(
                uuid, type, name, versionCode, versionName, managerUrl, managerFilename, pluginZipUrl, pluginZipFilename
            )
        }
    }

    fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["uuid"] = uuid
        jsonObject["type"] = type
        jsonObject["name"] = name
        jsonObject["versionCode"] = versionCode
        jsonObject["versionName"] = versionName
        jsonObject["managerUrl"] = managerUrl
        jsonObject["managerFilename"] = managerFilename
        jsonObject["pluginZipUrl"] = pluginZipUrl
        jsonObject["pluginZipFilename"] = pluginZipFilename
        return jsonObject
    }

    fun getPluginManagerFile(context: Context): File = File(Plugin.fileInPluginDir(context, managerFilename))
    fun getPluginZipFile(context: Context): File = File(Plugin.fileInPluginDir(context, pluginZipFilename))
}
