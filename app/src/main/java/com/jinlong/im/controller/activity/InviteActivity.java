package com.jinlong.im.controller.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jinlong.im.R;
import com.jinlong.im.controller.adapter.InviteAdapter;
import com.jinlong.im.model.Model;
import com.jinlong.im.model.bean.InvationInfo;
import com.jinlong.im.utils.Constant;

import java.util.List;

/**
 * 邀请信息列表页面
 */
public class InviteActivity extends Activity {
    private ListView lv_invite;
    private InviteAdapter inviteAdapter;
    private InviteAdapter.OnInviteListener mOnInviteListener = new InviteAdapter.OnInviteListener() {
        @Override
        public void onAccept(final InvationInfo invationInfo) { //通知环信服务器，点击了接受按钮
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().acceptInvitation(invationInfo.getUser().getHxid());
                        //数据库更新
                        Model.getInstance().getDbManager().getInviteTableDao().updateInvitationStatus(
                                InvationInfo.InvitationStatus.INVITE_ACCEPT , invationInfo.getUser().getHxid());
                        runOnUiThread(new Runnable() { //主线程，页面发送变化
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受了邀请", Toast.LENGTH_SHORT).show();
                                refresh();  //刷新页面
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() { //主线程，页面发送变化
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受邀请失败哦", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
        @Override
        public void onReject(final InvationInfo invationInfo) {    //通知环信服务器，点击了拒绝按钮
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().contactManager().declineInvitation(invationInfo.getUser().getHxid());
                        //数据库变化
                        Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(invationInfo.getUser().getHxid());
                        runOnUiThread(new Runnable() { //主线程，页面发送变化
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "哈哈，你拒绝了ta", Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() { //主线程，页面发送变化
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒接失败哦", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }

        //接受群邀请
        @Override
        public void onInviteAccept(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {   //告诉环信服务器接受了邀请
                        EMClient.getInstance().groupManager().acceptInvitation(invationInfo.getGroup().getGroupId() ,
                                invationInfo.getGroup().getInvatePerson());
                        //本地数据库更新
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_INVITE);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
                        //内存数据变化
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受了群邀请", Toast.LENGTH_SHORT).show();
                                refresh();  //刷新页面
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受群邀请失败", Toast.LENGTH_SHORT).show();
                                refresh();  //刷新页面
                            }
                        });
                    }
                }
            });
        }
        //拒绝群邀请
        @Override
        public void onInviteReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {  //告诉环信服务器拒绝了邀请
                    try {
                        EMClient.getInstance().groupManager().declineInvitation(invationInfo.getGroup().getGroupId() ,
                                invationInfo.getGroup().getInvatePerson() , "拒绝了你的群邀请");
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_INVITE);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝了群邀请", Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝群邀请失败", Toast.LENGTH_SHORT).show();
                                refresh();  //刷新页面
                            }
                        });
                    }
                }
            });
        }
        //接受群申请
        @Override
        public void onApplicationAccept(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().acceptApplication(invationInfo.getGroup().getGroupId(),
                                invationInfo.getGroup().getInvatePerson());
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_ACCEPT_APPLICATION);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受了群申请", Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "接受群申请失败", Toast.LENGTH_SHORT).show();
                                refresh();  //刷新页面
                            }
                        });
                    }
                }
            });
        }
        //拒绝群申请
        @Override
        public void onApplicationReject(final InvationInfo invationInfo) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        EMClient.getInstance().groupManager().declineApplication(invationInfo.getGroup().getGroupId(),
                                invationInfo.getGroup().getInvatePerson() , "拒绝你的群申请");
                        invationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_REJECT_APPLICATION);
                        Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invationInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝了群申请", Toast.LENGTH_SHORT).show();
                                refresh();
                            }
                        });
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(InviteActivity.this, "拒绝群申请失败", Toast.LENGTH_SHORT).show();
                                refresh();  //刷新页面
                            }
                        });
                    }
                }
            });
        }
    };
    private LocalBroadcastManager mLBM;
    private BroadcastReceiver InviteChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            refresh();  //一旦监听到邀请信息变化就刷新页面
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏导航栏
        setContentView(R.layout.activity_invite);
        initView();
        initData();
    }

    private void initData() {
        //初始化Listview
        inviteAdapter = new InviteAdapter(this , mOnInviteListener);
        lv_invite.setAdapter(inviteAdapter);
        //刷新方法
        refresh();
        //注册邀请信息变化的广播
        mLBM = LocalBroadcastManager.getInstance(this);
        mLBM.registerReceiver(InviteChangedReceiver , new IntentFilter(Constant.CONTACT_INVITE_CHANGED));
        mLBM.registerReceiver(InviteChangedReceiver , new IntentFilter(Constant.GROUP_INVITE_CHANGED));
    }
    private void refresh() {
        //获取数据库中所有邀请信息
        List<InvationInfo> invitations = Model.getInstance().getDbManager().getInviteTableDao().getInvitations();
        inviteAdapter.refresh(invitations);   //刷新适配器
    }
    private void initView() {
        lv_invite = (ListView) findViewById(R.id.lv_invite);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLBM.unregisterReceiver(InviteChangedReceiver);  //销毁广播，否则会OOM
    }
}
