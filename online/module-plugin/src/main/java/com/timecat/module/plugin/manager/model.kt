package com.timecat.module.plugin.manager

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.timecat.identity.data.getStringList
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

data class PartPlugin(
    val partKey: String,
    val activityList: List<String> = listOf(),
    val serviceList: List<String> = listOf(),
) : Serializable {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): PartPlugin {
            val partKey: String = jsonObject.getString("partKey")
            val activityList = jsonObject.getStringList("activityList")
            val serviceList = jsonObject.getStringList("serviceList")
            return PartPlugin(partKey, activityList, serviceList)
        }
    }

    fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["partKey"] = partKey
        jsonObject["activityList"] = activityList
        jsonObject["serviceList"] = serviceList
        return jsonObject
    }

}

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
    val parts: List<PartPlugin> = listOf(),
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
            val parts = jsonObject.getPartPluginList("parts")
            return PluginInfo(
                uuid, type, name,
                versionCode, versionName,
                managerUrl, managerFilename,
                pluginZipUrl, pluginZipFilename,
                parts,
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
        jsonObject["parts"] = parts.map { it.toJsonObject() }
        return jsonObject
    }

    fun getPluginManagerFile(context: Context): File = File(Plugin.fileInPluginDir(context, managerFilename))
    fun getPluginZipFile(context: Context): File = File(Plugin.fileInPluginDir(context, pluginZipFilename))
}

fun JSONObject.getPartPluginList(key: String): MutableList<PartPlugin> {
    return getJSONArray(key)?.toPartPluginList() ?: mutableListOf()
}

fun JSONArray.toPartPluginList(): MutableList<PartPlugin> {
    val list: MutableList<PartPlugin> = mutableListOf()
    for (i in this) {
        list.add(PartPlugin.fromJson(i as JSONObject))
    }
    return list
}
