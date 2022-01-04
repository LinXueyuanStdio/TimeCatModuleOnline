package com.timecat.module.user.app.block

import android.content.Context
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.same.lib.core.ActionBarMenu
import com.same.lib.core.BasePage
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.room.AppRoomDatabase
import com.timecat.data.room.record.RoomRecord
import com.timecat.identity.data.base.GOAL
import com.timecat.identity.data.base.HABIT
import com.timecat.identity.data.base.REMINDER
import com.timecat.identity.data.block.type.BLOCK_RECORD
import com.timecat.layout.ui.business.breadcrumb.Path
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.layout.ui.entity.BaseItem
import com.timecat.middle.block.adapter.TypeCard
import com.timecat.middle.block.adapter.TypeItem
import com.timecat.middle.block.aggreement.RecordManager
import com.timecat.middle.block.ext.launch
import com.timecat.middle.block.item.BaseRecordItem
import com.timecat.middle.block.page.BaseSearchDockPage
import com.timecat.middle.block.service.*
import com.timecat.middle.block.support.ChangeReminderService
import com.timecat.middle.block.support.HabitService
import com.timecat.middle.block.view.ThingAction
import com.timecat.module.user.R
import com.timecat.module.user.adapter.virtual.PermissionHeaderCard
import com.timecat.module.user.ext.getSpace
import com.timecat.module.user.ext.getUser
import com.timecat.module.user.ext.toBlock
import eu.davidea.flexibleadapter.items.IFlexible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/12/5
 * @description null
 * @usage TODO 根据权限展示内容
 */
class DockPage(
    val record: RoomRecord,
    val db: IDatabase,
    val parentCardFactory: CardFactory,
    val parentCardPermission: CardPermission,
) : BaseSearchDockPage() {
    override fun title(): String = "聚合"

    val block = record.toBlock(record.bag.getUser()!!, record.bag.getSpace(), false)
    var cardFactory: CardFactory = parentCardFactory
    var cardPermission: CardPermission = parentCardPermission
    private val habitService: HabitService? by lazy { NAV.service(HabitService::class.java) }
    private val timeService: ChangeReminderService? by lazy { NAV.service(ChangeReminderService::class.java) }

    private lateinit var commonListener: CommonListener

    //region menu
    override fun configMenu(context: Context, menu: ActionBarMenu) {
        super.configMenu(context, menu)
        val iconColor = Attr.getIconColor(context)
        menu.addItem(MENU_STATUS, R.drawable.ic_close).apply {
            setIconColor(iconColor)
        }
    }

    override fun onMenuItemClick(id: Int) {
        when (id) {
            -1 -> finishFragment(false)
            MENU_STATUS -> finishFragment(false)
        }
    }
    //endregion

    //region dock
    override suspend fun getDockData(context: Context): List<BaseItem<*>> {
        val dockList= mutableListOf<BaseItem<*>>()
        if (cardPermission.fullAccess()) {
            //header
            dockList.add(PermissionHeaderCard(context, block, commonListener))
        }
        // 推荐
        val recommend = TypeItem(0, "推荐完成", "推荐优先完成的任务、习惯、目标")
        val recommendCard = TypeCard(recommend)
        getDoing(context).forEach {
            recommendCard.addSubItem(it)
        }
        dockList.add(recommendCard)
        return dockList
    }

    override suspend fun getDockMoreData(context: Context): List<BaseItem<*>> {
        return emptyList()
    }
    //endregion

    //region search
    override suspend fun getSearchResults(context: Context): List<BaseItem<*>> {
        val query = searchAction.query
        val result = db.searchAll(query, 0, true, 0, pageSize)
        val data = result.map { cardFactory.buildOneCard(it, context, commonListener) }
        return data
    }

    override suspend fun getMoreSearchResults(context: Context): List<BaseItem<*>> {
        val query = searchMoreAction.query
        val result = db.searchAll(query, 0, true, searchOffset, pageSize)
        val data = result.map { cardFactory.buildOneCard(it, context, commonListener) }
        return data
    }
    //endregion

    override fun loadData(context: Context) {
        commonListener = CommonListener(context)
        super.loadData(context)
    }

    private suspend fun getDoing(context: Context): List<BaseItem<*>> {
        val baseTasks: MutableList<BaseItem<*>> = ArrayList()
        val data = db.getAllByTypeAndSubtype(BLOCK_RECORD, listOf(REMINDER, HABIT, GOAL), 0, true, dockLoadOffset, pageSize)
        for (record in data) {
            if (record.isFinished()) continue
            if (record.isArchived()) continue
            val card = cardFactory.buildOneCard(record, context, commonListener)
            baseTasks.add(card)
        }
        return baseTasks
    }

    open inner class CommonListener(
        context: Context
    ) : BaseCommonListener(
        context,
        dockAdapter,
        dockRecycleView,
        db,
        habitService,
        timeService
    ) {

        override fun openPage(page: BasePage) {
            presentFragment(page)
        }

        override fun openPage(page: BasePage, removeLast: Boolean) {
            presentFragment(page, removeLast)
        }

        override fun openPage(page: BasePage, removeLast: Boolean, forceWithoutAnimation: Boolean) {
            presentFragment(page, removeLast, forceWithoutAnimation)
        }

        override fun showMore(record: RoomRecord) {
            (parentActivity as AppCompatActivity?)?.let {
                RecordManager.showDetail(record.uuid, it.supportFragmentManager)
            }
        }

        override fun close() {
        }

        override fun popupParentView(): View {
            return fragmentView
        }

        override fun getPermission(): CardPermission {
            return cardPermission
        }

        override fun setPermission(permission: CardPermission) {
            cardPermission = permission
        }

        override fun getCurrentCardFactory(): CardFactory {
            return cardFactory
        }

        override fun resetCardFactory() {
            cardFactory = parentCardFactory
        }

        override fun setCurrentCardFactory(factory: CardFactory) {
            cardFactory = factory
        }
    }

    abstract class BaseCommonListener(
        context: Context,
        val mAdapter: BaseAdapter,
        val mRecyclerView: RecyclerView,
        val currentDb: IDatabase,
        val habitService: HabitService? = null,
        val timeService: ChangeReminderService? = null,
    ) : BaseRecordListener(context) {

        override fun setTop(isTop: Boolean) {
            if (mRecyclerView.isComputingLayout) return
            mAdapter.reload()
        }

        override fun insert(position: Int, record: RoomRecord, listener: ItemCommonListener) {
        }

        override fun addNewItemToEndOfList(record: RoomRecord, listener: ItemCommonListener) {
        }

        override fun changeType(item: BaseRecordItem<*>, type: Int, subType: Int, listener: ItemCommonListener) {
            context.launch(Dispatchers.IO) {
                item.record.type = type
                item.record.subType = subType
                listener.primaryDb().updateRecord(item.record)
                val newItem = buildOneCard(item.record, context, listener)
                withContext(Dispatchers.Main) {
                    mAdapter.updateItem(mAdapter.getGlobalPositionOf(item), newItem, null)
                }
            }
        }

        override fun changeShowType(item: BaseRecordItem<*>, showType: Int, listener: ItemCommonListener) {
            context.launch(Dispatchers.IO) {
                item.record.miniShowType = showType
                listener.primaryDb().updateRecord(item.record)
                val newItem = buildOneCard(item.record, context, listener)
                withContext(Dispatchers.Main) {
                    mAdapter.updateItem(mAdapter.getGlobalPositionOf(item), newItem, null)
                }
            }
        }

        override fun primaryDb(): IDatabase {
            return currentDb
        }

        override fun habitService(): HabitService? {
            return habitService
        }

        override fun changeReminderService(): ChangeReminderService? {
            return timeService
        }

        override fun adapter(): BaseAdapter {
            return mAdapter
        }
    }

    abstract class BaseRecordListener(
        val context: Context,
    ) : ItemCommonListener {

        override fun showAudio(url: String, record: RoomRecord) {
        }

        override fun showImage(url: String, record: RoomRecord) {
        }

        override fun loadFor(record: RoomRecord) {
        }

        override fun onLongClick(selectPosition: Int) {
        }

        override fun openPage(page: BasePage) {
        }

        override fun openPage(page: BasePage, removeLast: Boolean) {
        }

        override fun openPage(page: BasePage, removeLast: Boolean, forceWithoutAnimation: Boolean) {
        }

        override fun playAudio(url: String, record: RoomRecord, callback: PlayAudioCallback) {
        }

        override fun isPlayingAudio(message: RoomRecord): Boolean {
            return false
        }

        override fun showUrl(url: String, record: RoomRecord) {
        }

        override fun showVideo(url: String, record: RoomRecord) {
        }

        override fun navigateTo(name: String, uuid: String, type: Int) {
        }

        override fun resetTo(path: Path) {
        }

        override fun close() {
        }

        override fun loadMore(lastPosition: Int, currentPage: Int) {
        }

        override fun focus(item: IFlexible<*>) {
        }

        override fun addAction(action: ThingAction) {

        }

        override fun insert(position: Int, record: RoomRecord, listener: ItemCommonListener) {
        }

        override fun addNewItemToEndOfList(record: RoomRecord, listener: ItemCommonListener) {
        }

        override fun changeType(item: BaseRecordItem<*>, type: Int, subType: Int, listener: ItemCommonListener) {
        }

        override fun changeShowType(item: BaseRecordItem<*>, showType: Int, listener: ItemCommonListener) {
        }

        override fun contextRecord(): RoomRecord? {
            return null
        }

        override fun appDatabase(): AppRoomDatabase {
            return AppRoomDatabase.forFile(context)
        }

        override fun secondaryDb(spaceId: String, callback: LoadDbCallback) {
            callback.onFail("无") {}
        }

    }

    companion object {
        const val MENU_STATUS = 1001
    }
}