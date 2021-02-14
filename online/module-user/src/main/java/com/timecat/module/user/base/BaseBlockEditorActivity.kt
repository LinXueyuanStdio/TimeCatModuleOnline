package com.timecat.module.user.base

import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.saveBlock
import com.timecat.data.bmob.ext.bmob.updateBlock
import com.timecat.element.alert.ToastUtil

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 重量级编辑器
 * @usage 简化添加 社区 block 流程
 */
abstract class BaseBlockEditorActivity : BaseComplexEditorActivity() {

    override fun release() {
        ok()
    }

    protected fun ok() {
        if (currentBlock() == null) {
            save()
        } else {
            update()
        }
    }

    abstract fun currentBlock(): Block?
    abstract fun subtype(): Int
    abstract fun savableBlock(): Block
    abstract fun updatableBlock(): Block.() -> Unit

    open fun onUpdateSuccess(it: Block) {
        ToastUtil.ok("更新成功！")
        finish()
    }

    open fun update() {
        currentBlock()?.let {
            updateBlock {
                target = it.apply(updatableBlock())
                onSuccess = {
                    onUpdateSuccess(it)
                }
                onError = errorCallback
            }
        }
    }

    open fun onSaveSuccess(it: Block) {
        ToastUtil.ok("成功！")
        finish()
    }

    open fun save() {
        saveBlock {
            target = savableBlock()
            onSuccess = {
                onSaveSuccess(it)
            }
            onError = errorCallback
        }
    }
}