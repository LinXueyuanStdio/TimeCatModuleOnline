package com.timecat.module.user.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.timecat.data.bmob.data._User;
import com.timecat.data.bmob.data.trace.Trace;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.component.router.app.NAV;
import com.timecat.module.user.R;
import com.timecat.module.user.base.GO;
import com.timecat.module.user.base.LOAD;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-13
 * @description null
 * @usage null
 */
public class TraceAdapter extends BaseMultiItemQuickAdapter<TraceItem, BaseViewHolder> {

    public TraceAdapter(List<TraceItem> list) {
        super(list);
        addItemType(Trace.FOLLOW_USER, R.layout.user_trace_item_user);
    }

    @Override
    protected void convert(BaseViewHolder helper, TraceItem item) {
        _User user = item.getTrace().getUser();
        _User target = item.getTrace().getTarget();

        switch (item.getItemType()) {
            case Trace.FOLLOW_USER:
                if (target != null) {
                    setHeadImage(helper, R.id.head_image, user.getAvatar(), user.getId());
                    setHeadText(helper, R.id.head_title,
                            user.getNickName() + " 关注了 " + target.getNickName(), null);
                    if (!TextUtils.isEmpty(user.getBrief_intro())) {
                        helper.setVisible(R.id.head_content, true);
                        setHeadText(helper, R.id.head_content,
                                user.getBrief_intro(), null);
                    }
                }
                break;
        }
    }

    private void setHead(BaseViewHolder helper, _User who) {
        LogUtil.se(who);
        View.OnClickListener onClickListener = v -> GO.userDetail(who.getObjectId());
        setHeadImage(helper, R.id.head_image, who.getAvatar(), who.getId());
        setHeadText(helper, R.id.head_title, who.getNickName(), onClickListener);
        if (!TextUtils.isEmpty(who.getBrief_intro())) {
            helper.setVisible(R.id.head_content, true);
            setHeadText(helper, R.id.head_content, who.getBrief_intro(), onClickListener);
        }
    }

    private void setHeadText(BaseViewHolder helper, @IdRes int id, String item, @Nullable View.OnClickListener listener) {
        TextView mTextView = helper.getView(id);
        mTextView.setText(item);
        mTextView.setOnClickListener(listener);
    }


    private void setHeadImage(BaseViewHolder helper, @IdRes int id, String avatar, String userId) {
        CircleImageView iv = helper.getView(id);
        LOAD.image(avatar, iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NAV.go(RouterHub.USER_UserDetailActivity, "userId", userId);
            }
        });
    }

    //设置超链接文字
    private SpannableString getClickableSpan(String he, String it, String doWhatEnd, @NonNull View.OnClickListener listener) {
        int itStrStart = he.length() + 1;
        int itStrEnd = itStrStart + it.length();
        SpannableString spanStr = new SpannableString(he + " " + it + " " + doWhatEnd);
        //设置下划线文字
        //            spanStr.setSpan(new BackgroundColorSpan(Color.GRAY), itStrStart, itStrEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {

            @Override
            public void onClick(@NonNull View widget) {
                listener.onClick(widget);
            }
        }, itStrStart, itStrEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(Color.BLUE), itStrStart, itStrEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }
}