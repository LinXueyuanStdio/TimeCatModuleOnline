package com.timecat.module.user.app.online

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.ext.bmob.requestOneBlockOrNull
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.middle.block.service.IDatabase
import com.timecat.middle.block.service.LoadDbCallback
import com.timecat.middle.block.service.OnlineBackendService
import com.timecat.module.user.record.EmptyDatabase
import com.timecat.module.user.record.OnlineBackendDb
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/13
 * @description null
 * @usage null
 */
@ServiceAnno(OnlineBackendService::class)
class OnlineBackendServiceImpl : OnlineBackendService {
    override fun buildBackend(context: Context, url: String, callback: LoadDbCallback) {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            callback.onFail("未登录") {

            }
            return
        }
        TimeCatOnline.parsePath(url, onSpace = { uuid ->
            requestOneBlockOrNull {
                query = oneBlockOf(uuid)
                onSuccess = {
                    val space = it
                    val db = OnlineBackendDb(context, I, space)
                    callback.onSuccess(db)
                }
                onError = {
                    callback.onFail("在线数据库不存在") {

                    }
                }
                onEmpty = {
                    callback.onFail("在线数据库不存在") {

                    }
                }
            }
        }, onBlock = { uuid ->
            requestOneBlockOrNull {
                query = oneBlockOf(uuid)
                onSuccess = {
                    val space = it.space
                    if (space == null) {
                        callback.onFail("在线数据库不存在") {

                        }
                    } else {
                        val db = OnlineBackendDb(context, I, space)
                        callback.onSuccess(db)
                    }
                }
                onError = {
                    callback.onFail("在线数据库不存在") {

                    }
                }
                onEmpty = {
                    callback.onFail("在线数据库不存在") {

                    }
                }
            }
        }, onFail = {
            callback.onFail("在线数据库不存在") {

            }
        })
    }
}