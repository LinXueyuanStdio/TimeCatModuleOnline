package com.timecat.module.user.social.leaderboard


import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.View
import android.widget.Toast
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.UpdateListener
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.timecat.component.commonsdk.utils.override.LogUtil
import com.timecat.data.bmob.data.common.Exec
import com.timecat.element.alert.ToastUtil
import com.timecat.identity.data.exec.RecommendBlock
import com.timecat.module.user.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-08
 * @description null
 * @usage null
 */
class RecommendAdapter(data: MutableList<Exec>) :
    BaseQuickAdapter<Exec, BaseViewHolder>(R.layout.leaderboard_recommend_item, data) {

    @SuppressLint("WrongConstant")
    override fun convert(holder: BaseViewHolder, item: Exec) {
        LogUtil.e(item.toString())
        //    if (item.cover != null) {
        //      ArmsUtils.obtainAppComponentFromContext(mContext).imageLoader()
        //          .loadImage(mContext, CommonImageConfigImpl.builder()
        //              .imageView(helper.getView(R.id.iv_avatar))
        //              .url(webApp.cover.getFileUrl()).build());
        //    }
        val r = RecommendBlock.fromJson(item.structure)
        holder.setText(R.id.tv_name, r.title)
        holder.setText(R.id.tv_content, r.content)
        holder.setText(R.id.status, "状态：" + r.getStatusStr(item.status))
        holder.setText(R.id.url, "推荐目标：" + r.structure)
        holder.setText(R.id.extra, "消息：" + r.extra)
        if (item.status <= 2) {
            holder.setText(R.id.cancel, "取消推荐")
        } else {
            holder.setText(R.id.cancel, "恢复推荐")
        }
        holder.getView<View>(R.id.extra).setOnClickListener {
            val clipboardManager = context
                .getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("Copy content", r.extra)
            clipboardManager.setPrimaryClip(clipData)
            ToastUtil.show("复制成功")
        }
        holder.getView<View>(R.id.cancel).setOnClickListener {
            if (item.status <= 2) {
                //撤销推荐
                item.status = RecommendBlock.RECOMMEND_CANCEL
                item.update(object : UpdateListener() {
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            holder.setText(R.id.cancel, "再次推荐")
                            holder.setText(R.id.status, "状态：" + r.getStatusStr(item.status))
                            holder.setText(R.id.extra, "消息：" + r.extra)
                            holder.setVisible(R.id.extra, true)
                        } else {
                            Toast.makeText(context, "操作失败$e", Toast.LENGTH_LONG).show()
                            LogUtil.e(e.toString())
                            item.status = RecommendBlock.RECOMMEND_WAITING_TO_CHECK//回滚
                        }
                    }
                })
            } else {
                //再次推荐
                item.status = RecommendBlock.RECOMMEND_WAITING_TO_CHECK
                item.update(object : UpdateListener() {
                    override fun done(e: BmobException?) {
                        if (e == null) {
                            holder.setText(R.id.cancel, "取消推荐")
                            holder.setText(R.id.status, "状态：" + r.getStatusStr(item.status))
                            holder.setText(R.id.extra, "消息：" + r.extra)
                            holder.setVisible(R.id.extra, true)
                        } else {
                            Toast.makeText(context, "操作失败$e", Toast.LENGTH_LONG).show()
                            LogUtil.e(e.toString())
                            item.status = RecommendBlock.RECOMMEND_CANCEL //回滚
                        }
                    }
                })
            }
        }
    }
}
