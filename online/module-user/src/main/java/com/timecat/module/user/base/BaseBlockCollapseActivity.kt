package com.timecat.module.user.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.component.router.app.FallBackFragment
import com.timecat.component.router.app.NAV
import com.timecat.data.bmob.data.common.Block
import com.timecat.data.bmob.ext.bmob.requestOneBlock
import com.timecat.data.bmob.ext.net.oneBlockOf
import com.timecat.module.user.social.common.BlockViewModel
import com.timecat.module.user.social.common.CommentListFragment
import com.timecat.module.user.social.common.LikeListFragment
import com.timecat.module.user.social.common.MomentListFragment

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-11-24
 * @description 动态、评论
 * @usage null
 */
abstract class BaseBlockCollapseActivity : BaseDetailCollapseActivity() {
    override fun routerInject() = NAV.inject(this)

    lateinit var blockViewModel: BlockViewModel

    override fun initViewAfterLogin() {
        super.initViewAfterLogin()
        initViewModel()
    }

    protected open fun initViewModel() {
        blockViewModel = ViewModelProvider(this).get(BlockViewModel::class.java)
        blockViewModel.block.observe(this, {
            it?.let {
                loadDetail(it)
                onContentLoaded()
            }
        })
    }

    protected open fun loadDetail(block: Block) {
        LogUtil.e(block.parent)
        titleString = block.title
        setupTabs(block)
    }

    protected open fun setupTabs(block:Block) {
        tabs.getTabAt(0)?.let {
            it.text = "转发${block.relays}"
        }
        tabs.getTabAt(1)?.let {
            it.text = "评论${block.comments}"
        }
        tabs.getTabAt(2)?.let {
            it.text = "赞${block.likes}"
        }
    }

    /**
     * 继承者需要的时候再调用，获取block，默认不调用
     * 且只能在重写 fetch() 中调用
     */
    fun fetch(blockId: String) {
        blockViewModel attach requestOneBlock {
            query = oneBlockOf(blockId)
            onSuccess = {
                blockViewModel.block.postValue(it)
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
            return 3
        }

        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MomentListFragment()
                1 -> CommentListFragment()
                2 -> LikeListFragment()
                else -> FallBackFragment()
            }
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> "转发"
                1 -> "讨论"
                2 -> "点赞"
                else -> super.getPageTitle(position)
            }
        }
    }

}