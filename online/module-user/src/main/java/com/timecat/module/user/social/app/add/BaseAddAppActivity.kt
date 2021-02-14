package com.timecat.module.user.social.app.add

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.App
import com.timecat.data.bmob.ext.Item
import com.timecat.data.bmob.ext.bmob.requestExistBlock
import com.timecat.data.bmob.ext.create
import com.timecat.data.bmob.ext.net.checkAppExistByTitle
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.AppBlock
import com.timecat.identity.data.block.ItemBlock
import com.timecat.module.user.base.BaseBlockEditorActivity
import com.timecat.module.user.base.BaseComplexEditorActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/6/11
 * @description 开发者上传 app
 * @usage null
 */
abstract class BaseAddAppActivity : BaseBlockEditorActivity() {
    abstract fun getItemBlock(): AppBlock

    override fun savableBlock(): Block = I() create App {
        title = formData.title
        content = formData.content
        subtype = subtype()
        headerBlock = getItemBlock()
    }

    override fun updatableBlock(): Block.() -> Unit = {
        title = formData.title
        content = formData.content
        subtype = subtype()
        structure = getItemBlock().toJson()
    }

    override fun ok() {
        if (currentBlock() == null) {
            GlobalScope.launch(Dispatchers.IO) {
                requestExistBlock {
                    query = checkAppExistByTitle(formData.title)
                    onError = errorCallback
                    onSuccess = { exist ->
                        if (exist) {
                            ToastUtil.w("已存在，请修改名称！")
                        } else {
                            save()
                        }
                    }
                }
            }
        } else {
            update()
        }
    }
}