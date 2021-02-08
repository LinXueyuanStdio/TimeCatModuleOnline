package com.timecat.module.user.base

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.standard.navi.BottomBar
import com.timecat.module.user.R
import com.timecat.page.base.base.support.BaseSupportActivity
import me.yokeyword.fragmentation.ISupportFragment
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/8
 * @description null
 * @usage null
 */
abstract class BaseMainActivity : BaseSupportActivity() {

    protected lateinit var mBottomBar: BottomBar
    protected lateinit var background: View
    protected lateinit var mini: ImageView
    protected val mFragments: MutableMap<String, ISupportFragment> = HashMap()
    open fun routerInject(){}
    open fun layoutId(): Int = R.layout.user_activity_base_main
    override fun onCreate(savedInstanceState: Bundle?) {
        val start = System.currentTimeMillis()
        LogUtil.sd("开始加载 view $start ms")
        routerInject()
        super.onCreate(savedInstanceState)
        setContentView(layoutId())

        mini = findViewById(R.id.mini)
        background = findViewById(R.id.background)
        background.background = Attr.getWindowBackground(this)
        mini.background = Attr.getMiniBackground(this)

        mBottomBar = findViewById(R.id.bottomBar)


        val end = System.currentTimeMillis()
        LogUtil.sd("结束加载 view $end ms")
        LogUtil.sd("总耗时 " + (end - start) + " ms")
    }

    abstract fun init()

    override fun applySkin() {
        super.applySkin()
        if (::background.isInitialized)
            background.background = Attr.getWindowBackground(this)
        if (::mini.isInitialized)
            mini.background = Attr.getMiniBackground(this)
    }

    private val WAIT_TIME = 2000L
    private var TOUCH_TIME: Long = 0

    override fun onBackPressedSupport() {
        when {
            mBottomBar.currentItemPosition != 0 -> {
                mBottomBar.setCurrentItem(0)
                //返回首页，消耗掉事件
            }
            System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME -> {
                //这里的事件处理完了
                super.onBackPressedSupport()
            }
            else -> {
                TOUCH_TIME = System.currentTimeMillis()
                ToastUtil.i(R.string.press_again_to_exit)
                //阻止误点击，消耗掉事件
            }
        }
    }
}