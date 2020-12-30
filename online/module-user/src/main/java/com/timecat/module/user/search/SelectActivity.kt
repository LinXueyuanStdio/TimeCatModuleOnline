package com.timecat.module.user.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.page.base.base.lazyload.adapter.FragmentLazyPagerAdapter
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/11/2
 * @description 搜索、单选
 * @usage
 * val types = ArrayList<String>()
 * types.add(SelectActivity.SelectType.USER.title)
 * ARouter.getInstance().build(RouterHub.SEARCH_SelectActivity)
 *     .withObject("types", types)
 *     .navigation(this, SEARCH_USER)
 */
@RouterAnno(hostAndPath = RouterHub.SEARCH_SelectActivity)
class SelectActivity : SearchActivity() {
    @AttrValueAutowiredAnno("types")
    @JvmField
    var types: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        NAV.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun getFragmentAdapter(): FragmentPagerAdapter {
        val titles = ArrayList<String>()
        val fragments = ArrayList<Fragment>()
        types?.forEach { i ->
            SelectType.values().find { it.title == i }?.let {
                titles.add(it.title)
                fragments.add(NAV.fragment((it.path)))
            }
        }
        val adapter = FragmentLazyPagerAdapter(supportFragmentManager, fragments, titles)
        return adapter
    }

    enum class SelectType(val title: String, val path: String) : Serializable {
        USER("用户", RouterHub.SEARCH_SelectUserFragment),
        HunPermission("混权限", RouterHub.SEARCH_SelectHunPermissionFragment),
        MetaPermission("元权限", RouterHub.SEARCH_SelectMetaPermissionFragment),
        Role("角色", RouterHub.SEARCH_SelectRoleFragment),
        Identity("身份", RouterHub.SEARCH_SelectIdentityFragment)
    }
}