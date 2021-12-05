package com.timecat.module.user.app

import android.annotation.SuppressLint
import android.content.Context
import cn.leancloud.AVACL
import com.afollestad.materialdialogs.input.input
import com.alibaba.fastjson.JSONObject
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.base.Json
import com.timecat.identity.data.block.type.BLOCK_DATABASE
import com.timecat.identity.data.block.type.BLOCK_SPACE
import com.timecat.middle.block.ext.showDialog
import com.timecat.middle.block.service.CreateDatabaseSchemaService
import com.timecat.middle.block.service.CreateOnlineDbService
import com.timecat.module.user.app.online.TimeCatOnline
import com.timecat.module.user.ext.ext
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/14
 * @description null
 * @usage null
 */
@ServiceAnno(CreateOnlineDbService::class)
class CreateOnlineDbServiceImpl : CreateOnlineDbService {
    override fun showCreateOnlineDbDialog(context: Context, onSelect: (String) -> Unit) {
        showCreateOnlineDbDialog(context) { block: Block? ->
            if (block == null) {
                onSelect("")
            } else {
                TimeCatOnline.block2Url(block)
            }
        }
    }

    override fun showImportOnlineDbDialog(context: Context, onSelect: (String) -> Unit) {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            onSelect("")
            return
        }
        context.showDialog {
            title(text = "未实现")
        }
    }
}

@SuppressLint("CheckResult")
fun showCreateOnlineDbDialog(context: Context, onSelect: (Block?) -> Unit) {
    val I = UserDao.getCurrentUser()
    if (I == null) {
        onSelect(null)
        return
    }
    context.showDialog {
        title(text = "创建在线数据库")
        input(hint = "在线数据库名称（名称长度短于 100 个字符）", maxLength = 100) { d, s ->
            val spaceName = s.toString()
            saveBlock {
                target = Block.forName(I, BLOCK_DATABASE, spaceName).apply {
                    subtype = 0
                    val schemaService = NAV.service(CreateDatabaseSchemaService::class.java)
                    schemaService?.simpleDatabaseSchema(context)?.let {
                        val jo = JSONObject()
                        jo.put("schema", it)
                        ext = Json(jo)
                    }
                }
                onSuccess = {
                    onSelect(it)
                }
                onError = {
                    onSelect(null)
                    ToastUtil.e_long(it.localizedMessage)
                }
            }
        }
    }
}

@SuppressLint("CheckResult")
fun showCreateOnlineSpaceDialog(context: Context, onSelect: (Block?) -> Unit) {
    val I = UserDao.getCurrentUser()
    if (I == null) {
        onSelect(null)
        return
    }
    context.showDialog {
        title(text = "创建在线超空间")
        input(hint = "在线超空间名称（名称长度短于 100 个字符）", maxLength = 100) { d, s ->
            val spaceName = s.toString()
            saveBlock {
                target = Block.forName(I, BLOCK_SPACE, spaceName).apply {
                    val ownerAcl = AVACL()
                    ownerAcl.publicReadAccess = false
                    ownerAcl.publicWriteAccess = false
                    ownerAcl.setReadAccess(I, true)
                    ownerAcl.setWriteAccess(I, true)
                    acl = ownerAcl
                }
                onSuccess = {
                    onSelect(it)
                }
                onError = {
                    onSelect(null)
                    ToastUtil.e_long(it.localizedMessage)
                }
            }
        }
    }
}