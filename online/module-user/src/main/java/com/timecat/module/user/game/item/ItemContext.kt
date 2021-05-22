package com.timecat.module.user.game.item

import androidx.lifecycle.LifecycleOwner
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.User
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.game.OwnItem
import com.timecat.data.bmob.data.game.OwnTask
import com.timecat.data.bmob.ext.net.allItem
import com.timecat.data.bmob.ext.net.allOwnItem
import com.timecat.data.bmob.ext.net.allOwnTask
import com.timecat.data.bmob.ext.net.allTask
import com.timecat.data.bmob.ext.toDataError
import com.timecat.module.user.game.task.rule.ActivityContext
import com.timecat.module.user.game.task.rule.LoadableContext
import com.timecat.module.user.game.task.rule.bindLoadableContext
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/22
 * @description null
 * @usage null
 */
class ItemContext(
    private val owner: LifecycleOwner,
    val user: User,
    private val onLoading: () -> Unit,
    private val onLoaded: (ItemContext) -> Unit
) : LoadableContext() {
    val items: MutableList<Block> = mutableListOf()
    val itemsMap: MutableMap<String, Block> = mutableMapOf()
    val ownItems: MutableList<OwnItem> = mutableListOf()
    val ownItemsMap: MutableMap<String, Int> = mutableMapOf()

    fun load() {
        onLoading()
        owner.bindLoadableContext(this)
        loadOwnItem(user)
    }


    data class ItemsAndOwnItems(
        val items: List<Block>? = null,
        val ownItems: List<OwnItem>? = null,
    )
    private fun loadOwnItem(user: User) {
        LogUtil.sd(user)
        val getAllItems = allItem().findInBackground()
        val getAllOwnItems = user.allOwnItem().findInBackground()
        this attach Observable.zip(getAllItems, getAllOwnItems) { tasks, ownTasks ->
            ItemsAndOwnItems(tasks, ownTasks)
        }.map {
            if (it.items.isNullOrEmpty()) {
                items.clear()
            } else {
                items.clear()
                items.addAll(it.items)
                progressItems(it.items)
            }
            if (it.ownItems.isNullOrEmpty()) {
                ownItems.clear()
            } else {
                ownItems.clear()
                ownItems.addAll(it.ownItems)
                progressOwnItems(it.ownItems)
            }
            true
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onLoaded(this) }, { LogUtil.e(it.toDataError()) })
    }

    private fun progressOwnItems(ownItems: List<OwnItem>) {
        for (i in ownItems) {
            ownItemsMap[i.item.uuid] = i.count
        }
    }

    private fun progressItems(items: List<Block>) {
        for (i in items) {
            itemsMap[i.uuid] = i
        }
    }
}