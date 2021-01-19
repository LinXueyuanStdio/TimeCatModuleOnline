package com.timecat.module.user.game.cube

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.dao.UserDao
import com.timecat.data.bmob.data.common.Action
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.*
import com.timecat.data.bmob.ext.follow
import com.timecat.data.bmob.ext.net.allFollowBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.block.ForumBlock
import com.timecat.identity.readonly.RouterHub
import com.timecat.module.user.base.BaseDetailCollapseActivity
import com.timecat.module.user.game.cube.fragment.*
import com.timecat.module.user.game.cube.vm.CubeViewModel
import com.timecat.module.user.view.TopicCard
import com.xiaojinzi.component.anno.AttrValueAutowiredAnno
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/10/4
 * @description 方块（人物）
 * @usage 方块相关的评论、帖子、动态等等，不包括方块的养成方面
 * 如果要访问方块的养成，可以访问 AllCubeActivity
 * 这个页面一般用来展示用户未拥有的那些方块的详情
 */
@RouterAnno(hostAndPath = RouterHub.USER_CubeActivity)
class CubeActivity : BaseDetailCollapseActivity() {

    @AttrValueAutowiredAnno("blockId")
    lateinit var blockId: String
    lateinit var viewModel: CubeViewModel
    lateinit var card: TopicCard
    override fun routerInject() = NAV.inject(this)

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        viewModel = ViewModelProvider(this).get(CubeViewModel::class.java)
        viewModel.block.observe(this, {
            it?.let {
                loadDetail(it)
            }
        })
        card = TopicCard(this)
        card.placeholder.updateLayoutParams<ConstraintLayout.LayoutParams> {
            height = getStatusBarHeightPlusToolbarHeight()
        }
        setupHeaderCard(card)
        setupCollapse()
        setupViewPager()
        fetch()
    }

    private fun loadDetail(block: Block) {
        // 1. 加载头部卡片
        val headerBlock = ForumBlock.fromJson(block.structure)
        titleString = block.title
        card.apply {
            title = block.title
            desc = "点赞 ${block.likes}  讨论 ${block.comments}  分享 ${block.relays}"
            icon = headerBlock.header?.icon ?: "R.drawable.ic_launcher"
            button.apply {
                isEnabled = false
                val I = UserDao.getCurrentUser()
                if (I == null) {
                    isEnabled = true
                    buttonColor = primaryColor
                    buttonText = "请登陆"
                    buttonClick = {
                        NAV.go(RouterHub.LOGIN_LoginActivity)
                    }
                } else {
                    var relation: Action? = null
                    requestAction {
                        query = I.allFollowBlock(block)
                        onError = {
                            isEnabled = true
                            buttonColor = primaryColor
                            buttonText = "关注"
                        }
                        onEmpty = {
                            isEnabled = true
                            buttonColor = primaryColor
                            buttonText = "关注"
                        }
                        onSuccess = {
                            isEnabled = true
                            relation = it
                            buttonColor = backgroundDarkestColor
                            buttonText = "已关注"
                        }
                        onListSuccess = {
                            isEnabled = true
                            relation = it[0]
                            buttonColor = backgroundDarkestColor
                            buttonText = "已关注"
                        }
                    }
                    buttonClick = { v ->
                        if (relation == null) {
                            saveAction {
                                target = I follow block
                                onSuccess = {
                                    buttonColor = backgroundDarkestColor
                                    buttonText = "已关注"
                                    ToastUtil.ok("关注成功")
                                }
                                onError = { e ->
                                    ToastUtil.ok("关注失败")
                                    LogUtil.e(e.toString())
                                }
                            }
                        } else {
                            deleteAction {
                                target = relation!!
                                onSuccess = {
                                    buttonColor = primaryColor
                                    buttonText = "关注"
                                    ToastUtil.ok("解除粉丝成功")
                                }
                                onError = { e ->
                                    ToastUtil.e("解除粉丝失败")
                                    LogUtil.e(e.toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun fetch() {
        requestBlock {
            query = oneBlockOf(blockId)
            onSuccess = {
                viewModel.block.postValue(it)
            }
            onError = {
                mStatefulLayout?.showError("出错啦") {
                    fetch()
                }
            }
        }
    }

    override fun getAdapter(): FragmentStatePagerAdapter {
        return DetailAdapter(supportFragmentManager)
    }

    class DetailAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
        override fun getCount(): Int {
            return 4
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> CubeDetailFragment()
                1 -> CommentListFragment()
                2 -> PostListFragment()
                3 -> MomentListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "详情"
                1 -> "讨论"
                2 -> "帖子"
                3 -> "动态"
                else -> super.getPageTitle(position)
            }
        }
    }
}