package com.timecat.module.user.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.tabs.TabLayout
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.standard.flowlayout.FlowLayout
import com.timecat.layout.ui.standard.flowlayout.TagAdapter
import com.timecat.layout.ui.standard.flowlayout.TagFlowLayout
import com.timecat.module.user.R
import com.timecat.module.user.search.db.RecordsDao
import com.timecat.module.user.search.vm.SearchViewModel
import com.timecat.module.user.search.vm.SearchViewModelFactory
import com.timecat.page.base.base.lazyload.adapter.FragmentLazyPagerAdapter
import com.timecat.page.base.friend.toolbar.BaseStatusBarActivity
import com.xiaojinzi.component.anno.RouterAnno
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

@RouterAnno(hostAndPath = RouterHub.SEARCH_SearchActivity)
open class SearchActivity : BaseStatusBarActivity() {

    //header
    private lateinit var back: ImageView
    private lateinit var et_search: EditText
    private lateinit var clear: ImageView
    private lateinit var search: ImageView

    //container
    private lateinit var vf: ViewFlipper

    //history
    private lateinit var tv_tip: TextView
    private lateinit var tagFlowLayout: TagFlowLayout
    private lateinit var tv_clear: TextView
    private lateinit var moreArrow: TextView

    //search detail
    private lateinit var vp: ViewPager
    private lateinit var tabs: TabLayout

    private lateinit var searchViewModel: SearchViewModel

    private val editSearch: String
        get() = et_search.text?.toString() ?: ""

    override fun layout(): Int = R.layout.activity_search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSearchHeader()
        setupSearchHistory()
        setupSearchDetail()
        setupSearchViewModel()
    }

    override fun bindView() {
        super.bindView()
        back = findViewById(R.id.back)
        et_search = findViewById(R.id.et_search)
        clear = findViewById(R.id.clear)
        search = findViewById(R.id.search)

        vf = findViewById(R.id.vf)

        tv_tip = findViewById(R.id.tv_tip)
        tv_clear = findViewById(R.id.tv_clear)
        tagFlowLayout = findViewById(R.id.histories)
        moreArrow = findViewById(R.id.iv_arrow)

        vp = findViewById(R.id.vp)
        tabs = findViewById(R.id.tabs)
    }

    var lastTw: TextWatcher? = null

    class FlowableTextWatcher(private val emitter: FlowableEmitter<String>) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            emitter.onNext(s.toString())
        }
    }

    val dispose = Flowable.create<String>({ emitter ->
        lastTw = FlowableTextWatcher(emitter)
    }, BackpressureStrategy.LATEST)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { s ->
            if (s.isNotEmpty()) {
                goToQuery(s)
                clear.visibility = View.VISIBLE
            } else {
                clear.visibility = View.GONE
            }
        }

    private fun setupSearchHeader() {
        // 搜索框的键盘搜索键点击回调
        et_search.setOnKeyListener { v, keyCode, event ->
            // 输入完后按键盘上的搜索键，修改回车键功能
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                // 1. 先隐藏键盘
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.hideSoftInputFromWindow(
                    currentFocus?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                // 2. 将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                val hasData = mRecordsDao.isHasRecord(editSearch)
                if (!hasData && !TextUtils.isEmpty(editSearch)) {
                    mRecordsDao.addRecords(editSearch)
                }

                // 3. 执行查询
                goToQuery(editSearch)
            }
            false
        }
        et_search.addTextChangedListener(lastTw)

        clear.setShakelessClickListener {
            et_search.setText("")
        }

        back.setShakelessClickListener {
            finish()
        }

        search.setShakelessClickListener {
            goToQuery(editSearch)
        }
    }

    //默然展示词条个数
    private val DEFAULT_RECORD_NUMBER = 10
    private var recordList: MutableList<String> = ArrayList()
    private lateinit var mRecordsAdapter: TagAdapter<String>
    private lateinit var mRecordsDao: RecordsDao
    private fun setupSearchHistory() {
        val user = UserDao.getCurrentUser()
        //初始化数据库
        mRecordsDao = RecordsDao(this, user?.objectId ?: "")
        //创建历史标签适配器
        //为标签设置对应的内容
        mRecordsAdapter = object : TagAdapter<String>(recordList) {
            override fun getView(parent: FlowLayout, position: Int, s: String): View {
                val tv = LayoutInflater.from(this@SearchActivity).inflate(
                    R.layout.search_item_history,
                    tagFlowLayout, false
                ) as TextView
                //为标签设置对应的内容
                tv.text = s
                return tv
            }
        }

        tagFlowLayout.adapter = mRecordsAdapter
        tagFlowLayout.setOnTagClickListener { view, position, parent -> //清空editText之前的数据
            //将获取到的字符串传到搜索结果界面,点击后搜索对应条目内容
            et_search.setText(recordList[position])
            et_search.setSelection(et_search.length())
            goToQuery(editSearch)
        }
        //删除某个条目
        tagFlowLayout.setOnLongClickListener { v, position ->
            //删除某一条记录
            showDialog("确定要删除该条历史记录？") {
                mRecordsDao.deleteRecord(recordList[position])
            }
        }

        moreArrow.setShakelessClickListener {
            mRecordsAdapter.notifyDataChanged()
        }
        mRecordsDao.setNotifyDataChanged { initHistoryData() }

        // 清空搜索历史
        tv_clear.setShakelessClickListener {
            //清除所有数据
            showDialog("确定要删除全部历史记录？") {
                mRecordsDao.deleteUsernameAllRecords()
            }
        }
        initHistoryData()
    }

    open fun getFragmentAdapter(): FragmentPagerAdapter {
        val titles = ArrayList<String>()
        val fragments = ArrayList<Fragment>()
        for (i in SearchType.values()) {
            titles.add(i.title)
            fragments.add(NAV.fragment((i.path)))
        }
        val adapter = FragmentLazyPagerAdapter(supportFragmentManager, fragments, titles)
        return adapter
    }

    private fun setupSearchDetail() {
        vp.adapter = getFragmentAdapter()
        tabs.setupWithViewPager(vp)
    }

    private fun setupSearchViewModel() {
        val factory = SearchViewModelFactory.getInstance()
        searchViewModel = ViewModelProvider(this, factory)
            .get(SearchViewModel::class.java)
    }

    enum class SearchType(val title: String, val path: String) {
        USER("用户", RouterHub.SEARCH_SearchUserFragment),
        MOMENT("动态", RouterHub.SEARCH_SearchUserFragment)
    }

    override fun onBackPressed() {
        if (vf.displayedChild != 0) {
            goToHistory()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * 搜索历史页
     */
    private fun goToHistory() {
        vf.displayedChild = 0
    }

    /**
     * 查询页
     */
    private fun goToQuery(q: String) {
        vf.displayedChild = 1

        searchViewModel.searchText.postValue(q)
    }

    private fun showDialog(dialogTitle: String, onClick: () -> Unit) {
        MaterialDialog(this).show {
            title(text = dialogTitle)
            positiveButton(R.string.confirm) {
                onClick()
            }
            negativeButton(R.string.cancel)
        }
    }

    private fun initHistoryData() {
        Observable.create<List<String>> { emitter ->
            emitter.onNext(
                mRecordsDao.getRecordsByNumber(DEFAULT_RECORD_NUMBER)
            )
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { s ->
                recordList.clear()
                recordList.addAll(s)
                if (mRecordsAdapter != null) {
                    mRecordsAdapter.setData(recordList)
                    mRecordsAdapter.notifyDataChanged()
                }
            }
    }


    override fun onDestroy() {
        mRecordsDao.closeDatabase()
        mRecordsDao.removeNotifyDataChanged()
        super.onDestroy()
    }
}