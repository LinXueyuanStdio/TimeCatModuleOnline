package com.timecat.module.user.game.export

import android.view.View
import android.widget.Button
import android.widget.ImageView
import cn.leancloud.AVOSCloud
import com.afollestad.materialdialogs.MaterialDialog
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.google.android.material.chip.Chip
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.identity.Attr
import com.timecat.component.router.app.NAV
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.business.TimerView
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.utils.IconLoader
import com.timecat.module.user.R
import com.timecat.module.user.base.login.BaseLoginMainFragment
import com.timecat.module.user.game.core.Level
import com.timecat.module.user.game.core.Water
import com.timecat.module.user.game.core.Water.recoverTime
import com.timecat.page.base.view.BlurringToolbar
import com.xiaojinzi.component.anno.FragmentAnno
import io.reactivex.disposables.Disposable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_GameHomeFragment)
class GameHomeFragment : BaseLoginMainFragment() {
    override fun layout(): Int = R.layout.user_fragment_game_home

    lateinit var main: ImageView
    lateinit var card: Button
    lateinit var cube: Button
    lateinit var bag: Button
    lateinit var shop: Button

    lateinit var level: Chip
    lateinit var star: Chip
    lateinit var water: Chip
    lateinit var event_timer: TimerView
    lateinit var exp_bar: RoundCornerProgressBar

    lateinit var currency: Chip
    lateinit var charge: Chip

    override fun bindView(view: View) {
        super.bindView(view)
        val toolbar: BlurringToolbar = view.findViewById(R.id.toolbar)
        val background: View = view.findViewById(R.id.background)
        toolbar.setBlurredView(background)
        toolbar.setPaddingStatusBar(_mActivity)

        main = view.findViewById(R.id.main)
        exp_bar = view.findViewById(R.id.exp_bar)
        level = view.findViewById(R.id.level)
        card = view.findViewById(R.id.card)
        cube = view.findViewById(R.id.cube)

        star = view.findViewById(R.id.star)
        bag = view.findViewById(R.id.bag)
        shop = view.findViewById(R.id.shop)

        water = view.findViewById(R.id.water)
        event_timer = view.findViewById(R.id.event_timer)

        currency = view.findViewById(R.id.currency)
        charge = view.findViewById(R.id.charge)
    }

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()

        val user = I()
        LogUtil.e(user.toJSONString())
        IconLoader.loadIcon(_mActivity, main, user.avatar)
        card.setShakelessClickListener {
            NAV.go(RouterHub.USER_CardActivity)
        }
        cube.setShakelessClickListener {
            NAV.go(RouterHub.USER_AllCubeActivity)
        }
        bag.setShakelessClickListener {
            NAV.go(RouterHub.USER_BagActivity)
        }
        shop.setShakelessClickListener {
            NAV.go(RouterHub.USER_ShopActivity)
        }

        notifyLevel(user.level, user.exp)
        exp_bar.progressColor = Attr.getAccentColor(_mActivity)
        exp_bar.setBackgroundColor(Attr.getBackgroundDarkColor(_mActivity))
        exp_bar.setShakelessClickListener {
            ToastUtil.i("当前等级 ${user.level}")
        }
        level.setShakelessClickListener {
            ToastUtil.i("当前等级 ${user.level}")
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
        charge.text = "${user.charge}"
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
                MaterialDialog(_mActivity).show {
                    message(text = "体力 $trueWater")
                }
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
        if (nowWater <= waterLimit) {
            timer.start(recoverTime)
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
}