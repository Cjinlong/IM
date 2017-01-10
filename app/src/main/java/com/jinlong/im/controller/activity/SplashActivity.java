package com.jinlong.im.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.hyphenate.chat.EMClient;
import com.jinlong.im.R;
import com.jinlong.im.model.Model;
import com.jinlong.im.model.bean.UserInfo;

import static android.R.attr.handle;

/**
 * 导航栏页面
 */
public class SplashActivity extends Activity {

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //如果当前activity已经退出，就不处理handle中的消息
            if(isFinishing()){
                return;
            }
            //判断进入主页面还是登陆页面
            toMainOrLogin();
        }
    };

    //判断进入主页面还是登陆页面
    private void toMainOrLogin() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {  //线程池
            @Override
            public void run() {
                //判断当前账号是否已经登陆过,   环信服务器
                if(EMClient.getInstance().isLoggedInBefore()){  //之前有登陆过
                    //根据环信id获取到当前登陆用户的信息
                    UserInfo accunt = Model.getInstance().getUserAccountDao().getAccountByHxId(EMClient.getInstance().getCurrentUser());
                    if(accunt == null){  //拿不到环信id就跳转到登陆页面
                        Intent intent = new Intent(SplashActivity.this , LoginActivity.class);
                        startActivity(intent);
                    }else{  //跳转到主页面
                        Model.getInstance().loginSuccess(accunt);//登录成功后的方法
                        Intent intent = new Intent(SplashActivity.this , MainActivity.class);
                        startActivity(intent);
                    }
                }else{   //没有登陆过 ,跳转到登陆页面
                    Intent intent = new Intent(SplashActivity.this , LoginActivity.class);
                    startActivity(intent);
                }
                //结束当前页面
                finish();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //隐藏导航栏
        setContentView(R.layout.activity_splash);
        //发送2s的延时消息  , 再跳转
        handler.sendMessageDelayed(Message.obtain() , 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁消息
        handler.removeCallbacksAndMessages(null);
    }
}
