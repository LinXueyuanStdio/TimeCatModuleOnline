package com.timecat.module.user.social.message;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.timecat.data.bmob.dao.UserDao;
import com.timecat.data.bmob.data._User;
import com.timecat.data.bmob.data.msg.Message;
import com.timecat.data.bmob.data.msg.MessageFans;
import com.timecat.data.bmob.data.msg.MessageSystem;
import com.timecat.data.bmob.data.msg.MessageSystemUp;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.component.router.app.NAV;
import com.timecat.module.user.R;
import com.timecat.module.user.base.LOAD;
import com.xiaojinzi.component.anno.RouterAnno;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import de.hdodenhof.circleimageview.CircleImageView;

;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-07-12
 * @description 用户收到的消息 推送 邮件
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.USER_MessageAlertActivity)
public class MessageAlertActivity extends AppCompatActivity {

    private ImageView back;
    private RecyclerView listView;
    private SwipeRefreshLayout refreshLayout;

    private _User current_user = UserDao.getCurrentUser();
    private Integer notification_sum;
    private MessageAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_message_alert);
        findView();
        initialization();
        clickEvents();
    }

    private void findView() {
        back = findViewById(R.id.back);
        listView = findViewById(R.id.listview);
        refreshLayout = findViewById(R.id.refresh);
    }

    public void initialization() {
        adapter = new MessageAdapter(new ArrayList<>());
//        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);TODO
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(adapter);
        if (current_user != null) {
            //查询系统通知数量
            BmobQuery<Message> query_3 = new BmobQuery<Message>();
            query_3.addWhereEqualTo("acceptor", current_user.getObjectId());
            query_3.count(Message.class, new CountListener() {
                @Override
                public void done(Integer count, BmobException e) {
                    if (e == null) {
                        notification_sum = count;
                        initialization_2();
                    }
                }
            });
        } else {
            mock();
        }
    }

    public void initialization_2() {
        //查询是否有未读消息
        BmobQuery<Message> query = new BmobQuery<Message>();
        query.addWhereEqualTo("acceptor", current_user.getObjectId());
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    refresh_list(list);
                }
            }
        });
    }

    private void mock() {
        List<Message> list = new ArrayList<>();
        MessageSystem messageSystem = new MessageSystem();
        messageSystem.setTitle("系统通知");
        messageSystem.setContent("您的语录被选为热门");

        MessageSystemUp messageSystemUp = new MessageSystemUp();
        messageSystemUp.setTitle("系统通知");
        messageSystemUp.setContent("您的语录被选为热门");
        messageSystemUp.setObjId("isfd");
        messageSystemUp.setObjContent("哈哈，这是一条语录");
        messageSystemUp.setObjType(MessageSystemUp.TYPE_SAYING);

        list.add(messageSystemUp.toMessage());
        list.add(messageSystem.toMessage());
        list.add(messageSystem.toMessage());
        list.add(messageSystem.toMessage());
        refresh_list(list);
    }

    public void refresh_list(List<Message> list) {
        List<MessageItem> items = new ArrayList<>();
        for (Message message : list) {
            items.add(new MessageItem(message));
        }
        adapter.replaceData(items);
    }

    public void clickEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobQuery<Message> query = new BmobQuery<Message>();
                query.addWhereEqualTo("acceptor", current_user.getObjectId());
                query.findObjects(new FindListener<Message>() {
                    @Override
                    public void done(List<Message> list, BmobException e) {
                        if (e == null) {
                            refresh_list(list);
                        }
                        refreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    class MessageItem implements MultiItemEntity {
        @NonNull
        Message message;

        public MessageItem(@NonNull Message message) {
            this.message = message;
        }

        @Override
        public int getItemType() {
            return message.getType();
        }

        @NonNull
        public Message getMessage() {
            return message;
        }
    }

    class MessageAdapter extends BaseMultiItemQuickAdapter<MessageItem, BaseViewHolder> {

        public MessageAdapter(List<MessageItem> list) {
            super(list);
            addItemType(Message.MESSAGE_NONE, R.layout.user_message_item_system);
            addItemType(Message.MESSAGE_SYSTEM, R.layout.user_message_item_system);
            addItemType(Message.MESSAGE_FANS, R.layout.user_message_item_user);
            addItemType(Message.MESSAGE_BOOKS_LIKE, R.layout.user_message_item_it);
            addItemType(Message.MESSAGE_BOOKS_COMMENT, R.layout.user_message_item_it);
            addItemType(Message.MESSAGE_BOOKS_REPLY, R.layout.user_message_item_it);
            addItemType(Message.MESSAGE_SAYING_LIKE, R.layout.user_message_item_it);
            addItemType(Message.MESSAGE_SAYING_COMMENT, R.layout.user_message_item_it);
            addItemType(Message.MESSAGE_SAYING_REPLY, R.layout.user_message_item_it);
            addItemType(Message.MESSAGE_SYSTEM_UP, R.layout.user_message_item_system);
        }

        @Override
        protected void convert(BaseViewHolder helper, MessageItem item) {
            switch (item.getItemType()) {
                case Message.MESSAGE_NONE:
                    helper.setText(R.id.title, "不支持的通知格式");
                    helper.setText(R.id.content, "不支持的通知格式");
                    break;
                case Message.MESSAGE_SYSTEM:
                    MessageSystem.Data data = MessageSystem.parse(item.getMessage().getMessage());
                    if (data != null) {
                        setHeadText(helper, R.id.title, data.getTitle(), null);
                        setHeadText(helper, R.id.content, data.getContent(), null);
                    }
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_SYSTEM_UP:
                    MessageSystemUp.Data data2 = MessageSystemUp.parse(item.getMessage().getMessage());
                    if (data2 != null) {
                        setHeadText(helper, R.id.title, data2.getTitle(), null);
                        setHeadText(helper, R.id.content, data2.getContent(), null);
                        setHeadText(helper, R.id.it, data2.getObjContent(), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (MessageSystemUp.TYPE_SAYING == data2.getObjType()) {

                                }
                            }
                        });
                        helper.setVisible(R.id.it, true);
                    }
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_FANS:
                    MessageFans.Data data3 = MessageFans.parse(item.getMessage().getMessage());
                    if (data3 != null) {
                        setHeadImage(helper, R.id.image, data3.getHisHead(), data3.getHe());
                        setHeadText(helper, R.id.title, data3.getHisName() + " 关注了你", null);
                        if (!TextUtils.isEmpty(data3.getHisIntro())) {
                            helper.setVisible(R.id.content, true);
                            setHeadText(helper, R.id.content, data3.getHisIntro(), null);
                        }
                    }
                    break;
                case Message.MESSAGE_BOOKS_LIKE:
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_BOOKS_COMMENT:
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_BOOKS_REPLY:
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_SAYING_LIKE:
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_SAYING_COMMENT:
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
                case Message.MESSAGE_SAYING_REPLY:
                    helper.setText(R.id.time, item.getMessage().getUpdatedAt());
                    break;
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
                    NAV.go(MessageAlertActivity.this, RouterHub.USER_UserDetailActivity, "userId", userId);
                }
            });
        }

        private void setHeadText(BaseViewHolder helper, @IdRes int id, String item) {
            TextView mTextView = helper.getView(id);
            //将TextView的显示文字设置为SpannableString
            mTextView.setText(item);
            //设置该句使文本的超连接起作用
            mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        }

        private void setItImage(BaseViewHolder helper, @IdRes int id, Message item) {
            ImageView iv = helper.getView(id);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NAV.go(MessageAlertActivity.this, RouterHub.USER_UserDetailActivity, "userId", item.getMessage());
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
}
