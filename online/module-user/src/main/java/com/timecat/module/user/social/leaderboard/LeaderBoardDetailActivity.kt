package com.timecat.module.user.social.leaderboard


import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

import com.timecat.element.alert.ToastUtil
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.dao.block.AppDao
import com.timecat.data.bmob.dao.block.LeaderBoardBlockDao
import com.timecat.data.bmob.dao.exec.RecommendDao
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.data.common.Block2Block
import com.timecat.data.bmob.data.common.Exec
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.module.user.R
import com.timecat.module.user.adapter.BlockItem
import com.timecat.module.user.base.BaseBlockListActivity
import com.timecat.identity.data.service.DataError
import com.timecat.identity.data.service.OnFindListener
import com.timecat.identity.data.service.OnSaveListener
import org.joda.time.DateTime

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.LEADERBOARD_LeaderBoardDetailActivity)
class LeaderBoardDetailActivity : BaseBlockListActivity() {

    @AttrValueAutowiredAnno("leaderBoard")
    lateinit var leaderBoard: Block

    override fun routerInject() = NAV.inject(this)

    override fun title(): String = leaderBoard.title

    override fun onRefresh() {
        mRefreshLayout.isRefreshing = true
        LeaderBoardBlockDao.findAllItemsInBoard(leaderBoard, object : OnFindListener<Block2Block> {
            override fun success(data: List<Block2Block>) {
                mRefreshLayout.isRefreshing = false
                if (data.isEmpty()) {
                    Toast.makeText(applicationContext, "什么也没找到", Toast.LENGTH_SHORT).show()
                } else {
                    mAdapter.replaceData(data.map { BlockItem(it.from) })
                }
            }

            override fun error(e: DataError) {
                mRefreshLayout.isRefreshing = false
                Toast.makeText(applicationContext, "查询失败", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.leaderboard_recommend, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == android.R.id.home) {
            finish()
            return true
        }
        if (i == R.id.add) {
            val user = UserDao.getCurrentUser()
            if (user == null) {
                NAV.go(RouterHub.LOGIN_LoginActivity)
                return false
            }
            MaterialDialog(this).show {
                title(text = "推荐")
                customView(R.layout.leaderboard_dialog_add_recommend)
                positiveButton {
                    val view = it.getCustomView()
                    val editText1 = view.findViewById<EditText>(R.id.title)
                    val title = editText1.text.toString()
                    val editText2 = view.findViewById<EditText>(R.id.content)
                    val content = editText2.text.toString()
                    val editText3 = view.findViewById<EditText>(R.id.url)
                    val url = editText3.text.toString()

                    AppDao.addWebApp(user, title, content, url, object : OnSaveListener<Block> {
                        override fun success(data: Block) {
                            RecommendDao.addForLeaderBoard(
                                user, title, content,
                                "于 ${DateTime().toString("yyyy-MM-dd HH:mm:ss")} 提交推荐",
                                data, leaderBoard, object : OnSaveListener<Exec> {
                                    override fun success(data: Exec) {
                                        onRefresh()
                                        ToastUtil.ok("提交推荐成功！")
                                    }

                                    override fun error(e: DataError) {
                                        onRefresh()
                                        LogUtil.e(e.toString())
                                        ToastUtil.e("提交推荐失败！")
                                    }
                                }
                            )
                        }

                        override fun error(e: DataError) {
                            LogUtil.e(e.toString())
                            ToastUtil.e("提交推荐失败！")
                        }
                    })
                }
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
