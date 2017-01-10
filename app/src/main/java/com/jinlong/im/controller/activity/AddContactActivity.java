package com.jinlong.im.controller.activity;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.jinlong.im.R;
import com.jinlong.im.model.Model;
import com.jinlong.im.model.bean.UserInfo;

/**
 * 添加联系人页面
 */
public class AddContactActivity extends Activity {
    private TextView tv_add_find;
    private EditText et_add_name;
    private RelativeLayout rl_add;
    private TextView tv_add_name;
    private Button bt_add_add;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏导航栏
        setContentView(R.layout.activity_add_contact);
        initView();      //初始化view
        initListener();  //初始化监听
    }

    private void initListener() {
        //查找按钮的点击事件处理
        tv_add_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                find();
            }
        });
        //添加按钮的点击事件处理
        bt_add_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }
    private void find() {   //查找按钮处理
        final String name = et_add_name.getText().toString();  //获取输入的用户名称
        if(TextUtils.isEmpty(name)){  //校验输入的用户名称
            Toast.makeText(AddContactActivity.this, "输入的用户名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //去环信服务器判断当前用户是否存在
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                userInfo = new UserInfo(name);          //获取用户
                runOnUiThread(new Runnable() {           //主线程更新UI
                    @Override
                    public void run() {
                        rl_add.setVisibility(View.VISIBLE);      //显示搜索到的用户
                        tv_add_name.setText(userInfo.getName()); //赋值用户名称
                    }
                });
            }
        });
    }
    private void add() {   //添加按钮处理
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {    //去环信服务器添加好友
                try {
                    EMClient.getInstance().contactManager().addContact(userInfo.getName() , "我想添加你为好友");
                    runOnUiThread(new Runnable() {  //主线程更新UI，去弹吐司
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "成功发送添加好友邀请", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(AddContactActivity.this, "发送添加好友邀请失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    private void initView() {
        tv_add_find = (TextView) findViewById(R.id.tv_add_find);
        et_add_name = (EditText) findViewById(R.id.et_add_name);
        rl_add = (RelativeLayout) findViewById(R.id.rl_add);
        tv_add_name = (TextView) findViewById(R.id.tv_add_name);
        bt_add_add = (Button) findViewById(R.id.bt_add_add);
    }
}
