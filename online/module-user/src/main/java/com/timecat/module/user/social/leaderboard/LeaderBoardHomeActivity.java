/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.timecat.module.user.social.leaderboard;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.timecat.data.bmob.dao.block.LeaderBoardBlockDao;
import com.timecat.data.bmob.data.common.Block;
import com.timecat.identity.readonly.RouterHub;
import com.timecat.component.router.app.NAV;
import com.timecat.module.user.R;
import com.timecat.module.user.adapter.BlockItem;
import com.timecat.module.user.base.BaseBlockListActivity;
import com.timecat.identity.data.service.DataError;
import com.timecat.identity.data.service.OnFindListener;
import com.xiaojinzi.component.anno.RouterAnno;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

;


/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/4/17
 * @description 所有排行榜
 * @usage null
 */
@RouterAnno(hostAndPath = RouterHub.LEADERBOARD_LeaderBoardHomeActivity)
public class LeaderBoardHomeActivity extends BaseBlockListActivity {
    @NonNull
    @Override
    protected String title() {
        return "造物榜";
    }

    @Override
    public void onRefresh() {
        LeaderBoardBlockDao.INSTANCE.findAll(null, new OnFindListener<Block>() {
            @Override
            public void error(@NotNull DataError e) {
                mRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "查询失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(@NotNull List<? extends Block> list) {
                mRefreshLayout.setRefreshing(false);
                if (list.size() == 0) {
                    Toast.makeText(getApplicationContext(), "什么也没找到", Toast.LENGTH_SHORT).show();
                } else {
                    List<BlockItem> l = new ArrayList<>();
                    for (Block b : list) {
                        l.add(new BlockItem(b, b.getType()));
                    }
                    mAdapter.replaceData(l);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.leaderboard_leaderboard, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            finish();
            return true;
        }
        if (i == R.id.recommend) {
            NAV.go(this, RouterHub.LEADERBOARD_RecommendActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
