package com.jinlong.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;
import com.jinlong.im.R;
import com.jinlong.im.controller.adapter.GroupListAdapter;
import com.jinlong.im.model.Model;

import java.util.List;

/**
 * 群组列表页面
 */
public class GroupListActivity extends Activity {
    private ListView lv_grouplist;
    private GroupListAdapter groupListAdapter;
    private LinearLayout ll_grouplist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        initView();
        initData();
        initListener();
    }

    private void initListener() {   //listview条目的点击事件
        lv_grouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    return;   //不作处理
                }
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);
                //传递会话类型（群聊）、群id
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE , EaseConstant.CHATTYPE_GROUP);
                EMGroup emGroup = EMClient.getInstance().groupManager().getAllGroups().get(position - 1);
                intent.putExtra(EaseConstant.EXTRA_USER_ID , emGroup.getGroupId());
                startActivity(intent);
            }
        });
        //跳转到新群
        ll_grouplist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initData() {
        groupListAdapter = new GroupListAdapter(this);
        lv_grouplist.setAdapter(groupListAdapter);
        //从环信服务器获取所有群信息
        getGroupsFromServer();
    }

    private void getGroupsFromServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {  //从网络获取数据
                try {
                    final List<EMGroup> mGroups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    //更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "加载群信息成功", Toast.LENGTH_SHORT).show();
                            //groupListAdapter.refresh(mGroups);
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this , "加载群信息失败" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void refresh(){  //刷新
        groupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }
    private void initView() {
        lv_grouplist = (ListView) findViewById(R.id.lv_grouplist);
        //添加头布局
        View headerView = View.inflate(this, R.layout.header_grouplist, null);
        lv_grouplist.addHeaderView(headerView);

        ll_grouplist = (LinearLayout) headerView.findViewById(R.id.ll_grouplist);
    }

    @Override
    protected void onResume() {   //当Activity可见时，刷新让新群显示
        super.onResume();
        refresh();
    }
}
