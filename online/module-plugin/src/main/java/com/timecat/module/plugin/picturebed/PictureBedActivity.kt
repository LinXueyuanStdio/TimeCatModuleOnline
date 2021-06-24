package com.timecat.module.plugin.picturebed

import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.identity.readonly.RouterHub
import com.timecat.component.router.app.NAV
import com.timecat.middle.image.BaseImageSelectorActivity
import com.timecat.module.plugin.R
import com.xiaojinzi.component.anno.RouterAnno

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/6/16
 * @description 插件商店
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.PLUGIN_PictureBedActivity)
class PictureBedActivity : BaseImageSelectorActivity() {
    private lateinit var header: PictureBedView
    private lateinit var adapter: BedAdapter
    override fun title(): String = "时光猫图床"
    override fun layout(): Int = R.layout.plugin_activity_picturebed
    override fun routerInject() = NAV.inject(this)
    override fun imageRecyclerView(): RecyclerView = header.recyclerView
    override fun initView() {
        header = PictureBedView(this)
        adapter = BedAdapter(mutableListOf())
        adapter.addHeaderView(header)

        //super.initView()里初始化 header 的 imageRecyclerView
        super.initView()

        //初始化主界面
        val rv = findViewById<RecyclerView>(R.id.rv)
        rv.adapter = adapter
    }

    /**
     * RoomRecord 的 PictureBed 类型中派生出来，用于展示图床信息
     */
    data class PictureBed(val uuid: String, val name: String)
    class BedAdapter(
        data: MutableList<PictureBed>
    ) : BaseQuickAdapter<PictureBed, BaseViewHolder>(R.layout.plugin_picture_beds, data) {
        override fun convert(holder: BaseViewHolder, item: PictureBed) {
            //每一个PictureBed，点进去就是对应的传输记录
        }

    }
}