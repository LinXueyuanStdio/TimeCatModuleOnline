package com.timecat.module.user.app

import android.content.Context
import com.timecat.data.bmob.dao.UserDao
import com.timecat.middle.block.ext.showDialog
import com.timecat.middle.block.service.CreateOnlineDbService
import com.xiaojinzi.component.anno.ServiceAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/8/14
 * @description null
 * @usage null
 */
@ServiceAnno(CreateOnlineDbService::class)
class CreateOnlineDbServiceImpl: CreateOnlineDbService {
    override fun showCreateOnlineDbDialog(context: Context, onSelect: (String) -> Unit) {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            onSelect("")
            return
        }
        context.showDialog {

        }
    }

    override fun showImportOnlineDbDialog(context: Context, onSelect: (String) -> Unit) {
        val I = UserDao.getCurrentUser()
        if (I == null) {
            onSelect("")
            return
        }
        context.showDialog {

        }
    }
}