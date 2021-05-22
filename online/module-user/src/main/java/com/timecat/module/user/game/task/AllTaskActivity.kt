package com.timecat.module.user.game.task

import androidx.lifecycle.ViewModelProvider
import com.alibaba.fastjson.JSONObject
import com.cheng.channel.Channel
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.NAV
import com.timecat.data.system.model.eventbus.TabReselectedEvent
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.standard.navi.BottomBar
import com.timecat.layout.ui.standard.navi.TabBlockItem
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginMainActivity
import com.timecat.module.user.game.task.channal.TaskChannel
import com.timecat.module.user.game.task.fragment.*
import com.timecat.module.user.game.task.rule.ActivityContext
import com.timecat.module.user.game.task.rule.GameService
import com.timecat.module.user.game.task.vm.TaskViewModel
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.anno.ServiceAutowiredAnno
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.yokeyword.eventbusactivityscope.EventBusActivityScope
import me.yokeyword.fragmentation.ISupportFragment
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 活动、任务
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_AllTaskActivity)
class AllTaskActivity : BaseLoginMainActivity() {
    lateinit var viewModel: TaskViewModel

    @ServiceAutowiredAnno
    lateinit var gameService: GameService

    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        gameService.activityContext(this, I(), {
            onPrepareContent()
        }) {
            initByActivityContext(it)
        }
    }

    fun initByActivityContext(activityContext: ActivityContext) {
        viewModel.channels.observe(this) {
            refreshChannel(it)
        }
        initBottomBar()
        viewModel.activities.postValue(activityContext.ownActivity)
    }

    //region bottom bar
    private fun initBottomBar() {
        val start = System.currentTimeMillis()
        LogUtil.sd("开始加载 view $start ms")
        mBottomBar.addHorizonSV()
        mBottomBar.setOnTabSelectedListener(object : BottomBar.OnTabSelectedListener {
            override fun onTabSelected(position: Int, prePosition: Int) {
                LogUtil.sd("select $position, $prePosition")
                //选中时触发
                destroyActionMode()
                val tab = mBottomBar.getItem(position)
                LogUtil.sd("$tab")
                LogUtil.sd("${tab?.item}")
                if (tab != null && tab.item != null) {
                    val item = tab.item
                    showByTabBlockItem(item)
                }
                viewModel.selectOwnActivityIndex.postValue(position)
            }

            override fun onTabUnselected(position: Int) {}
            override fun onTabLongSelected(position: Int) {
                destroyActionMode()
            }

            override fun onTabReselected(position: Int) {
                EventBusActivityScope.getDefault(this@AllTaskActivity).post(TabReselectedEvent(position))
            }
        })
        val end = System.currentTimeMillis()
        LogUtil.sd("结束加载 view $end ms")
        LogUtil.sd("总耗时 " + (end - start) + " ms")
    }

    private fun showByTabBlockItem(item: TabBlockItem) {
        val selected = fragmentByTabBlockItem(item) ?: return
        for (i in mFragments) {
            LogUtil.sd("${i.key} -> ${i.value}")
        }
        LogUtil.sd("* ${item.key} -> ${selected}")
        showHideFragment(selected)
    }

    private fun fragmentByTabBlockItem(item: TabBlockItem): ISupportFragment? {
        var selected = mFragments[item.key]
        report(item.title)
        if (selected == null) {
            LogUtil.sd("${item.fragmentRouterPath}")
            val a: Any = NAV.fragment(item.fragmentRouterPath)
            LogUtil.sd(a)
            if (a is ISupportFragment) {
                selected = a
                mFragments[item.key] = selected
            }
        }
        return selected
    }

    protected fun report(title: String) {}

    private fun destroyActionMode() {}

    /**
     * 刷新 除固定的tab以外的 自定义tab
     *
     * @param list
     */
    private fun refreshCustom(list: List<Channel>?) {
        if (list != null && list.size > 1) {
            mBottomBar.clearContainer()
            for (i in 1 until list.size) {
                val c = list[i]
                val obj = c.obj
                if (obj is TabBlockItem) {
                    addTabBlockItem(obj)
                } else if (obj is JSONObject) {
                    val item = obj.toJavaObject(TabBlockItem::class.java)
                    addTabBlockItem(item)
                }
            }
        }
    }

    private fun addTabBlockItem(item: TabBlockItem): Any? {
        mBottomBar.addItem(item.createTabView(this))
        return mFragments[item.key] ?: addToFragmentMap(item.key, item.fragmentRouterPath)
    }

    private fun addToFragmentMap(key: String, path: String): Any? {
        val f = NAV.fragment(path)
        if (f is ISupportFragment) {
            mFragments[key] = f as ISupportFragment
        }
        return f
    }

    private fun refreshStatic() {
        addToFragmentMap(TaskChannel.Home.getKey(), TaskChannel.Home.fragmentRouterPath)
    }

    private fun refreshChannel(list: List<Channel>) {
        GlobalScope.launch(Dispatchers.IO) {
            val keysBefore: Set<String> = HashSet(mFragments.keys)
            val primaryKey = TaskChannel.Home.getKey()
            if (!keysBefore.contains(primaryKey)) {
                refreshStatic()
            }
            withContext(Dispatchers.Main) {
                refreshCustom(list)
            }
            val keysAfter: MutableSet<String> = HashSet(mFragments.keys)
            keysAfter.removeAll(keysBefore)
            val supportFragmentList: ArrayList<ISupportFragment?> = ArrayList()
            if (keysAfter.contains(primaryKey)) {
                supportFragmentList.add(0, mFragments[primaryKey])
                keysAfter.remove(primaryKey)
            }
            for (s in keysAfter) {
                supportFragmentList.add(mFragments[s])
            }
            LogUtil.sd(keysAfter)
            val obj: Array<ISupportFragment> = supportFragmentList.toArray(arrayOfNulls<ISupportFragment>(0))
            if (obj.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    loadMultipleRootFragment(R.id.container, -1, *obj)
                }
            }
        }
    }
    //endregion

}