package com.timecat.module.user.base

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import com.effective.android.panel.PanelSwitchHelper
import com.effective.android.panel.view.PanelSwitchLayout
import com.effective.android.panel.view.panel.PanelView
import com.shuyu.textutillib.EmojiLayout
import com.shuyu.textutillib.RichEditBuilder
import com.shuyu.textutillib.RichEditText
import com.shuyu.textutillib.SmileUtils
import com.shuyu.textutillib.listener.OnEditTextUtilJumpListener
import com.shuyu.textutillib.model.TopicModel
import com.shuyu.textutillib.model.UserModel
import com.timecat.component.commonsdk.extension.hideKeyboard
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.element.alert.ToastUtil
import com.timecat.extend.image.savablePath
import com.timecat.identity.data.base.AIT_PHOTO
import com.timecat.identity.data.base.AttachmentItem
import com.timecat.identity.data.base.AttachmentTail
import com.timecat.identity.data.service.DataError
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.middle.block.util.KeyboardUtil
import com.timecat.module.user.R
import com.timecat.module.user.ext.receieveImage


/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020-02-16
 * @description 重量级编辑器 + 进阶版
 * @usage 简化添加 社区 block 流程
 * emojiEditText
 */
abstract class BaseComplexEditorActivity : BaseSimpleEditorActivity() {
    companion object {
        const val REQUEST_USER_CODE_INPUT = 1111
        const val REQUEST_USER_CODE_CLICK = 2222
        const val REQUEST_TOPIC_CODE_INPUT = 3333
        const val REQUEST_TOPIC_CODE_CLICK = 4444
    }

    /**
     * 涉及的话题
     */
    private val topicModels = ArrayList<TopicModel>()

    /**
     * 提及的用户
     */
    private val nameList = ArrayList<UserModel>()

    override fun layout(): Int = R.layout.user_activity_complex_add

    lateinit var ok: Button

    lateinit var emojiEditText: RichEditText

    lateinit var panel_switch_layout: PanelSwitchLayout
    lateinit var photo: View
    lateinit var at: View
    lateinit var topic: View
    lateinit var block: View
    lateinit var more: View
    lateinit var keyboard: View
    protected var mHelper: PanelSwitchHelper? = null

    override fun bindView() {
        super.bindView()
        ok = findViewById(R.id.ok)

        emojiEditText = findViewById(R.id.emojiEditText)
        formData.emojiEditText = emojiEditText

        panel_switch_layout = findViewById(R.id.panel_switch_layout)
        photo = findViewById(R.id.photo)
        at = findViewById(R.id.at)
        topic = findViewById(R.id.topic)
        block = findViewById(R.id.block)
        more = findViewById(R.id.more)
        keyboard = findViewById(R.id.keyboard)
    }

    override fun onStart() {
        super.onStart()
        if (mHelper == null) {
            mHelper = PanelSwitchHelper.Builder(this)
//                .addKeyboardStateListener {
//                    onKeyboardChange { bool, int ->
//                        //可选实现，监听输入法变化
//                    }
//                }
//                .addEditTextFocusChangeListener {
//                    onFocusChange { _, hasFocus ->
//                        //可选实现，监听输入框焦点变化
//                    }
//                }
//                .addViewClickListener {
//                    onClickBefore {
//                        //可选实现，监听触发器的点击
//                    }
//                }
                .addPanelChangeListener {
                    onKeyboard {
                        //可选实现，输入法显示回调
                        keyboard.animate().cancel()
                        keyboard.animate().rotation(0f).start()
                    }
                    onNone {
                        //可选实现，默认状态回调
                        keyboard.animate().cancel()
                        keyboard.animate().rotation(180f).start()
                    }
                    onPanel {
                        //可选实现，面板显示回调
                        keyboard.animate().cancel()
                        keyboard.animate().rotation(0f).start()
                    }
                    onPanelSizeChange { panelView, _, _, _, width, height ->
                        //可选实现，输入法动态调整时引起的面板高度变化动态回调
                        if (panelView is PanelView) {
                            when (panelView.id) {
                                R.id.panel_emotion -> {
                                    val pagerView: EmojiLayout = panelView.findViewById(R.id.emojiLayout)
                                    initEmoji()
                                    pagerView.editTextSmile = emojiEditText
                                }
                            }
                        }
                    }
                }
//                .addContentScrollMeasurer { //可选，滑动模式下，可以针对内容面板内的view，定制滑动距离，默认滑动距离为 defaultDistance
//                    getScrollDistance { defaultDistance -> defaultDistance - 200 }
//                    getScrollViewId { R.id.recycler_view }
//                }
//                .addPanelHeightMeasurer {   //可选 用于设置未获取输入法高度前面板的高度，如果不设置则默认以框架内高度为主
//                    getTargetPanelDefaultHeight { DisplayUtils.dip2px(this@DefaultHeightPanelActivity, 400f) }
//                    getPanelTriggerId { R.id.add_btn }
//                }
                .addContentScrollMeasurer { //可选，滑动模式下，可以针对内容面板内的view，定制滑动距离，默认滑动距离为 defaultDistance
                    getScrollDistance { defaultDistance -> getScrollDistanceOfScrollView(defaultDistance) }
                    getScrollViewId { R.id.scrollView }
                }
                .contentScrollOutsideEnable(true)  //可选，默认为true
                .logTrack(true)                   //可选，默认false，是否开启log信息输出
                .build(panel_switch_layout, true)                      //可选，默认false，是否默认打开输入法
        }
    }

    protected open fun getScrollDistanceOfScrollView(defaultDistance: Int): Int {
        return 0
    }

    override fun onBackPressed() {
        if (mHelper?.hookSystemBackByPanelSwitcher() == null) {
            return
        }
        super.onBackPressed()
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        photo.setShakelessClickListener {
            onAddImage()
        }
        at.setShakelessClickListener {
            val intent = Intent(this, UserListBlockActivity::class.java)
            startActivityForResult(intent, REQUEST_USER_CODE_CLICK)
        }

        topic.setShakelessClickListener {
            val intent = Intent(this, TopicListBlockActivity::class.java)
            startActivityForResult(intent, REQUEST_TOPIC_CODE_CLICK)
        }

        block.setShakelessClickListener {

        }

        more.setShakelessClickListener {

        }

        keyboard.setShakelessClickListener {
            mHelper?.apply {
                when {
                    isPanelState() -> toKeyboardState()
                    isKeyboardState() -> hideKeyboard()
                    else -> toKeyboardState()
                }
            }
        }

        val richEditBuilder = RichEditBuilder()
        richEditBuilder.setEditText(emojiEditText)
            .setTopicModels(topicModels)
            .setUserModels(nameList)
            .setColorAtUser("#2962FF")
            .setColorTopic("#2962FF")
            .setEditTextAtUtilJumpListener(object : OnEditTextUtilJumpListener {
                override fun notifyAt() = atUser()

                override fun notifyTopic() = atTopic()
            })
            .builder()
    }

    protected fun atUser() {
        val intent = Intent(this, UserListBlockActivity::class.java)
        startActivityForResult(intent, REQUEST_USER_CODE_INPUT)
    }

    protected fun atTopic() {
        val intent = Intent(this, TopicListBlockActivity::class.java)
        startActivityForResult(intent, REQUEST_TOPIC_CODE_INPUT)
    }

    protected fun lockEverythingForPublish() {
        KeyboardUtil.hideKeyboard(emojiEditText)
        mStatefulLayout?.showLoading()
        ok.isEnabled = false
    }

    var errorCallback: (DataError) -> Unit = {
        ToastUtil.e("创建失败！${it.msg}")
        LogUtil.e("创建失败！${it.msg}")
        unlockEverythingWhenPublish()
    }

    protected fun unlockEverythingWhenPublish() {
        mStatefulLayout?.showContent()
        ok.isEnabled = true
    }

    protected fun publish() {
        lockEverythingForPublish()
        val originImgs = imageAdapter.data.map { it.savablePath() }.filterNotNull()
        if (originImgs.isEmpty()) {
            formData.attachments = null
            release()
        } else {
            receieveImage(I(), originImgs, false) { origins ->
                val photoList = origins.map { AttachmentItem(AIT_PHOTO, it) }.toMutableList()
                formData.attachments = AttachmentTail(photoList)
                release()
            }
        }
    }

    abstract fun release()

    /**
     * 处理自己的表情
     */
    private fun initEmoji() {
        val data: MutableList<Int> = ArrayList()
        val strings: MutableList<String> = ArrayList()
        for (i in 1..67) {
            val resName = String.format("im_emotion_%2d", i);
            val resId = resources.getIdentifier(resName, "drawable", packageName)
            data.add(resId)
            strings.add("[$resName]")
        }
        /**初始化为自己的 */
        SmileUtils.addPatternAll(SmileUtils.getEmoticons(), strings, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    REQUEST_USER_CODE_CLICK -> emojiEditText.resolveAtResult(
                        it.getSerializableExtra(
                            UserListBlockActivity.DATA
                        ) as UserModel
                    )
                    REQUEST_USER_CODE_INPUT -> emojiEditText.resolveAtResultByEnterAt(
                        it.getSerializableExtra(
                            UserListBlockActivity.DATA
                        ) as UserModel
                    )

                    REQUEST_TOPIC_CODE_INPUT -> emojiEditText.resolveTopicResultByEnter(
                        it.getSerializableExtra(
                            TopicListBlockActivity.DATA
                        ) as TopicModel
                    )
                    REQUEST_TOPIC_CODE_CLICK -> emojiEditText.resolveTopicResult(
                        it.getSerializableExtra(
                            TopicListBlockActivity.DATA
                        ) as TopicModel
                    )
                }
            }
        }
    }
}