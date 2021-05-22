package com.timecat.module.user.game.export

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import cn.leancloud.AVOSCloud
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.google.android.material.chip.Chip
import com.timecat.component.commonsdk.extension.beGone
import com.timecat.component.commonsdk.extension.beVisible
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.User
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.font.FontAwesome
import com.timecat.identity.font.FontDrawable
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.TimerView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginMainFragment
import com.timecat.module.user.game.core.Level
import com.timecat.module.user.game.core.Water
import com.timecat.module.user.game.core.Water.recoverTime
import com.timecat.module.user.game.core.WaterModified
import com.timecat.module.user.game.core.allCharge
import com.timecat.page.base.view.BlurringToolbar
import com.xiaojinzi.component.anno.FragmentAnno
import io.reactivex.disposables.Disposable
import org.greenrobot.eventbus.Subscribe

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_GameHomeFragment)
class GameHomeFragment : BaseLoginMainFragment() {
    override fun needEventBus(): Boolean = true
    override fun layout(): Int = R.layout.user_fragment_game_home

    lateinit var main: ImageView
    lateinit var cube: Button
    lateinit var bag: Button
    lateinit var mail: Button

    lateinit var activity: Button
    lateinit var card: Button
    lateinit var shop: Button

    lateinit var level: Chip
    lateinit var star: Chip
    lateinit var water: Chip
    lateinit var event_timer: TimerView
    lateinit var state: TextView
    lateinit var exp_bar: RoundCornerProgressBar

    lateinit var currency: Chip
    lateinit var charge: Chip

    override fun bindView(view: View) {
        super.bindView(view)
        val toolbar: BlurringToolbar = view.findViewById(R.id.toolbar)
        val background: View = view.findViewById(R.id.background)
        toolbar.setBlurredView(background)
        toolbar.setPaddingStatusBar(_mActivity)
        toolbar.setTitle("游戏化")

        main = view.findViewById(R.id.main)
        exp_bar = view.findViewById(R.id.exp_bar)
        level = view.findViewById(R.id.level)
        card = view.findViewById(R.id.card)
        cube = view.findViewById(R.id.cube)
        mail = view.findViewById(R.id.mail)

        star = view.findViewById(R.id.star)
        activity = view.findViewById(R.id.activity)
        bag = view.findViewById(R.id.bag)
        shop = view.findViewById(R.id.shop)

        water = view.findViewById(R.id.water)
        event_timer = view.findViewById(R.id.event_timer)
        state = view.findViewById(R.id.state)

        currency = view.findViewById(R.id.currency)
        charge = view.findViewById(R.id.charge)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        cube.setShakelessClickListener {
            NAV.go(RouterHub.USER_AllOwnCubeActivity)
        }
        bag.setShakelessClickListener {
            NAV.go(RouterHub.USER_BagActivity)
        }
        mail.setShakelessClickListener {
            NAV.go(RouterHub.USER_AllOwnMailActivity)
        }
        activity.setShakelessClickListener {
            NAV.go(RouterHub.USER_AllOwnActivityActivity)
        }
        card.setShakelessClickListener {
            NAV.go(RouterHub.USER_CardActivity)
        }
        shop.setShakelessClickListener {
            NAV.go(RouterHub.USER_ShopActivity)
        }
        exp_bar.progressColor = Attr.getAccentColor(_mActivity)
        exp_bar.setBackgroundColor(Attr.getBackgroundDarkColor(_mActivity))
        val tf = FontAwesome.getFontAwesomeSolid(context)
        water.chipIcon = FontDrawable(context, tf, R.string.fa_fire_solid).apply { textSize = 24f }
        currency.chipIcon = FontDrawable(context, tf, R.string.fa_dice_d6_solid).apply { textSize = 24f }
        charge.chipIcon = FontDrawable(context, tf, R.string.fa_globe_europe_solid).apply { textSize = 24f }

        val user = I()
        loadGameData(user)
    }

    private fun loadGameData(user: User) {
        mStatefulLayout?.showLoading()
        user.fetchInBackground().subscribe({
            mStatefulLayout?.showContent()
            loadGameView(I())
        }, {
            mStatefulLayout?.showError {
                loadGameData(user)
            }
        })
    }

    private fun loadGameView(user: User) {
        LogUtil.e(user.toJSONString())
        IconLoader.loadIcon(_mActivity, main, user.avatar)
        val (currentLevel, currentExp) = Level.getLevel(user.exp)
        notifyLevel(currentLevel, currentExp)

        exp_bar.setShakelessClickListener {
            ToastUtil.i("当前经验 $currentExp / ${Level.expLimit(currentLevel)}")
        }
        level.setShakelessClickListener {
            ToastUtil.i("当前等级 $currentLevel")
        }
        star.text = "星级 ${user.star}"
        star.setShakelessClickListener {
            ToastUtil.i("当前星级 ${user.star}")
        }
        loadWater()

        water.setShakelessClickListener {
            showWaterDialog()
        }
        currency.text = "${user.currency}"
        currency.setOnCloseIconClickListener {
            ToastUtil.w("开发喵施工中")
        }
        charge.text = "${user.allCharge}"
        charge.setOnCloseIconClickListener {
            ToastUtil.w("开发喵施工中")
        }
    }

    private fun showWaterDialog(): Disposable {
        mStatefulLayout?.showLoading()
        return AVOSCloud.getServerDateInBackground().subscribe({
            val i = I()
            val currentTime = it.date.time
            Water.compute(i, currentTime) { trueWater, _, _ ->
                mStatefulLayout?.showContent()
                ToastUtil.i("体力 $trueWater")
            }
        }, {
            mStatefulLayout?.showError("发生错误") {
                loadWater()
            }
        })
    }

    private fun loadWater(): Disposable {
        mStatefulLayout?.showLoading()
        return AVOSCloud.getServerDateInBackground().subscribe({
            val i = I()
            val currentTime = it.date.time
            Water.compute(i, currentTime) { trueWater, waterLimit, needWaitTime ->
                var nowWater = trueWater
                event_timer.apply {
                    setOnCountdownEndListener {
                        nowWater++
                        tryToStartTimer(nowWater, waterLimit, recoverTime, this)
                    }
                }
                tryToStartTimer(nowWater, waterLimit, needWaitTime, event_timer)
            }
            mStatefulLayout?.showContent()
        }, {
            mStatefulLayout?.showError("发生错误") {
                loadWater()
            }
        })
    }

    private fun tryToStartTimer(nowWater: Int, waterLimit: Int, recoverTime: Long, timer: TimerView) {
        notifyWater(nowWater.coerceIn(0, waterLimit), waterLimit)
        if (nowWater < waterLimit) {
            timer.beVisible()
            state.setText("后恢复 1 体力")
            timer.start(recoverTime)
        } else {
            timer.beGone()
            state.setText("体力已满")
        }
    }

    private fun notifyWater(nowWater: Int, waterLimit: Int) {
        water.text = "${nowWater} / ${waterLimit}"
        Water.save(nowWater, I())
    }

    private fun notifyLevel(currentLevel: Int, currentExp: Long) {
        level.text = "等级 ${currentLevel}"
        exp_bar.max = Level.expLimit(currentLevel).toFloat()
        exp_bar.progress = currentExp.toFloat()
    }


    @Subscribe
    fun onEvent(e: WaterModified?) {
        loadWater()
    }
}