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
import com.timecat.module.user.game.task.fragment.*
import com.timecat.module.user.game.task.rule.ActivityContext
import com.timecat.module.user.game.task.rule.GameService
import com.timecat.module.user.game.task.vm.TaskViewModel
import com.xiaojinzi.component.anno.RouterAnno
import com.xiaojinzi.component.anno.ServiceAutowiredAnno
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
@RouterAnno(hostAndPath = RouterHub.USER_AllOwnActivityActivity)
class AllOwnActivityActivity : BaseLoginMainActivity() {
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
        LogUtil.sd(activityContext.ownActivity)
        viewModel.channels.observe(this) {
            LogUtil.sd(it)
            refreshChannel(it)
        }
        initBottomBar()
        viewModel.activities.postValue(activityContext.ownActivity)

        viewModel.tasks.postValue(activityContext.tasks)
        viewModel.taskProgress.postValue(activityContext.taskProgress)
        viewModel.taskRewardProgress.postValue(activityContext.taskRewardProgress)
        viewModel.rules.postValue(activityContext.rules)
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
                EventBusActivityScope.getDefault(this@AllOwnActivityActivity).post(TabReselectedEvent(position))
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
            val a = NAV.fragment(item.fragmentRouterPath)
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

    private fun addTabBlockItem(item: TabBlockItem): Any {
        mBottomBar.addItem(item.createTabView(this))
        return mFragments[item.key] ?: addToFragmentMap(item.key, item.fragmentRouterPath)
    }

    private fun addToFragmentMap(key: String, path: String): Any {
        val f = NAV.fragment(path)
        if (f is ISupportFragment) {
            mFragments[key] = f as ISupportFragment
        }
        return f
    }

    private fun refreshChannel(list: List<Channel>) {
        val loadingFragments = mutableListOf<ISupportFragment>()
        mBottomBar.clearContainer()
        for (element in list) {
            val c = element
            val obj = c.obj
            if (obj is TabBlockItem) {
                val f = addTabBlockItem(obj)
                if (f is ISupportFragment) {
                    loadingFragments.add(f)
                }
            } else if (obj is JSONObject) {
                val item = obj.toJavaObject(TabBlockItem::class.java)
                val f = addTabBlockItem(item)
                if (f is ISupportFragment) {
                    loadingFragments.add(f)
                }
            }
        }
        LogUtil.se("${loadingFragments.size}")
        if (loadingFragments.isNotEmpty()) {
            loadMultipleRootFragment(R.id.container, 0, *loadingFragments.toTypedArray())
        }
    }
    //endregion

}