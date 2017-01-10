package com.jinlong.im.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.jinlong.im.R;
import com.jinlong.im.controller.fragment.ChatFragment;
import com.jinlong.im.controller.fragment.ContactListFragment;
import com.jinlong.im.controller.fragment.SettingFragment;

public class MainActivity extends FragmentActivity {
    private RadioGroup rg_main;
    private ChatFragment chatFragment;
    private ContactListFragment contactListFragment;
    private SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏导航栏
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListener();
    }

    private void initListener() {    //RadioGroup的选择事件
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment fragment = null;
                switch (checkedId){
                    case R.id.rb_main_chat:    //会话列表页面
                        fragment = chatFragment;
                        break;
                    case R.id.rb_main_contact://联系人列表页面
                        fragment = contactListFragment;
                        break;
                    case R.id.rb_main_setting://设置页面
                        fragment = settingFragment;
                        break;
                }
                switchFragment(fragment); //实现fragment切换的方法
            }
        });
        //默认选择打开会话列表页面
        rg_main.check(R.id.rb_main_chat);
    }

    private void switchFragment(Fragment fragment) { //实现fragment切换的方法
        FragmentManager fragmentManager = getSupportFragmentManager();  //开启事务
        fragmentManager.beginTransaction().replace(R.id.fl_main , fragment).commit(); //替换相应的fragment
    }

    private void initData() {
        //创建3个fragment对象
        chatFragment = new ChatFragment();
        contactListFragment = new ContactListFragment();
        settingFragment = new SettingFragment();
    }
    private void initView() {
        rg_main = (RadioGroup)findViewById(R.id.rg_main);  //初始化底部按钮布局
    }
}
