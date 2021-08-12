//package com.timecat.module.user.adapter.virtual
//
//import android.content.Context
//import android.text.InputType
//import android.text.TextWatcher
//import android.view.KeyEvent
//import android.view.View
//import android.view.inputmethod.EditorInfo
//import android.widget.HorizontalScrollView
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.material.chip.Chip
//import com.timecat.component.commonsdk.extension.beGone
//import com.timecat.component.commonsdk.extension.beGoneIf
//import com.timecat.component.commonsdk.extension.beInvisible
//import com.timecat.component.commonsdk.extension.beVisible
//import com.timecat.component.commonsdk.utils.ShareUtils
//import com.timecat.component.commonsdk.utils.override.LogUtil
//import com.timecat.component.identity.Attr
//import com.timecat.component.router.app.NAV
//import com.timecat.data.bmob.data.User
//import com.timecat.data.room.record.RoomRecord
//import com.timecat.element.alert.ToastUtil
//import com.timecat.identity.data.base.TEXT
//import com.timecat.identity.data.block.type.BLOCK_MARKDOWN
//import com.timecat.identity.service.ImageSelectService
//import com.timecat.identity.service.ImageUploadService
//import com.timecat.layout.ui.entity.BaseItem
//import com.timecat.layout.ui.layout.setShakelessClickListener
//import com.timecat.layout.ui.utils.IconLoader
//import com.timecat.middle.block.ext.selectImg
//import com.timecat.middle.block.view.FlowableTextWatcher
//import com.timecat.middle.block.adapter.vh.BaseRecordCardVH
//import com.timecat.module.user.R
//import eu.davidea.flexibleadapter.FlexibleAdapter
//import eu.davidea.flexibleadapter.items.IExpandable
//import eu.davidea.flexibleadapter.items.IFlexible
//import io.reactivex.BackpressureStrategy
//import io.reactivex.Flowable
//import io.reactivex.FlowableOnSubscribe
//import io.reactivex.android.schedulers.AndroidSchedulers
//import io.reactivex.disposables.Disposable
//import io.reactivex.schedulers.Schedulers
//import java.util.*
//
///**
// * @author dlink
// * @email linxy59@mail2.sysu.edu.cn
// * @date 2018/8/17
// * @description 容器的头部
// * @usage null
// */
//open class UserHeaderCard(
//    val container: User,
//    val activity: Context,
//    var listener: Listener
//) : BaseItem<UserHeaderCard.RecordCardVH>("HEADER"), IExpandable<UserHeaderCard.RecordCardVH, BaseItem<*>> {
//
//    val uploader: ImageUploadService? by lazy { NAV.service(ImageUploadService::class.java) }
//    val selector: ImageSelectService? by lazy { NAV.service(ImageSelectService::class.java) }
//
//    interface Listener {
//        fun update(record: User)
//        fun insertToNext(record: User)
//        fun showImage(url: String, record: User)
//    }
//
//    override fun createViewHolder(
//        view: View,
//        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
//    ): RecordCardVH {
//        return RecordCardVH(view, adapter)
//    }
//
//    override fun getLayoutRes(): Int = R.layout.card_header_card_idea
//
//    override fun bindViewHolder(
//        adapter: FlexibleAdapter<IFlexible<*>>,
//        holder: RecordCardVH,
//        position: Int,
//        payloads: List<Any>
//    ) {
////        if (adapter is BaseAdapter) {
////            adapter.bindViewHolderAnimation(holder)
////        }
//        holder.bindRecord(container, this, listener, adapter)
//        holder.property.setShakelessClickListener {
//            if (isExpanded) {
//                adapter.collapse(adapter.getGlobalPositionOf(this))
//                holder.property.setChipIconResource(R.drawable.ic_arrow_drop_down)
//            } else {
//                adapter.expand(this)
//                holder.property.setChipIconResource(R.drawable.ic_arrow_drop_up)
//            }
//        }
//    }
//
//    //region IExpandable
//    private var mSubItems: MutableList<BaseItem<*>> = mutableListOf()
//    override fun getExpansionLevel(): Int = 0
//
//    override fun isExpanded(): Boolean = container.expand
//    override fun setExpanded(expanded: Boolean) {
//        container.expand = expanded
//        listener.update(container)
//    }
//
//    override fun getSubItems(): MutableList<BaseItem<*>> = mSubItems
//
//    fun hasSubItems(): Boolean {
//        return mSubItems.size > 0
//    }
//
//    fun removeSubItem(item: BaseItem<*>): Boolean {
//        return mSubItems.remove(item)
//    }
//
//    fun removeSubItem(position: Int): Boolean {
//        if (position >= 0 && position < mSubItems.size) {
//            mSubItems.removeAt(position)
//            return true
//        }
//        return false
//    }
//
//    fun addSubItem(subItem: BaseItem<*>) {
//        mSubItems.add(subItem)
//    }
//
//    fun addSubItem(position: Int, subItem: BaseItem<*>) {
//        if (position >= 0 && position < mSubItems.size) {
//            mSubItems.add(position, subItem)
//        } else addSubItem(subItem)
//    }
//    //endregion
//
//    inner class RecordCardVH(
//        v: View,
//        adapter: FlexibleAdapter<*>
//    ) : BaseRecordCardVH(v, adapter) {
//        val front_view: ConstraintLayout by lazy { v.findViewById<ConstraintLayout>(R.id.front_view) }
//        val cover: ImageView by lazy { v.findViewById<ImageView>(R.id.cover) }
//        val icon: ImageView by lazy { v.findViewById<ImageView>(R.id.icon) }
//        val chips: HorizontalScrollView by lazy { v.findViewById<HorizontalScrollView>(R.id.chips) }
//        val add_icon: Chip by lazy { v.findViewById<Chip>(R.id.add_icon) }
//        val add_cover: Chip by lazy { v.findViewById<Chip>(R.id.add_cover) }
//        val property: Chip by lazy { v.findViewById<Chip>(R.id.property) }
//        val add_comment: Chip by lazy { v.findViewById<Chip>(R.id.add_comment) }
//        val share: Chip by lazy { v.findViewById<Chip>(R.id.share) }
//        val more: Chip by lazy { v.findViewById<Chip>(R.id.more) }
//        val title: MarkdownInput by lazy { v.findViewById<MarkdownInput>(R.id.title) }
//
//        var tw: TextWatcher? = null
//        var requestSave: () -> Unit = { }
//        var createOrChangeToNext: () -> Boolean = { false }
//        var onFocus: () -> Unit = {}
//        var disposable: Disposable? = null
//
//        fun bindRecord(
//            record: User,
//            item: BaseItem<*>,
//            listener: Listener,
//            adapter: FlexibleAdapter<IFlexible<*>>,
//        ) {
//            bindTitle(adapter, record)
//            loadIcon(record)
//            setRecordIcon(record, listener) {
//                selectImg(activity, uploader, selector, true, it)
//            }
//            loadCover(record)
//            setRecordCover(record, listener) {
//                selectImg(activity, uploader, selector, false, it)
//            }
//            bindChip(record, listener)
//            requestSave = {
//                val text = title.text.toString()
//                record.title = text
//                record.content = text
//                listener.update(record)
//                LogUtil.sd("save: ${record}")
//            }
//            createOrChangeToNext = {
//                title.clearFocus()
//                val newTitle = title.text?.substring(0, title.selectionStart) ?: ""
//                val newContent = title.text?.substring(title.selectionEnd) ?: ""
//
//                record.title = newTitle
////            listener.update(record)
//                title.setText(newTitle) //textChangeListener会保存
//
//                val newRecord = RoomRecord.forName(newContent)
//                newRecord.id = 0
//                newRecord.type = BLOCK_MARKDOWN
//                newRecord.subType = TEXT
//                newRecord.parent = record.uuid
//                listener.insertToNext(newRecord)
//                true
//            }
//            bindEditorTextChangeWatcher()
//        }
//
//        fun bindTitle(adapter: FlexibleAdapter<IFlexible<*>>, record: User) {
//            title.setText(record.nickName)
//        }
//
//        fun bindChip(record: User, listener: Listener) {
//            add_icon.setShakelessClickListener {
//                record.icon = IconLoader.randomAvatar()
//                listener.update(record)
//                loadIcon(record)
//            }
//            add_cover.setShakelessClickListener {
//                record.coverImageUrl = IconLoader.randomCover()
//                listener.update(record)
//                loadCover(record)
//            }
//            add_comment.beGoneIf(record.commentHostId.isEmpty())
//            add_comment.setShakelessClickListener {
//                //云端提供服务，显示评论列表 BottomSheet
//                ToastUtil.w_long("TODO")
//            }
//            share.setShakelessClickListener {
//                ShareUtils.share(activity, record.content)
//            }
//            property.beGone()
//            more.setShakelessClickListener {
//                showFloatMenu(more) {
//                    listOf(
//                        //TODO 导出当前页，导入，锁定，收藏本页，复用本页，删除本页，移动到，样式（字体、子大小），段落（section，控制段落块的显示和隐藏，如property、comment、share）
//                        FloatMenuItem("移除图标", { record.icon.isNotEmpty() }, menuItemConfig = {
//                            setOnMenuItemClickListener {
//                                record.icon = ""
//                                listener.update(record)
//                                loadIcon(record)
//                                true
//                            }
//                        }),
//                        FloatMenuItem("移除头图", { !record.coverImageUrl.isNullOrEmpty() }, menuItemConfig = {
//                            setOnMenuItemClickListener {
//                                record.coverImageUrl = ""
//                                listener.update(record)
//                                loadCover(record)
//                                true
//                            }
//                        }),
//                    )
//                }
//            }
//        }
//
//        fun setRecordCover(
//            record: User,
//            listener: Listener,
//            onClick: ((String) -> Unit) -> Unit = {}
//        ) {
//            cover.setShakelessClickListener {
//                onClick {
//                    record.coverImageUrl = it
//                    listener.update(record)
//                    loadCover(record)
//                }
//            }
//            cover.setOnLongClickListener {
//                val url = record.coverImageUrl ?: return@setOnLongClickListener false
//                listener.showImage(url, record)
//                true
//            }
//        }
//
//        fun loadCover(record: User) {
//            if (record.coverImageUrl.isNullOrEmpty()) {
//                if (record.icon.isNotEmpty()) {
//                    cover.beInvisible()
//                } else {
//                    cover.beGone()
//                }
//                add_cover.beVisible()
//            } else {
//                cover.beVisible()
//                add_cover.beGone()
//                IconLoader.loadIcon(activity, cover, record.coverImageUrl)
//            }
//        }
//
//        fun setRecordIcon(
//            record: User,
//            listener: Listener,
//            onClick: ((String) -> Unit) -> Unit = {}
//        ) {
//            icon.setShakelessClickListener {
//                onClick {
//                    record.icon = it
//                    listener.update(record)
//                    loadIcon(record)
//                }
//            }
//            icon.setOnLongClickListener {
//                val url = record.icon
//                listener.showImage(url, record)
//                true
//            }
//        }
//
//        fun loadIcon(record: User) {
//            if (record.icon.isEmpty()) {
//                icon.beGone()
//                add_icon.beVisible()
//            } else {
//                icon.beVisible()
//                add_icon.beGone()
//                IconLoader.loadIcon(activity, icon, record.icon)
//            }
//        }
//
//        fun bindEditorTextChangeWatcher() {
//            disposable?.dispose()
//            disposable = Flowable.create(FlowableOnSubscribe<String> { emitter ->
//                title.removeTextChangedListener(tw)
//                tw = FlowableTextWatcher(emitter)
//                title.addTextChangedListener(tw)
//            }, BackpressureStrategy.LATEST)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe { requestSave() }
//        }
//
//        fun getEditorActionListener(
//            addAndFocusNext: () -> Boolean,
//            ignoreConditions: () -> Boolean = { false }
//        ): TextView.OnEditorActionListener {
//            return TextView.OnEditorActionListener { _: TextView, actionId: Int, event: KeyEvent? ->
//                LogUtil.se("${actionId}, event=${event}, condition=${ignoreConditions()}")
//                if (ignoreConditions()) {
//                    return@OnEditorActionListener false
//                }
//
//                if (event == null) {
//                    if (actionId != EditorInfo.IME_ACTION_DONE && actionId != EditorInfo.IME_ACTION_NEXT) {
//                        return@OnEditorActionListener false
//                    } else {
//                        return@OnEditorActionListener addAndFocusNext()
//                    }
//                } else {
//                    when (actionId) {
//                        EditorInfo.IME_NULL, KeyEvent.KEYCODE_ENTER -> {
//                            if (event.action != KeyEvent.ACTION_DOWN) {
//                                return@OnEditorActionListener true
//                            }
//                        }
//                        else -> {
//                            return@OnEditorActionListener false
//                        }
//                    }
//                }
//                LogUtil.se("runnable")
//                return@OnEditorActionListener addAndFocusNext()
//            }
//        }
//
//        /**
//         * Allows to expand or collapse child views of this itemView when [View.OnClickListener]
//         * event occurs on the entire view.
//         *
//         * This method returns always true; Extend with "return false" to Not expand or collapse
//         * this ItemView onClick events.
//         *
//         * @return always true, if not overridden
//         * @since 5.0.0-b1
//         */
//        override fun isViewExpandableOnClick(): Boolean {
//            return true //default=true
//        }
//
//        /**
//         * Allows to collapse child views of this ItemView when [View.OnClickListener]
//         * event occurs on the entire view.
//         *
//         * This method returns always true; Extend with "return false" to Not collapse this
//         * ItemView onClick events.
//         *
//         * @return always true, if not overridden
//         * @since 5.0.4
//         */
//        override fun isViewCollapsibleOnClick(): Boolean {
//            return true //default=true
//        }
//
//        /**
//         * Allows to collapse child views of this ItemView when [View.OnLongClickListener]
//         * event occurs on the entire view.
//         *
//         * This method returns always true; Extend with "return false" to Not collapse this
//         * ItemView onLongClick events.
//         *
//         * @return always true, if not overridden
//         * @since 5.0.0-b1
//         */
//        override fun isViewCollapsibleOnLongClick(): Boolean {
//            return true //default=true
//        }
//
//        /**
//         * Allows to notify change and rebound this itemView on expanding and collapsing events,
//         * in order to update the content (so, user can decide to display the current expanding status).
//         *
//         * This method returns always false; Override with `"return true"` to trigger the
//         * notification.
//         *
//         * @return true to rebound the content of this itemView on expanding and collapsing events,
//         * false to ignore the events
//         * @see .expandView
//         * @see .collapseView
//         * @since 5.0.0-rc1
//         */
//        override fun shouldNotifyParentOnClick(): Boolean {
//            return true //default=false
//        }
//
//        /**
//         * Expands or Collapses based on the current state.
//         *
//         * @see .shouldNotifyParentOnClick
//         * @see .expandView
//         * @see .collapseView
//         * @since 5.0.0-b1
//         */
//        override fun toggleExpansion() {
//            super.toggleExpansion() //If overridden, you must call the super method
//        }
//
//        /**
//         * Triggers expansion of this itemView.
//         *
//         * If [.shouldNotifyParentOnClick] returns `true`, this view is rebound
//         * with payload [Payload.EXPANDED].
//         *
//         * @see .shouldNotifyParentOnClick
//         * @since 5.0.0-b1
//         */
//        override fun expandView(position: Int) {
//            super.expandView(position) //If overridden, you must call the super method
//            // Let's notify the item has been expanded. Note: from 5.0.0-rc1 the next line becomes
//            // obsolete, override the new method shouldNotifyParentOnClick() as showcased here
//            //if (mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
//        }
//
//        /**
//         * Triggers collapse of this itemView.
//         *
//         * If [.shouldNotifyParentOnClick] returns `true`, this view is rebound
//         * with payload [Payload.COLLAPSED].
//         *
//         * @see .shouldNotifyParentOnClick
//         * @since 5.0.0-b1
//         */
//        override fun collapseView(position: Int) {
//            super.collapseView(position) //If overridden, you must call the super method
//            // Let's notify the item has been collapsed. Note: from 5.0.0-rc1 the next line becomes
//            // obsolete, override the new method shouldNotifyParentOnClick() as showcased here
//            //if (!mAdapter.isExpanded(position)) mAdapter.notifyItemChanged(position, true);
//        }
//
//        init {
//            title.setTextColor(Attr.getPrimaryTextColor(v.context))
//            title.setOnFocusChangeListener { _, hasFocus ->
//                frontView.isSelected = hasFocus
//                if (hasFocus) {
//                    onFocus()
//                }
//            }
//            title.setOnEditorActionListener(getEditorActionListener(
//                addAndFocusNext = { createOrChangeToNext() },
//                ignoreConditions = { !title.isFocused }
//            ))
//            title.imeOptions = EditorInfo.IME_ACTION_DONE
//            title.setRawInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE)
//        }
//
//    }
//
//    init {
//        isDraggable = false
//        isSelectable = false
//        isSwipeable = false
//    }
//}