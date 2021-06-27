package com.timecat.module.plugin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.tencent.shadow.dynamic.host.EnterCallback
import com.timecat.module.plugin.manager.Plugin

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
class PluginContainerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), Plugin.UpdateListener, EnterCallback {
    init {
        ProgressBar(context).apply {

        }.also { addView(it) }
    }

    override fun onStart() {

    }

    override fun onLoad() {
    }

    override fun onPause() {
    }

    override fun onResume() {
    }

    override fun onStop() {
    }

    override fun onComplete() {
    }

    override fun onShowLoadingView(view: View?) {

    }

    override fun onCloseLoadingView() {
    }

    override fun onEnterComplete() {
    }
}