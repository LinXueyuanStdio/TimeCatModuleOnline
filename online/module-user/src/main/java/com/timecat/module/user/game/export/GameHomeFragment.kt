package com.timecat.module.user.game.export

import android.view.View
import android.widget.Button
import com.timecat.component.router.app.NAV
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.module.user.R
import com.timecat.page.base.friend.main.BaseMainFragment
import com.xiaojinzi.component.anno.FragmentAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
@FragmentAnno(RouterHub.USER_GameHomeFragment)
class GameHomeFragment : BaseMainFragment() {
    override fun layout(): Int = R.layout.user_fragment_game_home

    lateinit var cube: Button
    lateinit var card: Button
    lateinit var shop: Button
    lateinit var bag: Button

    override fun bindView(view: View) {
        super.bindView(view)
        cube = view.findViewById(R.id.cube)
        card = view.findViewById(R.id.card)
        shop = view.findViewById(R.id.shop)
        bag = view.findViewById(R.id.bag)
    }

    override fun lazyInit() {
        cube.setShakelessClickListener {
            NAV.go(RouterHub.USER_CubeActivity)
        }
        card.setShakelessClickListener {
            NAV.go(RouterHub.USER_CardActivity)
        }
        shop.setShakelessClickListener {
            NAV.go(RouterHub.USER_ShopActivity)
        }
        bag.setShakelessClickListener {
            NAV.go(RouterHub.USER_BagActivity)
        }
    }
}